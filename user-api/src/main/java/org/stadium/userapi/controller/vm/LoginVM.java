package org.stadium.userapi.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginVM {
    @NotBlank(message = "Username field is required")
    @Pattern(regexp = "^[9][9][8][0-9]{9}$", message = "Phone number must be 12 digits.")
    private String phoneNumber;
    private String hash;
    private String name;
    private String surname;
}
