package org.stadium.adminapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StadiumInfoDto {
    private Long id;

    private String name;

    private String imageUrl;

    //Short address info
    private String address;

    private BigDecimal price;

    private Long stadiumId;

    private Double lon;

    private Double lat;
}

