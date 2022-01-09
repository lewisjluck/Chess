package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

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
    public List<Position> getPossibleMoves(Board board, Position position, Player player) {
        List<Position> directions = straightDirections;
        directions.addAll(diagonalDirections);

        return getMovesFromDirection(directions, board, position, player);
    }
}
