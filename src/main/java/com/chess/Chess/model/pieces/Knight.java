package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Colour colour) {
        super(colour);
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour) {
        List<Position> possibleMoves = new ArrayList<>();
        for (int stemDirection = -2; stemDirection < 5; stemDirection += 4) {
            for (int hookDirection = -1; hookDirection < 2; hookDirection += 2) {
                for (int i = 0; i < 2; i++) {
                    Position currentPosition = new Position(position);
                    if (i == 1) {
                        currentPosition.add(new Position(stemDirection, hookDirection));
                    } else {
                        currentPosition.add(new Position(hookDirection, stemDirection));
                    }
                    Piece piece = board.getPieceFromPosition(currentPosition);
                    if (currentPosition.isOutOfBounds() || (piece != null && piece.getColour() == colour)) {
                        continue;
                    }
                    possibleMoves.add(new Position(currentPosition));
                }
            }
        }

        return possibleMoves;
    }
}
