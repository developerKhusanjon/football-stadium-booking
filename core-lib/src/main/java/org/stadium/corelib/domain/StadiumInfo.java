package org.stadium.corelib.domain;

import lombok.*;
import org.stadium.corelib.domain.audit.DateAudit;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StadiumInfo extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    //Short address info
    private String address;

    private BigDecimal price;

    @Column(name = "stadium_id", nullable = false)
    private Long stadiumId;

    private Long imageId;

    private Double lon;

    private Double lat;
}
