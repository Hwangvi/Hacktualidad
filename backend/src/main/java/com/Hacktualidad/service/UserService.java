package com.Hacktualidad.service;

import com.Hacktualidad.dto.LoginRequestDTO;
import com.Hacktualidad.dto.UserRequestDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import com.Hacktualidad.dto.UserUpdateRequestDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import java.util.List;

public interface UserService {
    Optional<UserResponseDTO> loginUser(LoginRequestDTO loginRequestDTO);
    UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO, MultipartFile file);
    UserResponseDTO registerUser(UserRequestDTO userRequestDTO, MultipartFile file);
    List<UserResponseDTO> findAllUsers();
    UserResponseDTO findUserById(Long userId);
    UserResponseDTO createUser(UserRequestDTO userRequestDTO, MultipartFile file);
    void deleteUser(Long id);
}