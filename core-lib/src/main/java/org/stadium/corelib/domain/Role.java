package org.stadium.corelib.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import javax.persistence.*;
import org.stadium.corelib.domain.enums.RoleName;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
