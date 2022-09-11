package com.jeffy.multipledbs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jeffy.multipledbs.models.Player;
import com.jeffy.multipledbs.services.PlayerRepository;

@Component
public class TestCommandLineRunner implements CommandLineRunner {
	private static Logger logger = LoggerFactory.getLogger(TestCommandLineRunner.class);

	@Autowired
	private PlayerRepository playerRepo;

	@Override
	public void run(String... args) throws Exception {
		List<Player> listPlayers = playerRepo.findAll();
		
		logger.info("Do i get here?");
		
	}

}
