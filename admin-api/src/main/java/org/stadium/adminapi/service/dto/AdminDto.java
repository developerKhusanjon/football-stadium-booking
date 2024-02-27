package org.stadium.adminapi.service.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminDto {
    private Long id;

    @NotBlank(message = "F.I.O is required")
    private String fio;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "password is required")
    private String password;

    @JsonSetter
    @NotBlank(message = "ConfirmPassword is required")
    private String confirmPassword;
    @NotBlank(message = "phone is required")
    private String phone;

    private Set<RoleDto> roles;

}
