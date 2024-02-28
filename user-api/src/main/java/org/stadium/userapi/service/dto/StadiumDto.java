package org.stadium.userapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StadiumDto {
    private Long id;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "description is required")
    private String description;

    //Long address info
    @NotBlank(message = "address is required")
    private String address;

    @NotBlank(message = "shortAddress is required")
    private String shortAddress;

    @NotBlank(message = "phone1 is required")
    private String phone1;

    @NotBlank(message = "phone2 is required")
    private String phone2;

    @NotBlank(message = "email is required")
    private String email;

    private BigDecimal price;
}
