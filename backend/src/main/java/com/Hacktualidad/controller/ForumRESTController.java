package com.Hacktualidad.controller;

import com.Hacktualidad.dto.CommentResponseDTO;
import com.Hacktualidad.dto.CommentRequestDTO;
import com.Hacktualidad.dto.PostRequestDTO;
import com.Hacktualidad.dto.PostResponseDTO;
import com.Hacktualidad.dto.TopicDTO;
import com.Hacktualidad.service.ForumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/forum")
@Tag(name = "Foro de Discusión", description = "Gestión de Temas (Topics), Posts y Comentarios")
public class ForumRESTController {

    @Autowired
    private ForumService forumService;

    @Operation(summary = "Listar temas", description = "Obtiene todos los temas principales del foro.")
    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        return ResponseEntity.ok(forumService.getAllTopics());
    }

    @Operation(summary = "Posts por tema", description = "Obtiene todos los posts dentro de un tema específico.")
    @GetMapping("/topics/{topicName}/posts")
    public ResponseEntity<List<PostResponseDTO>> getPostsByTopic(@PathVariable String topicName) {
        return ResponseEntity.ok(forumService.getPostsByTopicName(topicName));
    }

    @Operation(summary = "Editar post", description = "El autor puede editar su post.")
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId, @RequestBody PostRequestDTO postRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        PostResponseDTO updatedPost = forumService.updatePost(postId, postRequestDTO, userEmail);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(summary = "Crear post", description = "Publica un nuevo post en un tema.")
    @PostMapping("/topics/{topicName}/posts")
    public ResponseEntity<PostResponseDTO> createPost(@PathVariable String topicName, @RequestBody PostRequestDTO postRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        PostResponseDTO newPost = forumService.createPost(postRequestDTO, topicName, userEmail);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @Operation(summary = "Eliminar post", description = "Elimina un post (requiere ser autor o Admin).")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        String userEmail = authentication.getName();
        forumService.deletePost(postId, userEmail);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Comentar en post", description = "Añade un comentario a un post existente.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> addCommentToPost(@PathVariable Long postId, @RequestBody CommentRequestDTO commentRequestDTO, Authentication authentication) {
        String userEmail = authentication.getName();
        CommentResponseDTO newComment = forumService.addCommentToPost(postId, commentRequestDTO, userEmail);
        return new ResponseEntity<>(newComment, HttpStatus.CREATED);
    }

    @Operation(summary = "Crear tema (Admin)", description = "Crea un nuevo 'Topic' principal con icono.")
    @PostMapping(value = "/topics", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TopicDTO> createTopic(
            @RequestPart("topic") String topicDtoString,
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        TopicDTO topicDTO = new ObjectMapper().readValue(topicDtoString, TopicDTO.class);
        TopicDTO newTopic = forumService.createTopic(topicDTO, file);

        return new ResponseEntity<>(newTopic, HttpStatus.CREATED);
    }

    @Operation(summary = "Ver post por ID")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        PostResponseDTO post = forumService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @Operation(summary = "Listar todos los posts (Admin)")
    @GetMapping("/posts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        return ResponseEntity.ok(forumService.getAllPosts());
    }

    @Operation(summary = "Eliminar tema (Admin)")
    @DeleteMapping("/topics/{topicId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTopic(@PathVariable int topicId) {
        forumService.deleteTopic(topicId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar comentario")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId, Authentication authentication) {
        String userEmail = authentication.getName();
        forumService.deleteComment(commentId, userEmail);
        return ResponseEntity.noContent().build();
    }
}