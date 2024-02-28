package org.stadium.corelib.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.stadium.corelib.domain.StadiumInfo;

import java.time.LocalDateTime;
import java.util.Optional;

public interface StadiumInfoRepository extends JpaRepository<StadiumInfo, Long>, JpaSpecificationExecutor<StadiumInfo> {
    @Query(nativeQuery = true, value = "SELECT si.id as id,\n" +
            "       si.name as name,\n" +
            "       si.address as address,\n" +
            "       si.price as price,\n" +
            "       si.image_url as image_url,\n" +
            "       si.stadium_id as stadium_id,\n" +
            "       st_distance(st_makepoint(:lon, :lat), st_makepoint(si.lon, si.lat)) as distance\n" +
            "FROM stadium_info si\n" +
            "WHERE not exists(select * from booking b where si.stadium_id = b.stadium_id and booked\n" +
            "                                           and ((:begins > b.from_hour and :begins < b.till_hour)\n" +
            "                                                    or (:ends  > b.from_hour and :ends < b.till_hour)))\n" +
            "ORDER BY distance", countQuery = "SELECT count(*) FROM stadium_info")
    Page<StadiumInfo> findNearestByTimeRangeOrderByDistance(Pageable pageable, @Param("lon") Double lon, @Param("lat") Double lat,
                                                     @Param("begins") LocalDateTime begins, @Param("ends") LocalDateTime ends);

    void deleteAllByStadiumId(Long stadiumId);
}
