package com.jeffy.dbtestmysql.services;

import org.springframework.data.jpa.repository.JpaRepository;
import com.jeffy.dbtestmysql.models.Player;




public interface PlayerRepository extends JpaRepository<Player, Long> {
	
}
