package com.chess.Chess.model;

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

    public Board() {
        player1 = new Player("TestPlayer1", Colour.BLACK);
        player2 = new Player("TestPlayer2", Colour.WHITE);
        initialiseNewBoard();
        takenPieces = new ArrayList<>();
        passantTurn = 1;
        passantAttackPosition = null;
        passantPawnPosition = null;
        castleRookTo = null;
        castleRookFrom = null;
        kingPositions.put(Colour.BLACK, new Position(0, 4));
        kingPositions.put(Colour.WHITE, new Position(7, 4));
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
2        List<List<String>> display = new ArrayList<>();

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

    public static Position getForwardDirection(Colour colour) {
        if (colour == Colour.WHITE) {
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

    public void promote (Position position, Colour colour) {
        /// somehow get promotion selection
        Piece selectedPiece = new Queen(colour);
        board.put(position, selectedPiece);
    }

    public HashMap<String, List<String>> move(Position from, Position to, Colour colour) {
        Piece pieceToMove = getPieceFromPosition(from);
        if (pieceToMove == null) {
            return null;
        }

        HashMap<String, List<String>> positionsToChange = new HashMap<>();
        positionsToChange.put("position", new ArrayList<>());
        positionsToChange.put("display", new ArrayList<>());

        if (pieceToMove.getPossibleMoves(this, from, colour).contains(to)) {
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
            }

            board.remove(from);

            // Isolate communication with front end into separate class
            if (getPieceFromPosition(to) != null) {
                takenPieces.add(getPieceFromPosition(to));
            }

            if (to.equals(passantAttackPosition)) {
                board.remove(passantPawnPosition);
                positionsToChange.get("position").add(passantPawnPosition.toString());
                positionsToChange.get("display").add("NONE");
            }

            if (to.equals(castleKingTo)) {
                Piece pieceToCastle = getPieceFromPosition(castleRookFrom);
                board.remove(castleRookFrom);
                board.put(castleRookTo, pieceToCastle);
                positionsToChange.get("position").add(castleRookFrom.toString());
                positionsToChange.get("display").add("NONE");
                positionsToChange.get("position").add(castleRookTo.toString());
                positionsToChange.get("display").add(pieceToCastle.getDisplay());
            }

            board.put(to, pieceToMove);

            passantTurn -- ;
            if (passantTurn == 0) {
                passantPawnPosition = null;
                passantAttackPosition = null;
            }

            // CHECK CHECKMATE OF OTHER PLAYER
        }

        return positionsToChange;
    }

    public List<Position> getMoves(Position position, Colour colour) {
        Piece piece = getPieceFromPosition(position);
        if (piece == null || piece.getColour() != colour) {
            return new ArrayList<>();
        }
        List<Position> moves = piece.getPossibleMoves(this, position, colour);

        if (squareInCheck(kingPositions.get(colour), colour)) {
            List<Position> validMoves = new ArrayList<>();
            for (Position to : moves) {
                if (doesMoveGetOutOfCheck(position, to, colour)) {
                    validMoves.add(to);
                }
            }
            moves = validMoves;
        }

        return moves;
    }

    private boolean doesMoveGetOutOfCheck(Position from, Position to, Colour colour) {
        Piece pieceToMove = board.remove(from);
        board.put(to, pieceToMove);
        boolean stillInCheck = squareInCheck(kingPositions.get(colour), colour);
        board.remove(to);
        board.put(from, pieceToMove);
        return stillInCheck;
    }

    public boolean squareInCheck(Position checkPosition, Colour colour) {
        for (Map.Entry<Position, Piece> square : board.entrySet()) {
            Piece piece = square.getValue();
            Position position = square.getKey();

            List<Position> moves;
            if (colour == piece.getColour()) {
                continue;
            } else {
                moves = getMoves(position, piece.getColour());
            }

            for (Position move : moves) {
                if (move.equals(checkPosition)) {
                    return true;
                }
            }
        }
        return false;
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
