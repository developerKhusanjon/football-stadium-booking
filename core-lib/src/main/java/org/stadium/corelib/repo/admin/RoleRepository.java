package org.stadium.corelib.repo.admin;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
