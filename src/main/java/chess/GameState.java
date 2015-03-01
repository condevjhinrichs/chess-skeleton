package chess;


import chess.pieces.*;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    private void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public void switchCurrentPlayer() {
        setCurrentPlayer(getOpponent());
    }

    public Player getOpponent() {
        return getCurrentPlayer() == Player.White ? Player.Black : Player.White;
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
     * Gets a sorted List of the current player's possible game moves
     * @return sorted List of Strings "a2 a4"
     */
    public List<String> getPossibleMovesList() {
        List<String> possibleMoves = Lists.newArrayList(getMovesList());
        Collections.sort(possibleMoves);
        return possibleMoves;
    }

    /**
     * Gets the Set of all possible moves for the current player
     * @return Set of Strings representing moves- "c2 d3"
     */
    public Set<String> getMovesList() {
        // if the current player is in check, return the possible moves of the king only
        if (isInCheck()) {
            return getCurrentPlayerKing().getPossibleMoves(positionToPieceMap);
        }

        // otherwise return all Pieces' possible moves
        return getPlayerMoves();
    }

    /**
     * Gets the current player's king
     * @return King
     */
    private King getCurrentPlayerKing() {
        King king = new King(getCurrentPlayer());
        for (Map.Entry<Position, Piece> positionPiece : positionToPieceMap.entrySet()) {
            if (positionPiece.getValue().getClass() == King.class &&
                    positionPiece.getValue().getOwner() == getCurrentPlayer()) {
                king = (King) positionPiece.getValue();
                break;
            }
        }
        return king;
    }

    /**
     * Determines whether the current player is in check
     * @return boolean
     */
    public boolean isInCheck() {
        // player is in check iff their king's position is in the set of positions of the opponent's possible moves
        String kingPosition = getCurrentPlayerKing().getPosition().toString();
        return getOpponentMovesPositionsOnly().contains(kingPosition);
    }

    /**
     * Finds the Set of all possible moves for the current player's pieces
     * @return Set of Strings representing all of the possible moves- "a2 c4"
     */
    private Set<String> getPlayerMoves() {
        Map<Position, Piece> currentPlayerPieces = Maps.filterEntries(positionToPieceMap, new PlayerFilter
                (getCurrentPlayer()));
        return getPlayerMoves(currentPlayerPieces, false);
    }

    /**
     * Finds the Set of positions of all the opponent's possible moves for the gameState
     * @return Set of Positions as Strings
     */
    private Set<String> getOpponentMovesPositionsOnly() {
        Map<Position, Piece> opponentPieces = Maps.filterEntries(positionToPieceMap, new PlayerFilter(getOpponent()));
        return getPlayerMoves(opponentPieces, true);
    }

    /**
     * Finds the Set of all moves or the Set of all positions a player's pieces can move to
     * @param playerPieces
     * @param testingCheck- boolean- false will give a Set of full move Strings- "a2 c4"
     *                                true will give a Set of only the "c4"
     * @return Set of Strings
     */
    private Set<String> getPlayerMoves(Map<Position, Piece> playerPieces, boolean testingCheck) {
        Set<String> possibleMoves = new HashSet<String>();

        for(Map.Entry<Position, Piece> positionPiece : playerPieces.entrySet()) {
            Set<String> pieceMoves = getPieceMovesInContext(positionPiece.getValue(), testingCheck);

            // add the possible moves or positions for the current piece to possibleMoves
            for(String move : pieceMoves) {
                if (testingCheck) {
                    move = move.substring(3, 5);
                }
                possibleMoves.add(move);
            }
        }
        // return the possible moves Set
        return possibleMoves;
    }

    /**
     * ignoreCheck true implies we're calling this method to get an opponent piece's moves for the purpose of
     * determining whether the game is in check
     * We're getting the opponent's moves to see if they can take the king,
     * so we want to get the opponent king's moves without filtering out moves that would be going into check
     * @param piece
     * @param ignoreCheck- boolean
     * @return
     */
    private Set<String> getPieceMovesInContext(Piece piece, boolean ignoreCheck) {
        Set<String> pieceMoves;

        if (ignoreCheck && piece.getClass() == King.class) {
            King king = (King) piece;
            pieceMoves = king.getPossibleMovesIgnoringCheck(positionToPieceMap);
        } else {
            pieceMoves = piece.getPossibleMoves(positionToPieceMap);
        }

        return pieceMoves;
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
     * Updates the positionToPieceMap given a valid game move
     * @param move- String of the form "initialPosition finalPosition"
     * @return- boolean for whether or not the requested move was valid
     */
    public boolean makeMoveIfValid(String move) {
        // get the set of the current player's possible moves
        Set<String> possibleMoves = getMovesList();

        // if the set of possible moves doesn't contain the requested move- it's invalid input
        if (!possibleMoves.contains(move)) {
            return false;
        }

        String fromPosition = move.substring(0, 2);
        String toPosition = move.substring(3, 5);

        // place the Piece in its new Position and remove it from the old one
        placePiece(getPieceAt(fromPosition), new Position(toPosition));
        removePieceAt(fromPosition);

        // on a successful move- switch the current player
        switchCurrentPlayer();
        return true;
    }
}
