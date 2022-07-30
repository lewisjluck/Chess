package com.chess.Chess.controller;

import com.chess.Chess.model.Board;

import java.util.HashMap;

public class GameController {
    private static HashMap<Integer, Board> games;
    private static int currentId;

    public static void initialiseGames() {
        games = new HashMap<>();
        currentId = 1;
    }

    public static int newGame() {
        games.put(currentId, new Board());
        return currentId ++;
    }

    public static Board getGame(int id) {
        return games.get(id);
    }
}
