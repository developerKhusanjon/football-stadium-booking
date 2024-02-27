package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.StadiumImage;

import java.util.Optional;

public interface StadiumImageRepository extends JpaRepository<StadiumImage, Long> {
    Optional<StadiumImage> findByUrl(String url);
}
