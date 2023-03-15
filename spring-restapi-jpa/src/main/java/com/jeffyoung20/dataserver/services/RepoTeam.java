package com.jeffyoung20.dataserver.services;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffyoung20.dataserver.models.data.Team;

public interface RepoTeam extends JpaRepository<Team, Long> {
	public List<Team> findByNameIgnoreCase(String name);
}
