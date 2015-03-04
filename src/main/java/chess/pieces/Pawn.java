package chess.pieces;

import chess.Player;
import chess.Position;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

/**
 * The Pawn
 */
public class Pawn extends Piece {

    private Map<Position, Piece> positionToPieceMap;
    private Set<String> possibleMoves;
    private final int MOVEMENTS[][] = { {0, 1}, {0, 2}, {-1, 1}, {1, 1} };

    public Pawn(Player owner) {
        super(owner);
    }

    @Override
    protected char getIdentifyingCharacter() {
        return 'p';
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
        // return if the pawn is trying to move two ahead but is not in the home row
        if (rowMovement == 2 && !pieceIsInHomeRow()) {
            return;
        }

        // since our board is using absolute coordinates and direction matters for pawns,
        // the Black player's movement needs to flip in direction
        if (getOwner() == Player.Black) {
            colMovement = -colMovement;
            rowMovement = -rowMovement;
        }

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

        // add the new position if there is no piece there and the movement is straight ahead
        if (pieceAtPosition == null && colMovement == 0) {
            possibleMoves = addPossibleMove(newPosition, possibleMoves);
            return;
        }

        // add the new position if the movement is to the diagonal where there is an opponent
        if (pieceAtPosition != null && pieceAtPosition.getOwner() != getOwner() && colMovement != 0) {
            possibleMoves = addPossibleMove(newPosition, possibleMoves);
        }
    }

    /**
     * Determines whether a Black or White Pawn is in its initial position
     *
     * @return boolean - if the Pawn is in its initial position
     */
    private boolean pieceIsInHomeRow() {
        boolean whiteInHomeRow = (getOwner() == Player.White) && (getPosition().getRow() == 2);
        boolean blackInHomeRow = (getOwner() == Player.Black) && (getPosition().getRow() == 7);

        return whiteInHomeRow || blackInHomeRow;
    }
}
