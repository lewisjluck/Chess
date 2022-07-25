package com.chess.Chess.model.pieces;

import com.chess.Chess.model.*;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {
    private boolean hasMoved;
    private String side;

    public Rook(Colour colour, String side) {
        super(colour);
        hasMoved = false;
        this.side = side;
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public List<Position> getPossibleMoves(Board board, Position position, Colour colour) {
        List<Position> directions = new ArrayList<>(straightDirections);

        return getMovesFromDirection(directions, board, position, colour);
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public String getSide() {
        return side;
    }

    public void setHasMoved(boolean set) {
        hasMoved = set;
    }
}
