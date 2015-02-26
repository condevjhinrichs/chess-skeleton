package chess.pieces;

import chess.Player;
import chess.Position;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A base class for chess pieces
 */
public abstract class Piece {
    private final Player owner;
    private Position position;

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

    public Set<String> getPossibleMoves(Map<Position, Piece> map) {
        return new HashSet<String>();
    }

    protected abstract char getIdentifyingCharacter();
}
