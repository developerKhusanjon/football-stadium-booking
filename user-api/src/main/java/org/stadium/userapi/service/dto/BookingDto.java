package org.stadium.userapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDto {
    private Long id;

    private UserDto user;

    private StadiumDto stadium;

    private LocalDateTime fromHour;

    private LocalDateTime tillHour;

    private boolean booked;
}
