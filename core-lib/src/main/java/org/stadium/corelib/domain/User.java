package org.stadium.corelib.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import org.stadium.corelib.domain.audit.DateAudit;

import java.time.LocalDateTime;
import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@Builder()
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User extends DateAudit implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "username")
    private String username;

    @Column(name = "is_enable")
    private boolean isEnabled = false;

    @Column(name = "is_completed")
    private boolean isCompleted = false;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    private LocalDateTime loginAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

}
