package org.stadium.corelib.domain;

import lombok.*;

import javax.persistence.*;
import org.stadium.corelib.domain.audit.DateAudit;

import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Stadium extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    //Long address info
    @Column(columnDefinition = "TEXT")
    private String address;

    @OneToOne(cascade = CascadeType.PERSIST)
    private StadiumImage image;

    private String phone1;

    private String phone2;

    private String email;

    private BigDecimal price;

    @OneToMany(mappedBy = "stadium", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
