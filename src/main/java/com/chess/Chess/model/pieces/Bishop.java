package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

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
    public List<Position> getPossibleMoves(Board board, Position position, Player player) {
        List<Position> directions = diagonalDirections;

        return getMovesFromDirection(directions, board, position, player);
    }
}
