package com.chess.Chess.model;

import com.chess.Chess.model.pieces.*;

import java.util.*;

public class Board {
    private Player player1;
    private Player player2;
    private Map<Position, Piece> board;
    private List<Piece> takenPieces;
    private static Position passantAttackPosition;
    private static Position passantPawnPosition;
    private static int passantTurn;

    public Board() {
        player1 = new Player("TestPlayer1", Colour.BLACK);
        player2 = new Player("TestPlayer2", Colour.WHITE);
        initialiseNewBoard();
        takenPieces = new ArrayList<>();
        passantTurn = 1;
        passantAttackPosition = null;
        passantPawnPosition = null;
    }

    public List<List<String>> getDisplay() {
        List<List<String>> display = new ArrayList<>();

        for (int row = 0; row < 8; row++) {
            List<String> rowDisplay = new ArrayList<>();
            for (int column = 0; column < 8; column++) {
                Piece piece = getPieceFromPosition(new Position(row, column));
                if (piece == null) {
                    rowDisplay.add(null);
                } else {
                    rowDisplay.add(piece.getDisplay());
                }
            }
            display.add(rowDisplay);
        }

        return display;
    }

    // initialise new board helper functions
    private void initialiseNewBoard() {
        board = new HashMap<>();

        // add pawns
        for (int i = 0; i < 8; i++) {
            // black pawns
            board.put(new Position(1, i), new Pawn(Colour.BLACK));
            
            // white pawns
            board.put(new Position(6, i), new Pawn(Colour.WHITE));
        }

        addBackPieces(Colour.BLACK, 0);
        addBackPieces(Colour.WHITE, 7);
    }
    private void addBackPieces(Colour colour, int row) {
        board.put(new Position(row,0), new Rook(colour));
        board.put(new Position(row,1), new Knight(colour));
        board.put(new Position(row,2), new Bishop(colour));
        board.put(new Position(row,3), new Queen(colour));
        board.put(new Position(row,4), new King(colour));
        board.put(new Position(row,5), new Bishop(colour));
        board.put(new Position(row,6), new Knight(colour));
        board.put(new Position(row,7), new Rook(colour));
    }

    public static Position getPlayerForwardDirection(Player player) {
        if (player.getColour() == Colour.WHITE) {
            return new Position(-1, 0);
        } else {
            return new Position(1, 0);
        }
    }

    public Piece getPieceFromPosition(Position position) {
        return board.get(position);
    }

    public static boolean isOutOfBounds(Position position) {
        return (position.getRow() > 7 || position.getRow() < 0 || position.getColumn() > 7 || position.getColumn() < 0);
    }

    public void promote (Position position, Player player) {
        /// somehow get promotion selection
        Piece selectedPiece = new Queen(player.getColour());
        board.put(position, selectedPiece);
    }

    public void move(Position from, Position to, Player player) {
        Piece pieceToMove = getPieceFromPosition(from);
        if (pieceToMove == null) {
            return;
        }

        if (pieceToMove.getPossibleMoves(this, from, player).contains(to)) {
            if (pieceToMove instanceof Pawn) {
                if (Math.abs(to.getRow() - from.getRow()) == 2 && from.isInFirstRow(player.getColour())) {
                    passantPawnPosition = to;
                    passantAttackPosition = new Position(from);
                    passantAttackPosition.add(getPlayerForwardDirection(player));
                    passantTurn = 2;
                }

                if (to.isInLastRow(player.getColour())) {
                    promote(to, player);
                }
            }

            board.remove(from);

            if (getPieceFromPosition(to) != null) {
                takenPieces.add(getPieceFromPosition(to));
            }

            if (to.equals(passantAttackPosition)) {
                board.remove(passantPawnPosition);
            }

            board.put(to, pieceToMove);

            passantTurn -- ;
            if (passantTurn == 0) {
                passantPawnPosition = null;
                passantAttackPosition = null;
            }
        }
    }

    public List<Position> getMoves(Position position, Player player) {
        Piece piece = getPieceFromPosition(position);
        if (piece == null || piece.getColour() != player.getColour()) {
            return new ArrayList<>();
        }
        return piece.getPossibleMoves(this, position, player);
    }

    @Override
    public String toString() {
        StringJoiner builder = new StringJoiner("");
        for (int row = 0; row < 8; row++) {
            builder.add("|");
            for (int col = 0; col < 8; col++) {
                if (getPieceFromPosition(new Position(row, col)) == null) {
                    builder.add(" |");
                } else {
                    Piece piece = getPieceFromPosition(new Position(row, col));
                    builder.add(piece.getSymbol() + "|");
                }
            }
            builder.add("\n------------------\n");
        }
        return builder.toString();
    }
}
