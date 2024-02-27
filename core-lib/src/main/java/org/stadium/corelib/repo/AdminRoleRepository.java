package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.Role;

import java.util.Set;

public interface AdminRoleRepository extends JpaRepository<Role, Long> {
    @Query(nativeQuery = true, value = "select r.* from role r where r.id in (:ids)")
    Set<Role> findDistinctBySetOfIds(@Param("ids") Set<Long> ids);
}
