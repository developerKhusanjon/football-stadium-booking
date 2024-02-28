package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.DeletedUserSession;

import java.util.Optional;

public interface DeletedUserSessionRepository extends JpaRepository<DeletedUserSession,Long> {
    @Query(value = "select  * from deleted_user_session\n" +
            "where user_id=:userId\n" +
            "order by id desc\n" +
            "limit 1",nativeQuery = true)
    Optional<DeletedUserSession> findByUserId(@Param("userId")Long userId);
}
