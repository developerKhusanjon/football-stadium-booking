package org.stadium.corelib.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.stadium.corelib.domain.DeletedUsers;

public interface DeletedUserRepository extends JpaRepository<DeletedUsers,Long> {

}
