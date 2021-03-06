package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    private boolean canTakePassant;
    public Pawn(Colour colour) {
        super(colour);
        canTakePassant = false;
    }

    @Override
    public String getSymbol() {
        return "P";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour, boolean test) {
        List<Position> possibleMoves = new ArrayList<>();

        Position currentPosition = new Position(position);

        Position direction = Board.getForwardDirection(colour);
        List<Position> attackDirections = new ArrayList<>();
        if (colour == Colour.WHITE) {
            attackDirections.add(new Position(-1, 1));
            attackDirections.add(new Position(-1, -1));
        } else {
            attackDirections.add(new Position(1, 1));
            attackDirections.add(new Position(1, -1));
        }

        int moves = canDouble(position, colour) ? 2 : 1;

        for (int i = 0; i < moves; i++) {
            currentPosition.add(direction);
            Piece piece = board.getPieceFromPosition(currentPosition);
            if (currentPosition.isOutOfBounds() || piece != null) {
                break;
            }

            possibleMoves.add(new Position(currentPosition));
        }

        for (Position attackDirection : attackDirections) {
            Position attackPosition = new Position(position);
            attackPosition.add(attackDirection);
            Piece attackPiece = board.getPieceFromPosition(attackPosition);
            if (attackPiece != null && attackPiece.getColour() != colour) {
                possibleMoves.add(new Position(attackPosition));
            } else if (board.getPassantPawnPosition() != null
                    && board.getPieceFromPosition(board.getPassantPawnPosition()).getColour() != colour
                    && board.getPieceFromPosition(board.getPassantAttackPosition()) == null
                    && attackPosition.equals(board.getPassantAttackPosition())) {
                possibleMoves.add(new Position(attackPosition));
            }
        }

        for (Position passantDirection : Piece.horizontalDirections) {
            Position passantPosition = new Position(position);
            passantPosition.add(passantDirection);

            if (board.getPieceFromPosition(passantPosition) instanceof Pawn &&
                    ((Pawn) board.getPieceFromPosition(passantPosition)).canPassant()) {
                passantPosition.add(direction);
                possibleMoves.add(passantPosition);
            }
        }

        return possibleMoves;
    }

    public void setCanTakePassant(boolean canTakePassant) {
        this.canTakePassant = canTakePassant;
    }

    public boolean canPassant() {
        return canTakePassant;
    }

    private boolean canDouble(Position position, Colour colour) {
        return (position.isInFirstRow(colour));
    }
}
