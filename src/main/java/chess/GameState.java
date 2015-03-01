package chess;


import chess.pieces.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Class that represents the current state of the game.  Basically, what pieces are in which positions on the
 * board.
 */
public class GameState {

    /**
     * The current player
     */
    private Player currentPlayer = Player.White;

    /**
     * A map of board positions to pieces at that position
     */
    private Map<Position, Piece> positionToPieceMap;

    /**
     * Create the game state.
     */
    public GameState() {
        positionToPieceMap = new HashMap<Position, Piece>();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public void switchCurrentPlayer() {
        Player newPlayer = getCurrentPlayer() == Player.White ? Player.Black : Player.White;
        setCurrentPlayer(newPlayer);
    }

    public Map<Position, Piece> getPositionToPieceMap() {
        return positionToPieceMap;
    }

    /**
     * Call to initialize the game state into the starting positions
     */
    public void reset() {
        // White Pieces
        placePiece(new Rook(Player.White), new Position("a1"));
        placePiece(new Knight(Player.White), new Position("b1"));
        placePiece(new Bishop(Player.White), new Position("c1"));
        placePiece(new Queen(Player.White), new Position("d1"));
        placePiece(new King(Player.White), new Position("e1"));
        placePiece(new Bishop(Player.White), new Position("f1"));
        placePiece(new Knight(Player.White), new Position("g1"));
        placePiece(new Rook(Player.White), new Position("h1"));
        placePiece(new Pawn(Player.White), new Position("a2"));
        placePiece(new Pawn(Player.White), new Position("b2"));
        placePiece(new Pawn(Player.White), new Position("c2"));
        placePiece(new Pawn(Player.White), new Position("d2"));
        placePiece(new Pawn(Player.White), new Position("e2"));
        placePiece(new Pawn(Player.White), new Position("f2"));
        placePiece(new Pawn(Player.White), new Position("g2"));
        placePiece(new Pawn(Player.White), new Position("h2"));

        // Black Pieces
        placePiece(new Rook(Player.Black), new Position("a8"));
        placePiece(new Knight(Player.Black), new Position("b8"));
        placePiece(new Bishop(Player.Black), new Position("c8"));
        placePiece(new Queen(Player.Black), new Position("d8"));
        placePiece(new King(Player.Black), new Position("e8"));
        placePiece(new Bishop(Player.Black), new Position("f8"));
        placePiece(new Knight(Player.Black), new Position("g8"));
        placePiece(new Rook(Player.Black), new Position("h8"));
        placePiece(new Pawn(Player.Black), new Position("a7"));
        placePiece(new Pawn(Player.Black), new Position("b7"));
        placePiece(new Pawn(Player.Black), new Position("c7"));
        placePiece(new Pawn(Player.Black), new Position("d7"));
        placePiece(new Pawn(Player.Black), new Position("e7"));
        placePiece(new Pawn(Player.Black), new Position("f7"));
        placePiece(new Pawn(Player.Black), new Position("g7"));
        placePiece(new Pawn(Player.Black), new Position("h7"));
    }

    /**
     * Provide a way to create a specific board scenario for testing purposes
     */
    @VisibleForTesting
    public void setFakeBoard(Map<Piece, Position> piecePositionMap) {
        for(Map.Entry<Piece, Position> piecePosition : piecePositionMap.entrySet()) {
            placePiece(piecePosition.getKey(), piecePosition.getValue());
        }
    }

    /**
     * Get all the possible moves for the current player
     * @return Set of Strings, each of which is "initial position final position"
     */
    public Set<String> getMovesList() {
        // get the current player's pieces
        Map<Position, Piece> currentPlayerPieces =
                Maps.filterEntries(positionToPieceMap, new PlayerPiece(getCurrentPlayer()));

        Set<String> possibleMoves = new HashSet<String>();
        // get the possible moves for each piece
        for(Map.Entry<Position, Piece> positionPiece : currentPlayerPieces.entrySet()) {
            Piece piece = positionPiece.getValue();

            // add the possible moves for the current piece to the final possibleMoves
            for(String move : piece.getPossibleMoves(positionToPieceMap)) {
                possibleMoves.add(move);
            }
        }
        // return the possible moves list
        return possibleMoves;
    }

    /**
     * Get the piece at the position specified by the String
     * @param colrow The string indication of position; i.e. "d5"
     * @return The piece at that position, or null if it does not exist.
     */
    public Piece getPieceAt(String colrow) {
        Position position = new Position(colrow);
        return getPieceAt(position);
    }

    /**
     * Removes a Piece from the board at the position given in String form
     * @param colrow String representing a board position- "c5"
     */
    private void removePieceAt(String colrow) {
        Position position = new Position(colrow);
        removePieceAt(position);
    }

    /**
     * Removes a Piece from the board at the given Position
     * @param position- Position object where the Piece will be removed
     */
    private void removePieceAt(Position position) {
        positionToPieceMap.remove(position);
    }

    /**
     * Get the piece at a given position on the board
     * @param position The position to inquire about.
     * @return The piece at that position, or null if it does not exist.
     */
    public Piece getPieceAt(Position position) {
        return positionToPieceMap.get(position);
    }

    /**
     * Method to place a piece at a given position
     * @param piece The piece to place
     * @param position The position
     */
    private void placePiece(Piece piece, Position position) {
        positionToPieceMap.put(position, piece);
        piece.setPosition(position);
    }

    /**
     * Updates the positionToPieceMap given a valid game move
     * @param move- String of the form "initialPosition finalPosition"
     * @return- boolean for whether or not the requested move was valid
     */
    public boolean makeMoveIfValid(String move) {
        // get the set of the current player's possible moves
        Set<String> possibleMoves = Sets.newHashSet(getMovesList());

        // if the set of possible moves doesn't contain the requested move- it's invalid input
        if (!possibleMoves.contains(move)) {
            return false;
        }

        String fromPosition = move.substring(0, 2);
        String toPosition = move.substring(3, 5);

        // remove the Piece from its original Position and place it in its new Position
        removePieceAt(fromPosition);
        placePiece(getPieceAt(fromPosition), new Position(toPosition));
        return true;
    }
}
