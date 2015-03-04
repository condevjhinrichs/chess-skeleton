package chess.pieces;

import chess.Player;
import chess.Position;

import java.util.Map;
import java.util.Set;

/**
 * The 'Rook' class
 */
public class Rook extends Piece {

    private final int MOVEMENTS[][] = { {0, 1}, {0, -1}, {1, 0}, {-1, 0} };

    public Rook(Player owner) {
        super(owner);
    }

    @Override
    protected char getIdentifyingCharacter() {
        return 'r';
    }

    /**
     * Gets the set of the piece's valid possible moves given the current state of the board
     *
     * @param positionToPieceMap
     * @return Set of Strings of the form:  "currentPosition possiblePosition"
     */
    @Override
    public Set<String> getPossibleMoves(Map<Position, Piece> positionToPieceMap) {
        return getPossibleRepetitiveMoves(positionToPieceMap, MOVEMENTS);
    }
}
