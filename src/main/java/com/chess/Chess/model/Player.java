package com.chess.Chess.model;

/**
 * Represents a user.
 */
public class Player {

    /** The user's username */
    private String username;

    /** The user's colour in their current game  THIS MAY NOT GO HERE */
    private Colour colour;

    /**
     * Initialises the player, setting specified username and colour
     *
     * @param username - the player's username
     * @param colour - the player's colour
     */
    public Player(String username, Colour colour) {
        this.username = username;
        this.colour = colour;
    }

    /**
     * Returns the users colour
     *
     * @return the user's colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the player's username
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }
}
