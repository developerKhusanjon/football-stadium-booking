package org.stadium.corelib.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.Booking;

import java.time.LocalDateTime;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findAllByStadiumId(Long stadium_id, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from booking b where (:begins <= b.from_hour and :ends >= b.till_hour)", countQuery = "SELECT count(*) FROM booking")
    Page<Booking> findAllByTimeRange(Pageable pageable, @Param("begins") LocalDateTime begins, @Param("ends") LocalDateTime ends);

    @Modifying
    @Query(nativeQuery = true, value = "update booking b set booked = false where b.id = :id")
    void cancelBookingById(Long id);

    @Query(nativeQuery = true, value = "exists(select * from booking b where b.id = :id and b.user_id = :userId)")
    boolean checkForBooked(@Param("id") Long bookingId, @Param("userId") Long userId);
}
