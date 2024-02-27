package org.stadium.corelib.repo.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.Admin;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndIdNot(String username, Long id);

}
