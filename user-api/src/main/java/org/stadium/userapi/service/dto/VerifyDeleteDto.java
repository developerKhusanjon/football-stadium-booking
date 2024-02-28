package org.stadium.userapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyDeleteDto {

    private String confirmationCode;

    private List<Long> reasons;

    private String comment;
}
