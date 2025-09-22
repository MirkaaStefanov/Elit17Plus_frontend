package com.example.Elit17Plus_frontend.dtos.auth;


import com.example.Elit17Plus_frontend.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PublicUserDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private Role role;
}
