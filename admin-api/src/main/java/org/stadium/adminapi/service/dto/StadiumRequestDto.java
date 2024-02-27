package org.stadium.adminapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import org.stadium.corelib.domain.enums.ImageMode;

import javax.validation.constraints.NotNull;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StadiumRequestDto extends StadiumDto {

    @Nullable
    private MultipartFile imageFile;

    @Nullable
    private MultipartFile compressImageFile;

    @NotNull(message = "longitude is required")
    private Double lon;

    @NotNull(message = "latitude is required")
    private Double lat;

    private ImageMode orientation;
}
