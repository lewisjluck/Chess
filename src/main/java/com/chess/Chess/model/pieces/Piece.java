package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class Piece {
    private final Colour colour;
    public abstract List<Position> getPossibleMoves(Board board, Position position, Colour colour);
    public static final List<Position> straightDirections = new ArrayList<>(Arrays.asList(
            new Position(0, -1),
            new Position(-1, 0),
            new Position(0, 1),
            new Position(1, 0)
    ));

    public static final List<Position> diagonalDirections = new ArrayList<>(Arrays.asList(
            new Position(1, -1),
            new Position(-1, 1),
            new Position(1, 1),
            new Position(-1, -1)
    ));

    public static final List<Position> horizontalDirections = new ArrayList<>(Arrays.asList(
            new Position(0, -1),
            new Position(0, 1)
    ));

    public Piece(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return colour == piece.colour;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour);
    }

    public abstract String getSymbol();

    public List<Position> getMovesFromDirection(List<Position> directions, Board board, Position position, Colour colour) {
        List<Position> possibleMoves = new ArrayList<>();

        Position currentPosition = new Position(position);

        for (Position direction : directions) {
            currentPosition.add(direction);
            while (board.getPieceFromPosition(currentPosition) == null) {
                if (Board.isOutOfBounds(currentPosition)) {
                    break;
                }

                possibleMoves.add(new Position(currentPosition));
                currentPosition.add(direction);
            }

            Piece currentPiece = board.getPieceFromPosition(currentPosition);
            if (currentPiece != null
                    && currentPiece.getColour() != colour) {
                possibleMoves.add(new Position(currentPosition));
            }

            currentPosition = new Position(position);
        }

        return possibleMoves;
    }

    public String getDisplay() {
        return "pieces/" + colour.toString() + "_" + getClass().getSimpleName() + ".png";
    }
}


