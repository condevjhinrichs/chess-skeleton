package chess.pieces;

import chess.Player;
import chess.Position;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * The Knight class
 */
public class Knight extends Piece {

    private Map<Position, Piece> positionToPieceMap;
    private Set<String> possibleMoves;
    private final int MOVEMENTS[][] = { {2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2} };

    public Knight(Player owner) {
        super(owner);
    }

    @Override
    protected char getIdentifyingCharacter() {
        return 'n';
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

        // return if there's an ally in the position
        if (pieceAtPosition != null && pieceAtPosition.getOwner() == getOwner()) {
            return;
        }

        // add the position if there's an enemy there or if it's empty
        possibleMoves = addPossibleMove(newPosition, possibleMoves);
    }
}
