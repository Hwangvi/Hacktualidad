package com.Hacktualidad.service;

import com.Hacktualidad.Enums.Role;
import com.Hacktualidad.dto.*;
import com.Hacktualidad.entity.ForumComment;
import com.Hacktualidad.entity.ForumTopic;
import com.Hacktualidad.entity.Posts;
import com.Hacktualidad.entity.User;
import com.Hacktualidad.mapper.ForumMapper;
import com.Hacktualidad.repository.ForumCommentRepository;
import com.Hacktualidad.repository.ForumTopicRepository;
import com.Hacktualidad.repository.PostsRepository;
import com.Hacktualidad.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ForumServiceImpl implements ForumService {

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private ForumTopicRepository forumTopicRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryStorageService cloudinaryStorageService;

    @Autowired
    private ForumCommentRepository forumCommentRepository;

    @Autowired
    private ForumMapper forumMapper;

    @Override
    public List<TopicDTO> getAllTopics() {
        List<ForumTopic> topics = forumTopicRepository.findAll();
        return forumMapper.toTopicDTOList(topics);
    }

    @Override
    public List<PostResponseDTO> getPostsByTopicName(String topicName) {
        ForumTopic topic = forumTopicRepository.findByTopicName(topicName)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el tema: " + topicName));

        List<Posts> posts = postsRepository.findByForumTopic(topic);
        return forumMapper.toPostResponseDTOList(posts);
    }

    @Override
    public PostResponseDTO updatePost(Long postId, PostRequestDTO postRequestDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + userEmail));
        Posts postToUpdate = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con ID: " + postId));

        if (!postToUpdate.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para editar este post");
        }

        postToUpdate.setTitle(postRequestDTO.getTitle());
        postToUpdate.setContent(postRequestDTO.getContent());

        Posts updatedPost = postsRepository.save(postToUpdate);
        return forumMapper.toPostResponseDTO(updatedPost);
    }

    @Override
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO, String topicName, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + userEmail));
        ForumTopic topic = forumTopicRepository.findByTopicName(topicName)
                .orElseThrow(() -> new EntityNotFoundException("Tema no encontrado: " + topicName));

        Posts newPost = new Posts();
        newPost.setTitle(postRequestDTO.getTitle());
        newPost.setContent(postRequestDTO.getContent());
        newPost.setForumTopic(topic);
        newPost.setUser(user);
        newPost.setPublicationDate(LocalDateTime.now());
        newPost.setActive(true);

        Posts savedPost = postsRepository.save(newPost);
        return forumMapper.toPostResponseDTO(savedPost);
    }

    @Override
    public void deletePost(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + userEmail));
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con ID: " + postId));

        if (!post.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para borrar este post");
        }

        postsRepository.delete(post);
    }

    @Override
    public PostResponseDTO getPostById(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con ID: " + postId));
        return forumMapper.toPostResponseDTO(post);
    }

    @Override
    public CommentResponseDTO addCommentToPost(Long postId, CommentRequestDTO commentRequestDTO, String userEmail) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post no encontrado con ID: " + postId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + userEmail));

        ForumComment newComment = new ForumComment();
        newComment.setContent(commentRequestDTO.getContent());
        newComment.setPost(post);
        newComment.setUser(user);
        newComment.setCommentDate(LocalDateTime.now());
        newComment.setActive(true);

        ForumComment savedComment = forumCommentRepository.save(newComment);
        return forumMapper.toCommentResponseDTO(savedComment);
    }

    @Override
    public TopicDTO createTopic(TopicDTO topicDTO, MultipartFile file) {
        ForumTopic newTopic = new ForumTopic();
        newTopic.setTopicName(topicDTO.getTopicName());
        newTopic.setTopicDescription(topicDTO.getTopicDescription());
        newTopic.setCreationDate(LocalDateTime.now());
        newTopic.setActive(true);

        if (file != null && !file.isEmpty()) {
            String fileUrl = cloudinaryStorageService.storeFile(file);
            newTopic.setBackgroundImage(fileUrl);
        }
        ForumTopic savedTopic = forumTopicRepository.save(newTopic);
        return forumMapper.toTopicDTO(savedTopic);
    }

    @Override
    public List<PostResponseDTO> getAllPosts() {
        List<Posts> posts = postsRepository.findAll(Sort.by(Sort.Direction.DESC, "publicationDate"));
        return forumMapper.toPostResponseDTOList(posts);
    }

    @Override
    public void deleteTopic(int topicId) {
        ForumTopic topicToDelete = forumTopicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Temática no encontrada con ID: " + topicId));

        String backgroundImage = topicToDelete.getBackgroundImage();
        if (backgroundImage != null && !backgroundImage.isEmpty()) {
            cloudinaryStorageService.deleteFile(backgroundImage);
        }
        forumTopicRepository.delete(topicToDelete);
    }

    @Override
    public void deleteComment(Long commentId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado: " + userEmail));

        ForumComment comment = forumCommentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comentario no encontrado ID: " + commentId));

        if (!comment.getUser().getUserId().equals(user.getUserId()) && user.getRole() != Role.ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tienes permiso para borrar este comentario");
        }

        forumCommentRepository.delete(comment);
    }
}