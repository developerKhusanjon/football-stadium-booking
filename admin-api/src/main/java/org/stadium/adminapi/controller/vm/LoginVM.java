package org.stadium.adminapi.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginVM {
    @NotBlank(message = "Username field is required")
    private String username;

    @NotBlank(message = "Password field is required")
    private String password;

}
