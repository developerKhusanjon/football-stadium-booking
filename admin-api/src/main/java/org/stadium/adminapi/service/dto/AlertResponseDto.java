package org.stadium.adminapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mamadaliyev Nodirbek
 * @created 19/06/2021 - 9:59
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlertResponseDto {
    private String message;
    private boolean success;
}
