package org.stadium.corelib.domain;

import lombok.*;

import javax.persistence.*;
import org.stadium.corelib.domain.audit.DateAudit;


@EqualsAndHashCode(callSuper = true)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSession extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_credentials_id")
    private UserCredential userCredential;

    @Column(name = "user_token", columnDefinition = "TEXT")
    private String userToken;

    @Column(name = "sms_code")
    private String smsCode;

    @Column(name = "device_ip")
    private String deviceIp;

    @Column(name = "device_model")
    private String deviceModel;

    @Column(name = "device_os_version")
    private String deviceOsVersion;

    @Column(name = "is_enable")
    private boolean isEnabled = false;

}
