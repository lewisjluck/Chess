package com.chess.Chess.controller;

import com.chess.Chess.model.Position;
import com.chess.Chess.model.pieces.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveResponse {
    private List<String> position;
    private List<String> display;
    boolean checkmate;
    boolean stalemate;

    public MoveResponse() {
        position = new ArrayList<>();
        display = new ArrayList<>();
        checkmate = false;
        stalemate = false;
    }

    public void addTile(Position to, Piece piece) {
        position.add(to.getDisplay());
        display.add(piece != null ? piece.getDisplay() : "NONE");
    }

    public void setCheckmate() {
        checkmate = true;
    }

    public void setStalemate() { stalemate = true; }

    public List<String> getDisplay() {
        return display;
    }

    public List<String> getPosition() {
        return position;
    }

    public boolean isCheckmate() {
        return checkmate;
    }

    public boolean isStalemate() {
        return stalemate;
    }
}
