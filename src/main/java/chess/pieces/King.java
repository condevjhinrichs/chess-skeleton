package chess.pieces;

import chess.Player;
import chess.PlayerPiece;
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
     * Gets the set of the piece's valid possible moves given the current state of the board
     *
     * @param positionPieceMap
     * @return Set of Strings of the form:  "currentPosition possiblePosition"
     */
    @Override
    public Set<String> getPossibleMoves(Map<Position, Piece> positionPieceMap) {
        positionToPieceMap = positionPieceMap;
        possibleMoves = Sets.newHashSet();

        for (int[] movement : MOVEMENTS) {
            addMoveIfValid(movement[0], movement[1]);
        }

        return possibleMoves;
    }

    /**
     * Adds a requested movement to the Set of possibleMoves if and only if it's a valid move
     *
     * @param colMovement
     * @param rowMovement
     */
    private void addMoveIfValid(int colMovement, int rowMovement) {
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
        if (wouldBeInCheck(newPosition)) {
            return;
        }

        // add the Position if there's an enemy there or if it's empty
        addPossibleMove(newPosition);

    }

    /**
     * Given a new Position to add to the possible moves set, this method forms the String representing the move
     * and adds it to the set (ex: "c2 c4")
     *
     * @param newPosition
     */
    private void addPossibleMove(Position newPosition) {
        possibleMoves.add(getPosition().toString() + " " + newPosition.toString());
    }

    /**
     * Determines if moving to the given position would put the king in check
     *
     * @param kingPosition
     * @return
     */
    private boolean wouldBeInCheck(Position kingPosition) {
        Set<Position> enemyMovePositions = getEnemyMovePositions(kingPosition);
        return enemyMovePositions.contains(kingPosition);
    }

    /**
     * Gets the set of possible moves of all enemies with the king in the test Position
     *
     * @param testPosition-
     * @return
     */
    private Set<Position> getEnemyMovePositions(Position testPosition) {
        Player enemy = getOwner() == Player.White ? Player.Black : Player.White;
        Piece currentPlayerKing = new King(getOwner());
        Set<Position> possibleEnemyMoves = new HashSet<Position>();
        Map<Position, Piece> kingInTestPositionMap;

        // get the enemy's pieces
        Map<Position, Piece> enemyPieces = Maps.filterEntries(positionToPieceMap, new PlayerPiece(enemy));

        // with the current player King in the test position,
        // get the Position for each move of each enemy Piece's possible moves
        for(Map.Entry<Position, Piece> positionPiece : enemyPieces.entrySet()) {
            Piece piece = positionPiece.getValue();

            // put the king in the position under test
            kingInTestPositionMap = Maps.newHashMap(positionToPieceMap);
            kingInTestPositionMap.remove(testPosition);
            kingInTestPositionMap.put(testPosition, currentPlayerKing);

            // add the possible moves for the current piece to the final possibleEnemyMoves
            for(String move : piece.getPossibleMoves(kingInTestPositionMap)) {
                // a move is of the form "a2 d4"- "initial final"- and we want the 'final' Position
                possibleEnemyMoves.add(new Position(move.substring(3, 5)));
            }
        }
        return possibleEnemyMoves;
    }
}
