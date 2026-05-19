package com.Hacktualidad.mapper;

import com.Hacktualidad.dto.AuthorDTO;
import com.Hacktualidad.dto.CommentResponseDTO;
import com.Hacktualidad.dto.PostResponseDTO;
import com.Hacktualidad.dto.TopicDTO;
import com.Hacktualidad.entity.ForumComment;
import com.Hacktualidad.entity.ForumTopic;
import com.Hacktualidad.entity.Posts;
import com.Hacktualidad.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ForumMapper {
    AuthorDTO toAuthorDTO(User user);
    TopicDTO toTopicDTO(ForumTopic topic);
    List<TopicDTO> toTopicDTOList(List<ForumTopic> topics);
    @Mapping(source = "user", target = "author")
    CommentResponseDTO toCommentResponseDTO(ForumComment comment);
    List<CommentResponseDTO> toCommentResponseDTOList(List<ForumComment> comments);
    @Mappings({
            @Mapping(source = "user", target = "author"),
            @Mapping(source = "comments", target = "comments"),
            @Mapping(source = "forumTopic.topicName", target = "topicName")
    })
    PostResponseDTO toPostResponseDTO(Posts post);
    default List<PostResponseDTO> toPostResponseDTOList(List<Posts> posts) {
        if (posts == null) {
            return Collections.emptyList();
        }
        return posts.stream()
                .map(this::toPostResponseDTO)
                .collect(Collectors.toList());
    }
}