package org.stadium.adminapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminSessionDto {
    private Long id;

    private AdminDto admin;

    private String token;

    private String deviceIp;

    private String deviceModel;

    private String deviceOsVersion;
}
