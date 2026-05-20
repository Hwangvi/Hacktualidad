package com.Hacktualidad.service;

import com.Hacktualidad.dto.*;
import com.Hacktualidad.entity.*;
import com.Hacktualidad.Enums.*;
import com.Hacktualidad.mapper.ForumMapper;
import com.Hacktualidad.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceImplTest {

    @Mock
    private PostsRepository postsRepository;
    @Mock
    private ForumTopicRepository forumTopicRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CloudinaryStorageService cloudinaryStorageService;
    @Mock
    private ForumCommentRepository forumCommentRepository;
    @Mock
    private ForumMapper forumMapper;

    @InjectMocks
    private ForumServiceImpl forumService;

    private User user;
    private User admin;
    private ForumTopic topic;
    private Posts post;
    private ForumComment comment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1L);
        user.setEmail("user@test.com");
        user.setRole(Role.USER);

        admin = new User();
        admin.setUserId(99L);
        admin.setEmail("admin@test.com");
        admin.setRole(Role.ADMIN);

        topic = new ForumTopic();
        topic.setTopicId(10);
        topic.setTopicName("Java");
        topic.setTopicDescription("Java Programming");

        post = new Posts();
        post.setPostId(100L);
        post.setTitle("Test Post");
        post.setUser(user);
        post.setForumTopic(topic);

        comment = new ForumComment();
        comment.setCommentId(100L);
        comment.setUser(user);
        comment.setPost(post);
    }


    @Test
    void getAllTopics_ShouldReturnList() {
        when(forumTopicRepository.findAll()).thenReturn(Arrays.asList(topic));
        when(forumMapper.toTopicDTOList(any())).thenReturn(Arrays.asList(new TopicDTO()));

        List<TopicDTO> result = forumService.getAllTopics();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(forumTopicRepository).findAll();
    }

    @Test
    void createTopic_WithFile_ShouldSaveAndReturnDTO() {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setTopicName("New Topic");
        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(cloudinaryStorageService.storeFile(file)).thenReturn("image.jpg");
        when(forumTopicRepository.save(any(ForumTopic.class))).thenReturn(topic);
        when(forumMapper.toTopicDTO(any(ForumTopic.class))).thenReturn(topicDTO);

        TopicDTO result = forumService.createTopic(topicDTO, file);

        assertNotNull(result);
        verify(cloudinaryStorageService).storeFile(file);
        verify(forumTopicRepository).save(any(ForumTopic.class));
    }

    @Test
    void deleteTopic_WithImage_ShouldDeleteFileAndEntity() {
        topic.setBackgroundImage("image.jpg");
        when(forumTopicRepository.findById(10)).thenReturn(Optional.of(topic));

        forumService.deleteTopic(10);

        verify(cloudinaryStorageService).deleteFile("image.jpg");
        verify(forumTopicRepository).delete(topic);
    }

    @Test
    void deleteTopic_NotFound_ShouldThrowException() {
        when(forumTopicRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> forumService.deleteTopic(99));
    }


    @Test
    void getPostsByTopicName_Success() {
        when(forumTopicRepository.findByTopicName("Java")).thenReturn(Optional.of(topic));
        when(postsRepository.findByForumTopic(topic)).thenReturn(Arrays.asList(post));
        when(forumMapper.toPostResponseDTOList(any())).thenReturn(Arrays.asList(new PostResponseDTO()));

        List<PostResponseDTO> result = forumService.getPostsByTopicName("Java");

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void createPost_Success() {
        PostRequestDTO request = new PostRequestDTO();
        request.setTitle("New Post");
        request.setContent("Content");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(forumTopicRepository.findByTopicName("Java")).thenReturn(Optional.of(topic));
        when(postsRepository.save(any(Posts.class))).thenReturn(post);
        when(forumMapper.toPostResponseDTO(any(Posts.class))).thenReturn(new PostResponseDTO());

        PostResponseDTO result = forumService.createPost(request, "Java", "user@test.com");

        assertNotNull(result);
        verify(postsRepository).save(any(Posts.class));
    }

    @Test
    void getPostById_Success() {
        when(postsRepository.findById(100L)).thenReturn(Optional.of(post));
        when(forumMapper.toPostResponseDTO(post)).thenReturn(new PostResponseDTO());

        PostResponseDTO result = forumService.getPostById(100L);

        assertNotNull(result);
    }

    @Test
    void updatePost_Owner_Success() {
        PostRequestDTO request = new PostRequestDTO();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(postsRepository.findById(100L)).thenReturn(Optional.of(post));
        when(postsRepository.save(any(Posts.class))).thenReturn(post);
        when(forumMapper.toPostResponseDTO(post)).thenReturn(new PostResponseDTO());

        PostResponseDTO result = forumService.updatePost(100L, request, "user@test.com");

        assertNotNull(result);
        assertEquals("Updated Title", post.getTitle());
    }

    @Test
    void updatePost_NotOwnerNotAdmin_ShouldThrowForbidden() {
        User otherUser = new User();
        otherUser.setUserId(2L);
        otherUser.setEmail("other@test.com");
        otherUser.setRole(Role.USER);

        when(userRepository.findByEmail("other@test.com")).thenReturn(Optional.of(otherUser));
        when(postsRepository.findById(100L)).thenReturn(Optional.of(post));

        assertThrows(ResponseStatusException.class, () ->
                forumService.updatePost(100L, new PostRequestDTO(), "other@test.com")
        );
    }

    @Test
    void deletePost_Admin_Success() {
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(admin));
        when(postsRepository.findById(100L)).thenReturn(Optional.of(post));

        forumService.deletePost(100L, "admin@test.com");

        verify(postsRepository).delete(post);
    }

    @Test
    void getAllPosts_ShouldReturnSortedList() {
        when(postsRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(post));
        when(forumMapper.toPostResponseDTOList(any())).thenReturn(Arrays.asList(new PostResponseDTO()));

        List<PostResponseDTO> result = forumService.getAllPosts();

        assertNotNull(result);
        verify(postsRepository).findAll(any(Sort.class));
    }

    @Test
    void addCommentToPost_Success() {
        CommentRequestDTO request = new CommentRequestDTO();
        request.setContent("Nice post");

        when(postsRepository.findById(100L)).thenReturn(Optional.of(post));
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(forumCommentRepository.save(any(ForumComment.class))).thenReturn(comment);
        when(forumMapper.toCommentResponseDTO(any(ForumComment.class))).thenReturn(new CommentResponseDTO());

        CommentResponseDTO result = forumService.addCommentToPost(100L, request, "user@test.com");

        assertNotNull(result);
        verify(forumCommentRepository).save(any(ForumComment.class));
    }

    @Test
    void deleteComment_Owner_Success() {
        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(forumCommentRepository.findById(500L)).thenReturn(Optional.of(comment));

        forumService.deleteComment(500L, "user@test.com");

        verify(forumCommentRepository).delete(comment);
    }

    @Test
    void deleteComment_Forbidden_ShouldThrowException() {
        User hacker = new User();
        hacker.setUserId(666L);
        hacker.setRole(Role.USER);

        when(userRepository.findByEmail("hacker@test.com")).thenReturn(Optional.of(hacker));
        when(forumCommentRepository.findById(500L)).thenReturn(Optional.of(comment));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                forumService.deleteComment(500L, "hacker@test.com")
        );
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
    }

    @Test
    void createPost_UserNotFound_ShouldThrowException() {
        PostRequestDTO request = new PostRequestDTO();
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                forumService.createPost(request, "Topic", "unknown@test.com")
        );
    }
}