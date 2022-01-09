package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.List;

public class Rook extends Piece {

    public Rook(Colour colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Player player) {
        List<Position> directions = straightDirections;

        return getMovesFromDirection(directions, board, position, player);
    }
}
