package com.chess.Chess.model;

import com.chess.Chess.controller.MoveResponse;
import com.chess.Chess.model.pieces.*;

import java.util.*;

public class Board {
    private Player player1;
    private Player player2;
    private Map<Position, Piece> board;
    private List<Piece> takenPieces;
    private Position passantAttackPosition;
    private Position passantPawnPosition;
    private int passantTurn;
    private Position castleRookTo;
    private Position castleRookFrom;
    private Position castleKingTo;
    private HashMap<Colour, Position> kingPositions;
    private Map<Colour, Map<String, Boolean>> movedRooks;
    private int fullMoveCount;

    public Board() {
        player1 = new Player("TestPlayer1", Colour.BLACK);
        player2 = new Player("TestPlayer2", Colour.WHITE);
        takenPieces = new ArrayList<>();
        passantTurn = 1;
        passantAttackPosition = null;
        passantPawnPosition = null;
        castleRookTo = null;
        castleRookFrom = null;
        castleKingTo = null;
        kingPositions = new HashMap<>();
        kingPositions.put(Colour.BLACK, new Position(0, 4));
        kingPositions.put(Colour.WHITE, new Position(7, 4));
        movedRooks = new HashMap<>();
        movedRooks.put(Colour.WHITE, new HashMap<>());
        movedRooks.put(Colour.BLACK, new HashMap<>());
        fullMoveCount = 0;
        initialiseNewBoard();
    }

    public Position getPassantAttackPosition() {
        return passantAttackPosition;
    }

    public Position getPassantPawnPosition() {
        return passantPawnPosition;
    }

    public void setCastleRookTo(Position castleRookTo) {
        this.castleRookTo = castleRookTo;
    }

    public void setCastleRookFrom(Position castleRookFrom) {
        this.castleRookFrom = castleRookFrom;
    }

    public void setCastleKingTo(Position castleKingTo) {
        this.castleKingTo = castleKingTo;
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
        board.put(new Position(row,0), new Rook(colour, "Q"));
        board.put(new Position(row,1), new Knight(colour));
        board.put(new Position(row,2), new Bishop(colour));
        board.put(new Position(row,3), new Queen(colour));
        board.put(new Position(row,4), new King(colour));
        board.put(new Position(row,5), new Bishop(colour));
        board.put(new Position(row,6), new Knight(colour));
        board.put(new Position(row,7), new Rook(colour, "K"));
        movedRooks.get(colour).put("K", false);
        movedRooks.get(colour).put("Q", false);
    }

    public static Position getForwardDirection(Colour colour) {
        return colour == Colour.WHITE ? new Position(-1, 0) : new Position(1, 0);
    }

    public Piece getPieceFromPosition(Position position) {
        return board.get(position);
    }

    public void promote (Position position, Colour colour) {
        /// somehow get promotion selection
        Piece selectedPiece = new Queen(colour);
        board.put(position, selectedPiece);
    }

    public MoveResponse move(Position from, Position to, Colour colour) {
        Piece pieceToMove = getPieceFromPosition(from);
        if (pieceToMove == null) {
            return null;
        }

        MoveResponse moveResponse = new MoveResponse();

        if (pieceToMove.getPossibleMoves(this, from, colour).contains(to)) {
            if (colour == Colour.BLACK) {
                fullMoveCount += 1;
            }
            if (pieceToMove instanceof Pawn) {
                if (Math.abs(to.getRow() - from.getRow()) == 2 && from.isInFirstRow(colour)) {
                    passantPawnPosition = to;
                    passantAttackPosition = new Position(from);
                    passantAttackPosition.add(getForwardDirection(colour));
                    passantTurn = 2;
                }

                if (to.isInLastRow(colour)) {
                    promote(to, colour);
                }
            } else if (pieceToMove instanceof King) {
                ((King) pieceToMove).setHasMoved(true);
                kingPositions.put(pieceToMove.getColour(), to);

            } else if (pieceToMove instanceof Rook) {
                ((Rook) pieceToMove).setHasMoved(true);
                movedRooks.get(colour).put(((Rook) pieceToMove).getSide(), true);
            }

            board.remove(from);

            if (getPieceFromPosition(to) != null) {
                takenPieces.add(getPieceFromPosition(to));
            }

            if (to.equals(passantAttackPosition)) {
                board.remove(passantPawnPosition);
                moveResponse.addTile(passantPawnPosition, null);
            }

            if (to.equals(castleKingTo)) {
                Piece pieceToCastle = getPieceFromPosition(castleRookFrom);
                board.remove(castleRookFrom);
                board.put(castleRookTo, pieceToCastle);
                moveResponse.addTile(castleRookFrom, null);
                moveResponse.addTile(castleRookTo, pieceToCastle);
            }

            board.put(to, pieceToMove);

            passantTurn -- ;
            if (passantTurn == 0) {
                passantPawnPosition = null;
                passantAttackPosition = null;
            }

            // CHECK CHECKMATE OF OTHER PLAYER
        }
        // moveResponse.gameOver();

        return moveResponse;
    }

