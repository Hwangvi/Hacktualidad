package com.Hacktualidad.service;

import com.Hacktualidad.dto.LoginRequestDTO;
import com.Hacktualidad.dto.UserRequestDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import com.Hacktualidad.entity.User;
import com.Hacktualidad.mapper.UserMapper;
import com.Hacktualidad.repository.UserRepository;
import com.Hacktualidad.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.Hacktualidad.dto.UserUpdateRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private KafkaTemplate<String, UserResponseDTO> kafkaTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FileStorageService fileStorageService;

    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO, MultipartFile file) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {  }
        if (!userRequestDTO.getPassword().equals(userRequestDTO.getConfirmPassword())) {  }

        User user = userMapper.toUser(userRequestDTO);
        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.storeFile(file);
            user.setPhoto(filename);
        }

        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);

        UserResponseDTO response = userMapper.toUserResponseDTO(savedUser);

        kafkaTemplate.send("user-registration-topic", response);
        return response;
    }

    @Override
    public Optional<UserResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow();
            return Optional.of(userMapper.toUserResponseDTO(user));

        } catch (BadCredentialsException e) {
            return Optional.empty();
        }
    }

    @Override
    public UserResponseDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO, MultipartFile file) {
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));

        userMapper.updateUserFromDto(userUpdateRequestDTO, userToUpdate);

        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.storeFile(file);
            userToUpdate.setPhoto(filename);
        }

        User updatedUser = userRepository.save(userToUpdate);
        return userMapper.toUserResponseDTO(updatedUser);
    }
    @Override
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO findUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + userId));
        return userMapper.toUserResponseDTO(user);
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO, MultipartFile file) {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está en uso.");
        }
        if (!userRequestDTO.getPassword().equals(userRequestDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        User user = userMapper.toUser(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));

        if (file != null && !file.isEmpty()) {
            String filename = fileStorageService.storeFile(file);
            user.setPhoto(filename);
        }

        if (userRequestDTO.getRole() == null) {
            user.setRole(Role.USER);
        } else {
            user.setRole(userRequestDTO.getRole());
        }

        User savedUser = userRepository.save(user);
        return userMapper.toUserResponseDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("No se encontró el usuario con ID: " + id);
        }
        userRepository.deleteById(id);
    }


}