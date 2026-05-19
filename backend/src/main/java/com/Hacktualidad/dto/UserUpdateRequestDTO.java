package com.Hacktualidad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.Hacktualidad.Enums.Role;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String surname;
    private String address;
    private Integer phone;
    private String photo;
    private Role role;
}