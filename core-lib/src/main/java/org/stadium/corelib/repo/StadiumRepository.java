package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.Stadium;

import java.util.List;

public interface StadiumRepository extends JpaRepository<Stadium, Long> {
}
