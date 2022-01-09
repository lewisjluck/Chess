package com.chess.Chess.model;

import java.util.Objects;

public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position(Position position) {
        this.row = position.getRow();
        this.column = position.getColumn();
    }

    public static Position getPositionFromString(String position) {
        String[] positions = position.split("-");

        int row;
        try {
            row = Integer.parseInt(positions[0]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }

        int col;
        try {
            col = Integer.parseInt(positions[1]);
        } catch (NumberFormatException e) {
            System.out.println(e);
            return null;
        }

        return new Position(row, col);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public void add(Position dPos) {
        this.row += dPos.row;
        this.column += dPos.column;
    }

    public boolean isInFirstRow(Colour colour) {
        return (colour == Colour.WHITE && row == 6) || (colour == Colour.BLACK && row == 1);
    }

    public boolean isInLastRow(Colour colour) {
        return (colour == Colour.WHITE && row == 0) || (colour == Colour.BLACK && row == 7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        char col = (char) (column + 97);
        return "(" + col + "," + (8 - row) + ")";
    }
}
