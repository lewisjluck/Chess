package com.chess.Chess.model;

public enum Colour {
    WHITE, BLACK;

    public Colour getOther() {
        return this == WHITE ? BLACK : WHITE;
    }
}
