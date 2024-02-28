package org.stadium.userapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationDto {
    private String token;
    private String confirmationCode;
    private String applicationToken;
    private String platform;
}
