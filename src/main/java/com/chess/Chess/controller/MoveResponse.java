package com.chess.Chess.controller;

import com.chess.Chess.model.Position;
import com.chess.Chess.model.pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveResponse {
    private List<String> position;
    private List<String> display;
    boolean gameOver;

    public MoveResponse() {
        position = new ArrayList<>();
        display = new ArrayList<>();
        gameOver = false;
    }

    public void addTile(Position to, Piece piece) {
        position.add(to.getDisplay());
        display.add(piece != null ? piece.getDisplay() : "NONE");
    }

    public void gameOver() {
        gameOver = true;
    }
}
