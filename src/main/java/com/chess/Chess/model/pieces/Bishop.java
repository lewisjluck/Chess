package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Piece {
    public Bishop(Colour colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return "B";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour, boolean test) {
        List<Position> directions = new ArrayList<>(diagonalDirections);

        return getMovesFromDirection(directions, board, position, colour);
    }
}
