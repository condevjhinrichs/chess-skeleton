package chess.pieces;

import chess.Player;
import chess.PlayerFilter;
import chess.Position;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The King class
 */
public class King extends Piece {

    private Map<Position, Piece> positionToPieceMap;
    private Set<String> possibleMoves;
    private final int MOVEMENTS[][] = { {0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, -1}, {-1, 1} };

    public King(Player owner) {
        super(owner);
    }

    @Override
    protected char getIdentifyingCharacter() {
        return 'k';
    }

    /**
     * Gets the set of the king's valid possible moves given the current state of the board
     * @param positionPieceMap
     * @return Set of Strings of the form:  "a2 a4"
     */
    @Override
    public Set<String> getPossibleMoves(Map<Position, Piece> positionPieceMap) {
        return getPossibleMovesInContext(positionPieceMap, true);
    }

    /**
     * Gets the set of a king's possible moves including those that would put the king in check.

     * This would be used in determining the current player's enemy's possible moves,
     * in which case the opponent king putting itself in check to capture the other king would be a valid move.
     * @param positionPieceMap
     * @return Set of possible move Strings
     */
    public Set<String> getPossibleMovesIgnoringCheck(Map<Position, Piece> positionPieceMap) {
        return getPossibleMovesInContext(positionPieceMap, false);
    }

    /**
     * Gets the set of the king's valid possible moves for the given board,
     *
     * @param positionPieceMap
     * @param testCheck- if false, don't check if the king would be in check in determining whether the move is valid
     * @return Set of Strings of the form:  "a2 a4"
     */
    private Set<String> getPossibleMovesInContext(Map<Position, Piece> positionPieceMap, boolean testCheck) {
        positionToPieceMap = positionPieceMap;
        possibleMoves = Sets.newHashSet();

        for (int[] movement : MOVEMENTS) {
            addMoveIfValid(movement[0], movement[1], testCheck);
        }

        return possibleMoves;
    }

    /**
     * Adds a requested movement to the Set of possibleMoves if and only if it's a valid move
     *
     * @param colMovement
     * @param rowMovement
     */
    private void addMoveIfValid(int colMovement, int rowMovement, boolean testCheck) {
        // return if the requested movement is not on the board
        if (!getPosition().isOnBoard(colMovement, rowMovement)) {
            return;
        }

        // get the Position of the requested movement
        char newColumn = getPosition().moveColumn(colMovement);
        int newRow = getPosition().moveRow(rowMovement);
        Position newPosition = new Position(newColumn, newRow);

        // get the Piece that is possibly already in that new Position
        Piece pieceAtPosition = positionToPieceMap.get(newPosition);

        // return if there's an ally in the Position
        if (pieceAtPosition != null && pieceAtPosition.getOwner() == getOwner()) {
            return;
        }

        // return if moving to the new Position would put the king in check
        // testCheck is false when we're getting possible moves of the opponent king-
        // when we don't care if that king would be putting itself in check
        if (testCheck && wouldBeInCheck(newPosition)) {
            return;
        }

        // add the Position if there's an enemy there or if it's empty
        possibleMoves = addPossibleMove(newPosition, possibleMoves);
    }

    /**
     * Determines if moving to the given position would put the king in check
     *
     * @param kingPosition
     * @return
     */
    private boolean wouldBeInCheck(Position kingPosition) {
        Set<Position> enemyMovePositions = getHypotheticalEnemyMoves(kingPosition);
        return enemyMovePositions.contains(kingPosition);
    }

    /**
     * Gets the set of possible moves of all enemies with the king in the test Position
     *
     * @param testPosition-
     * @return
     */
    private Set<Position> getHypotheticalEnemyMoves(Position testPosition) {
        Player enemy = getOpponent();
        Piece currentPlayerKing = new King(getOwner());
        Set<Position> possibleEnemyMoves = new HashSet<Position>();
        Map<Position, Piece> kingInTestPositionMap;

        // get the enemy's pieces
        Map<Position, Piece> enemyPieces = Maps.filterEntries(positionToPieceMap, new PlayerFilter(enemy));

        // with the current player King in the test position,
        // get the Position for each move of each enemy Piece's possible moves
        for(Map.Entry<Position, Piece> positionPiece : enemyPieces.entrySet()) {
            Piece piece = positionPiece.getValue();

            // put the king in the position under test
            kingInTestPositionMap = Maps.newHashMap(positionToPieceMap);
            kingInTestPositionMap.put(testPosition, currentPlayerKing);
            kingInTestPositionMap.remove(getPosition());
            currentPlayerKing.setPosition(testPosition);

            Set<String> pieceMoves;
            if (piece.getClass() == King.class) {
                King king = (King) piece;
                pieceMoves = king.getPossibleMovesIgnoringCheck(kingInTestPositionMap);
            } else {
                pieceMoves = piece.getPossibleMoves(kingInTestPositionMap);
            }

            // add the possible moves for the current piece to the final possibleEnemyMoves
            for(String move : pieceMoves) {
                // a move is of the form "a2 d4"- "initial final"- and we want the 'final' Position
                possibleEnemyMoves.add(new Position(move.substring(3, 5)));
            }
        }
        return possibleEnemyMoves;
    }
}
