package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.UserSession;

import java.util.Optional;


public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
    @Query(value = "select session.*\n" +
            "from user_session session\n" +
            "         join user_credential credential on session.user_credentials_id = credential.id\n" +
            "where user_token = :token\n" +
            "  and phone = :phone\n", nativeQuery = true)
    Optional<UserSession> findByUserTokenAndPhone(@Param("token") String token, @Param("phone") String phone);

    Optional<UserSession> findByUserToken(String userToken);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_session where id = :id")
    void deleteUserSession(@Param("id") Long id);

    @Modifying
    @Query(nativeQuery = true, value = "delete from user_session where user_credentials_id = :cr_id and id <> :id")
    void disableOtherSessions(@Param("cr_id") Long crId, @Param("id") Long id);

    @Query(value = "select us.* from user_session us " +
            "join user_credential uc on uc.user_id = :userId\n" +
            "where uc.id = us.user_credentials_id and us.is_enable", nativeQuery = true)
    Optional<UserSession> findByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(nativeQuery = true, value = "update user_session set is_enable = true where id = :id")
    void update(@Param("id") Long id);
}
