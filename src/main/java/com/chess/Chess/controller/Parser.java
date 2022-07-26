package com.chess.Chess.controller;

import com.chess.Chess.model.Position;

import java.util.List;
import java.util.stream.Collectors;

public class Parser {
    public static Position getPositionFromString(String position) {
        String[] positions = position.substring(1).split("-");

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

    public static List<String> formatJsonMoves(List<Position> positions) {
        return positions.stream().map(Position::getDisplay).collect(Collectors.toList());
    }

    public static Position getPositionFromNotation(String pos) {
        //LAN = long algebraic notation
        int column = pos.charAt(0) - 97;
        int row = 8 - Character.getNumericValue(pos.charAt(1));
        return new Position(row, column);
    }
}
