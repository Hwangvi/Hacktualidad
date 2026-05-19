package com.Hacktualidad.controller;

import com.Hacktualidad.dto.PostRequestDTO;
import com.Hacktualidad.dto.PostResponseDTO;
import com.Hacktualidad.service.ForumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ForumRESTController.class)
class ForumRESTControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumService forumService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "forumUser")
    void shouldCreatePost() throws Exception {
        PostRequestDTO request = new PostRequestDTO();
        PostResponseDTO response = new PostResponseDTO();

        when(forumService.createPost(any(PostRequestDTO.class), eq("TechTopic"), eq("forumUser")))
                .thenReturn(response);

        mockMvc.perform(post("/api/forum/topics/TechTopic/posts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }
}