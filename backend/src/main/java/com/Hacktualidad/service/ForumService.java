package com.Hacktualidad.service;

import com.Hacktualidad.dto.PostRequestDTO;
import com.Hacktualidad.dto.PostResponseDTO;
import com.Hacktualidad.dto.TopicDTO;
import com.Hacktualidad.dto.CommentRequestDTO;
import com.Hacktualidad.dto.CommentResponseDTO;
import com.Hacktualidad.entity.Posts;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ForumService {
    List<TopicDTO> getAllTopics();
    List<PostResponseDTO> getPostsByTopicName(String topicName);
    PostResponseDTO getPostById(Long postId);
    PostResponseDTO createPost(PostRequestDTO postRequestDTO, String topicName, String userEmail);
    void deletePost(Long postId, String userEmail);
    CommentResponseDTO addCommentToPost(Long postId, CommentRequestDTO commentRequestDTO, String userEmail);
    TopicDTO createTopic(TopicDTO topicDTO, MultipartFile file);
    PostResponseDTO updatePost(Long postId, PostRequestDTO postRequestDTO, String userEmail);
    List<PostResponseDTO> getAllPosts();
    void deleteTopic(int topicId);
    void deleteComment(Long commentId, String userEmail);
}
