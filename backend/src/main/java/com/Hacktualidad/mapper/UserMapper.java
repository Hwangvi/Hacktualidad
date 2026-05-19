package com.Hacktualidad.mapper;

import com.Hacktualidad.dto.UserRequestDTO;
import com.Hacktualidad.dto.UserResponseDTO;
import com.Hacktualidad.entity.User;
import com.Hacktualidad.dto.UserUpdateRequestDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

public interface UserMapper {
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(source = "address", target = "address")
    User toUser(UserRequestDTO userRequestDTO);
    UserResponseDTO toUserResponseDTO(User user);
    void updateUserFromDto(UserUpdateRequestDTO dto, @MappingTarget User user);
}