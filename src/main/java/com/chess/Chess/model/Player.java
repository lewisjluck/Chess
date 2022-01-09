package com.chess.Chess.model;

public class Player {
    private String username;
    private Colour colour;

    public Player(String username, Colour colour) {
        this.username = username;
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }
}
