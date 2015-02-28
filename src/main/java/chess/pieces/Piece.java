package chess.pieces;

import chess.Player;
import chess.Position;
import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A base class for chess pieces
 */
public abstract class Piece {
    private final Player owner;
    private Position position;

    private Map<Position, Piece> positionToPieceMap;
    private Set<String> possibleMoves;
    private int[][] MOVEMENTS;

    protected Piece(Player owner) {
        this.owner = owner;
    }

    public char getIdentifier() {
        char id = getIdentifyingCharacter();
        if (owner.equals(Player.White)) {
            return Character.toLowerCase(id);
        } else {
            return Character.toUpperCase(id);
        }
    }

    public Player getOwner() {
        return owner;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected abstract char getIdentifyingCharacter();

    /**
     * Method called by a Piece subclass to pass the current state of the board and its unique set of movements
     *
     * @param positionPieceMap
     * @param movements - a 2D array holding the subclass's set of potential movements
     * @return Set of Strings of the form:  "currentPosition possiblePosition"
     */
    public Set<String> getPossibleMoves(Map<Position, Piece> positionPieceMap, int[][] movements) {
        MOVEMENTS = movements;
        positionToPieceMap = positionPieceMap;
        return getPossibleMoves();
    }

    /**
     * Iterate through each of a Piece's movement possibilities, calling addMoveIfValid() for each one
     *
     * @return Set of Strings of the form:  "currentPosition possiblePosition"
     */
    private Set<String> getPossibleMoves() {
        possibleMoves = Sets.newHashSet();

        boolean shouldContinue;
        for (int[] movement : MOVEMENTS) {
            int step = 1;
            shouldContinue = true;
            while(shouldContinue) {
                // multiplying the row and column movements by 'step' simulates stepping along a movement direction
                shouldContinue = addMoveIfValid(movement[0] * step, movement[1] * step);
                step++;
            }
        }
        return possibleMoves;
    }

    /**
     * Adds a requested movement to the Set of possibleMoves if and only if it's a valid move
     *
     * @param colMovement
     * @param rowMovement
     * @return boolean- whether there are more possible moves in the current direction of movement
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

    /**
     * Given a newPosition to add to the possible moves set, this method forms the actual String representing the move
     * and adds it to the set
     * Ex: "c2 c4"
     *
     * @param newPosition
     */
    private void addPossibleMove(Position newPosition) {
        possibleMoves.add(getPosition().toString() + " " + newPosition.toString());
    }

    /**
     * Method exists only to be overridden
     */
    public Set<String> getPossibleMoves(Map<Position, Piece> map) {
        return new HashSet();
    }
}
