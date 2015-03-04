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

    public Player getOpponent() {
        return getOwner() == Player.White ? Player.Black : Player.White;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    protected abstract char getIdentifyingCharacter();

    /**
     * Iterates through each of a Piece's movement possibilities and finds which are valid
     *
     * @param positionPieceMap
     * @param movements - a 2D array holding a set of potential movements
     * @return Set of strings of the form:  "currentPosition possiblePosition"
     */
    protected Set<String> getPossibleRepetitiveMoves(Map<Position, Piece> positionPieceMap, int[][] movements) {
        MOVEMENTS = movements;
        positionToPieceMap = positionPieceMap;
        possibleMoves = Sets.newHashSet();

        boolean shouldContinue;
        int step;
        for (int[] movement : MOVEMENTS) {
            step = 1;
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
     * Adds a requested movement to the set of possible moves if and only if it's a valid move
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

        // add the new Position if there is no Piece there and return that there could be more possible moves
        if (pieceAtPosition == null) {
            addPossibleMove(newPosition);
            return true;
        }

        // add the new position if there is an enemy there
        if (pieceAtPosition != null && pieceAtPosition.getOwner() != getOwner()) {
            addPossibleMove(newPosition);
        }

        // return that there are no more possible moves if an ally or enemy was in the position
        return false;
    }

    /**
     * Given a new position to add to the possible moves set, this method forms the string representing the move
     * and adds it to the set (ex: "c2 c4")
     * @param newPosition
     */
    private void addPossibleMove(Position newPosition) {
        possibleMoves.add(getPosition().toString() + " " + newPosition.toString());
    }

    /**
     * Given a new position to add to the possible moves set, this method forms the string representing the move
     * and adds it to the set (ex: "c2 c4")
     * @param newPosition
     * @param possibleMoves
     * @return
     */
    protected Set<String> addPossibleMove(Position newPosition, Set<String> possibleMoves) {
        possibleMoves.add(getPosition().toString() + " " + newPosition.toString());
        return possibleMoves;
    }


    /**
     * Method exists only to be overridden
     */
    public Set<String> getPossibleMoves(Map<Position, Piece> map) {
        return new HashSet();
    }
}
