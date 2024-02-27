package org.stadium.corelib.repo.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.AdminSession;


public interface AdminSessionRepository extends JpaRepository<AdminSession, Long> {
    Page<AdminSession> findAllByAdminId(Long admin_id, Pageable pageable);
}
