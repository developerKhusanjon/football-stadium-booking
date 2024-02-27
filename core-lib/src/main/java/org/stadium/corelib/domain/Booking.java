package org.stadium.corelib.domain;

import lombok.*;
import org.stadium.corelib.domain.audit.DateAudit;
import org.stadium.corelib.domain.audit.UserDateAudit;

import javax.persistence.*;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Stadium stadium;

    private LocalDateTime fromHour;

    private LocalDateTime tillHour;

    private boolean booked;
}
