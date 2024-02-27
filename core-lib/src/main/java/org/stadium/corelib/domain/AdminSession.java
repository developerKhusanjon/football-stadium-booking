package org.stadium.corelib.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "admin_sessions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String token;


    private String deviceIp;

    private String deviceModel;

    private String deviceOsVersion;

    @CreationTimestamp
    private LocalDateTime loginAt;

}
