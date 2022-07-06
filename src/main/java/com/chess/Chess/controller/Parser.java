package com.chess.Chess.controller;

import com.chess.Chess.model.Position;

public class Parser {
    public static Position getPositionFromString(String position) {
        String[] positions = position.split("-");

        int row;
        try {
            row = Integer.parseInt(positions[0]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }

        int col;
        try {
            col = Integer.parseInt(positions[1]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }

        return new Position(row, col);
    }
}
