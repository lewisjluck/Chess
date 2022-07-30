package com.chess.Chess;

import com.chess.Chess.controller.GameController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessApplication {

	public static void main(String[] args) {
		GameController.initialiseGames();
		SpringApplication.run(ChessApplication.class, args);
	}

}
