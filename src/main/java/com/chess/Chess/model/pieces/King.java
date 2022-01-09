package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(Colour colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return "C";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Player player) {
        List<Position> possibleMoves = new ArrayList<>();
        List<Position> directions = diagonalDirections;
        directions.addAll(straightDirections);

        for (Position direction : directions) {
            Position currentPosition = new Position(position);
            currentPosition.add(direction);
            Piece piece = board.getPieceFromPosition(currentPosition);
            if (Board.isOutOfBounds(currentPosition) || (piece != null && piece.getColour() == player.getColour())) {
                continue;
            }
            possibleMoves.add(new Position(currentPosition));
        }

        return possibleMoves;
    }
}
