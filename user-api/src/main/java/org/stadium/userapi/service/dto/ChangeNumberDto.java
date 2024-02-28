package org.stadium.userapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeNumberDto {
    @NotBlank(message = "Username field is required")
    @Pattern(regexp = "^[9][9][8][0-9]{9}$", message = "Phone number must be 12 digits.")
    private String phone;
    private String otp;
}
