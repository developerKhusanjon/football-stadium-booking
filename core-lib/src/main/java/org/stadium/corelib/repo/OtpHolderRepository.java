package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.OtpHolder;

public interface OtpHolderRepository extends JpaRepository<OtpHolder, Long> {
    void deleteAllByPhone(String phone);

    boolean existsByPhoneAndOtp(String phone, String otp);
}
