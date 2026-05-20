package com.Hacktualidad.service;

import com.Hacktualidad.dto.*;
import com.Hacktualidad.entity.User;
import com.Hacktualidad.mapper.UserMapper;
import com.Hacktualidad.repository.UserRepository;
import com.Hacktualidad.Enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private UserMapper userMapper;
    @Mock private CloudinaryStorageService cloudinaryStorageService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterUserSuccessfully() {
        UserRequestDTO request = new UserRequestDTO();
        request.setEmail("test@test.com");
        request.setPassword("1234");
        request.setConfirmPassword("1234");

        User userEntity = new User();
        userEntity.setEmail("test@test.com");

        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setRole(Role.USER);

        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(userMapper.toUser(request)).thenReturn(userEntity);
        when(passwordEncoder.encode("1234")).thenReturn("hashed1234");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.toUserResponseDTO(savedUser)).thenReturn(new UserResponseDTO());

        UserResponseDTO response = userService.registerUser(request, null);

        assertNotNull(response);
        verify(userRepository).save(userEntity);
        verify(cloudinaryStorageService, never()).storeFile(any());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        LoginRequestDTO login = new LoginRequestDTO();
        login.setEmail("test@test.com");
        login.setPassword("1234");

        User user = new User();
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDTO(user)).thenReturn(new UserResponseDTO());

        Optional<UserResponseDTO> result = userService.loginUser(login);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldUploadPhotoOnUpdate() {
        Long userId = 1L;
        User user = new User();
        MultipartFile file = mock(MultipartFile.class);
        UserUpdateRequestDTO updateDTO = new UserUpdateRequestDTO();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(file.isEmpty()).thenReturn(false);
        when(cloudinaryStorageService.storeFile(file)).thenReturn("new-photo.jpg");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserResponseDTO(user)).thenReturn(new UserResponseDTO());

        userService.updateUser(userId, updateDTO, file);

        assertEquals("new-photo.jpg", user.getPhoto());
        verify(cloudinaryStorageService).storeFile(file);
    }
}