    public List<Position> getMoves(Position position, Colour colour) {
        Piece piece = getPieceFromPosition(position);
        if (piece == null || piece.getColour() != colour) {
            return new ArrayList<>();
        }
        List<Position> moves = piece.getPossibleMoves(this, position, colour);

        List<Position> validMoves = new ArrayList<>();
        for (Position to : moves) {
            if (!isMoveInCheck(position, to, colour)) {
                validMoves.add(to);
            }
        }

        return validMoves;
    }

    private boolean isMoveInCheck(Position from, Position to, Colour colour) {
        Piece pieceToMove = board.remove(from);
        Piece pieceTaken = board.put(to, pieceToMove);
        if (pieceToMove instanceof King) {
            kingPositions.put(pieceToMove.getColour(), to);
        }

        boolean inCheck = squareInCheck(kingPositions.get(colour), colour);

        board.remove(to);
        board.put(from, pieceToMove);
        board.put(to, pieceTaken);
        if (pieceToMove instanceof King) {
            kingPositions.put(pieceToMove.getColour(), from);
        }

        return inCheck;
    }

    public boolean squareInCheck(Position checkPosition, Colour colour) {
        for (Map.Entry<Position, Piece> square : board.entrySet()) {
            Piece piece = square.getValue();
            Position position = square.getKey();

            if (piece == null || colour == piece.getColour()) {
                continue;
            }

            for (Position move : piece.getPossibleMoves(this, position, colour.getOther())) {
                if (move.equals(checkPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getFENPiecePlacement() {
        StringJoiner builder = new StringJoiner("");
        for (int row = 0; row < 8; row++) {
            int emptyCount = 0;
            for (int col = 0; col < 8; col++) {
                if (getPieceFromPosition(new Position(row, col)) == null) {
                    emptyCount++;
                } else {
                    Piece piece = getPieceFromPosition(new Position(row, col));
                    if (emptyCount != 0) {
                        builder.add(Integer.toString(emptyCount));
                        emptyCount = 0;
                    }
                    builder.add(piece.getFENSymbol());
                }
            }
            if (emptyCount != 0) {
                builder.add(Integer.toString(emptyCount));
            }
            builder.add("/");
        }
        return builder.toString();
    }

    public String getCastlingRights() {
        StringBuilder builder = new StringBuilder();
        for (Colour colour : Colour.values()) {
            if (((King) getPieceFromPosition(kingPositions.get(colour))).getHasMoved()) {
                continue;
            }
            for (Map.Entry<String, Boolean> entry : movedRooks.get(colour).entrySet()) {

                if (!entry.getValue()) {
                    builder.append(colour == Colour.WHITE ? entry.getKey().toUpperCase(Locale.ROOT) :
                            entry.getKey().toLowerCase(Locale.ROOT));
                }
            }

        }
        return builder.toString() == "" ? "-" : builder.toString();
    }

    public String getFEN(String activeColour) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(getFENPiecePlacement());
        joiner.add(activeColour.substring(0, 1));
        joiner.add(getCastlingRights());
        joiner.add(passantAttackPosition == null ? "-" : passantAttackPosition.toString());
        joiner.add("0");
        joiner.add(Integer.toString(fullMoveCount));
        return joiner.toString();
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
