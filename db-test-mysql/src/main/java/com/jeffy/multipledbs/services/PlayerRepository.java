package com.jeffy.multipledbs.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jeffy.multipledbs.models.Player;


public interface PlayerRepository extends JpaRepository<Player, Long> {
	
}
