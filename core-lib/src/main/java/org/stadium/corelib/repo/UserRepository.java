package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import org.stadium.corelib.domain.User;
import org.stadium.corelib.domain.UserCredential;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true, value = "select exists(select 1 from users where phone =:phone and is_deleted = false)")
    boolean existsByPhone(@Param("phone") String phone);

    @Query(nativeQuery = true, value = "select * from users where (username=:username or phone =:phone) and not (is_deleted) and is_enable")
    Optional<User> findByPhoneOrUsername(@Param("phone") String phone, @Param("username") String username);

    @Query(value = "select u.* from users u join user_credential creadantial on creadantial.user_id = u.id where creadantial.phone=:username and u.is_deleted = true ", nativeQuery = true)
    Optional<User> findUserByCredential(@Param("username") String username);

    @Query(value = "select creadantial.* \n" +
            "from users users\n" +
            "         left join user_credential creadantial on creadantial.user_id = users.id\n" +
            "where creadantial.phone = :username\n", nativeQuery = true)
    Optional<UserCredential> findCredentialsByUsername(@Param("username") String username);

    @Query(value = "select u.*\n" +
            "from users u\n" +
            "         join (select session.*, credential.*\n" +
            "               from user_session session\n" +
            "                        join user_credential credential on credential.id = session.user_credentials_id\n" +
            "               where phone = :phone\n" +
            "                 and user_token = :token\n" +
            "               limit 1) session on session.user_id = u.id\n", nativeQuery = true)
    Optional<User> findByPhoneAndToken(@Param("phone") String phone, @Param("token") String token);

    @Transactional
    @Modifying
    @Query(value = "with a as\n" +
            "         (delete from user_session\n" +
            "             where user_session.user_credentials_id in(select user_credential.id from user_credential\n" +
            "                 where user_credential.user_id=:userId)\n" +
            "        ),\n" +
            "     i as\n" +
            "         (delete from user_credential\n" +
            "             where user_credential.user_id=:userId\n" +
            "           returning user_credential.id),\n" +
            "delete from users\n" +
            "where users.id=:userId\n",nativeQuery = true)
    void deleteUserById(@Param("userId")Long userId);
}
