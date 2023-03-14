package com.jeffyoung20.dataserver.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffyoung20.dataserver.models.data.Team;

public interface TeamRepo extends JpaRepository<Team, Long> {
	public List<Team> findByNameIgnoreCase(String name);
}
