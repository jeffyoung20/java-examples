package com.jeffy.multipledbs.baseball.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffy.multipledbs.baseball.models.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
	
}
