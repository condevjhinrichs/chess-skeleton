package chess.pieces;

import chess.Player;
import chess.Position;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * The 'Bishop' class
 */
public class Bishop extends Piece {
    public Bishop(Player owner) {
        super(owner);
    }

    private Map<Position, Piece> positionToPieceMap;
    private Set<String> possibleMoves;
    private int MOVEMENTS[][]={{1, 1}, {1, -1}, {-1, -1}, {-1, 1}};

    @Override
    protected char getIdentifyingCharacter() {
        return 'b';
    }


    /**
     * Gets the set of the piece's valid possible moves given the current state of the board
     *
     *
     * @param positionPieceMap
     * @return Set of Strings of the form:  "currentPosition possiblePosition"
     */

    @Override
    public Set<String> getPossibleMoves(Map<Position, Piece> positionPieceMap) {
        positionToPieceMap = positionPieceMap;
        possibleMoves = Sets.newHashSet();

        boolean shouldContinue;
        for (int[] movement : MOVEMENTS) {
            int counter = 1;
            shouldContinue = true;
            while(shouldContinue) {
                shouldContinue = addMoveIfValid(movement[0] * counter, movement[1] * counter);
                counter++;
            }
        }


        return possibleMoves;
    }

    /**
     * Adds a requested movement to the Set of possibleMoves if and only if it's a valid move
     * @param colMovement
     * @param rowMovement
     * @return boolean- whether there are more possible moves in the current line of movement
     */
    private boolean addMoveIfValid(int colMovement, int rowMovement) {
        // return if the requested movement is not on the board
        if (!getPosition().isOnBoard(colMovement, rowMovement)) {
            return false;
        }

        // get the new Position of the requested movement
        char newColumn = getPosition().moveColumn(colMovement);
        int newRow = getPosition().moveRow(rowMovement);
        Position newPosition = new Position(newColumn, newRow);

        // get the Piece that is possibly already in that new Position
        Piece pieceAtPosition = positionToPieceMap.get(newPosition);

        // add the new position if there is no piece there and return that there could be more possible moves
        if (pieceAtPosition == null) {
            addPossibleMove(newPosition);
            return true;
        }

        // add the new position if there is an opponent there and return that there are no more possible moves
        if (pieceAtPosition != null && pieceAtPosition.getOwner() != getOwner()) {
            addPossibleMove(newPosition);
            return false;
        }

        // remaining case- ally is in the new position.  Don't add and return that there are no more possible moves
        return false;
    }

    private void addPossibleMove(Position newPosition) {
        possibleMoves.add(getPosition().toString() + " " + newPosition.toString());
    }
}
