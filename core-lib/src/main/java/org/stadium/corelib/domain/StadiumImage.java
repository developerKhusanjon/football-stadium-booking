package org.stadium.corelib.domain;

import lombok.*;

import javax.persistence.*;
import org.stadium.corelib.domain.audit.DateAudit;
import org.stadium.corelib.domain.enums.ImageMode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StadiumImage extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String compressUrl;

    private Double width;

    private Double height;

    private String contentType;

    @Enumerated(EnumType.STRING)
    private ImageMode orientation;
}
