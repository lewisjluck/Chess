package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {
    public Queen(Colour colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return "Q";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour, boolean test) {
        List<Position> directions = new ArrayList<>(straightDirections);
        directions.addAll(diagonalDirections);

        return getMovesFromDirection(directions, board, position, colour);
    }
}
