package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.UserCredential;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    @Query(nativeQuery = true, value = "select * from user_credential\n" +
            "where user_id = :id and phone = CAST(:phone as VARCHAR)")
    Optional<UserCredential> findByPhoneAndId(@Param("phone") String phone, @Param("id") Long id);

    Optional<UserCredential> findByPhoneAndUserId(String phone, Long userId);

    @Query(nativeQuery = true, value = "update user_credential set phone = :newPhone where user_id = :userId and phone = :phone")
    @Modifying
    void updatePhone(@Param("newPhone") String newPhone, @Param("userId") Long userId, @Param("phone") String phone);
}
