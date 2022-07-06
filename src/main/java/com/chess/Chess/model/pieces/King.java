package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    private boolean hasMoved;

    public King(Colour colour) {
        super(colour);
        hasMoved = false;
    }

    public void setHasMoved(boolean set) {
        hasMoved = set;
    }

    @Override
    public String getSymbol() {
        return "C";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour) {
        List<Position> possibleMoves = new ArrayList<>();
        List<Position> directions = new ArrayList<>(straightDirections);
        directions.addAll(diagonalDirections);

        for (Position direction : directions) {
            Position currentPosition = new Position(position);
            currentPosition.add(direction);
            Piece piece = board.getPieceFromPosition(currentPosition);
            if (currentPosition.isOutOfBounds() || (piece != null && piece.getColour() == colour)) {
                continue;
            }
            possibleMoves.add(new Position(currentPosition));
        }

        // castling
        if (!hasMoved) {
            for (Position direction : horizontalDirections) {
                Position currentPosition = new Position(position);
                currentPosition.add(direction);
                Position castleRookTo = new Position(currentPosition);
                if (board.getPieceFromPosition(currentPosition) == null) {
                    currentPosition.add(direction);
                    if (board.getPieceFromPosition(currentPosition) == null) {
                        Position castlePosition = new Position(position);
                        castlePosition.add(direction);

                        boolean underAttack = false;

                        while (board.getPieceFromPosition(castlePosition) == null) {
                            if (castlePosition.isOutOfBounds()) {
                                break;
                            }

                            if (board.squareInCheck(castlePosition, colour)) {
                                underAttack = true;
                            }

                            castlePosition.add(direction);
                        }

                        if (board.getPieceFromPosition(castlePosition) instanceof Rook
                                && !((Rook) board.getPieceFromPosition(castlePosition)).hasMoved()
                                && !underAttack) {
                            possibleMoves.add(currentPosition);
                            board.setCastleRookFrom(castlePosition);
                            board.setCastleRookTo(castleRookTo);
                            board.setCastleKingTo(currentPosition);
                        }
                    }
                }
            }
        }


        return possibleMoves;
    }
}
