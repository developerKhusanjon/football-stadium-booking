package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.DeviceToken;

import java.util.Optional;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {
    void deleteByPushToken(String pushToken);

    Optional<DeviceToken> findFirstByUserSessionId(Long userSession_id);

    void deleteAllByUserId(Long user_id);

    Optional<DeviceToken> findByUserIdAndPushToken(Long user_id, String pushToken);
}

