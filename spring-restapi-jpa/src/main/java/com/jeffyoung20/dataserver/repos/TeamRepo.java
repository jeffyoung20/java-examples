package com.jeffyoung20.dataserver.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffyoung20.dataserver.models.data.Team;

public interface TeamRepo extends JpaRepository<Team, Long> {

}
