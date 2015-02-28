package chess;

import chess.pieces.Piece;
import com.google.common.base.Predicate;

import java.util.Map;

/**
 * Created by jhinrichs on 2/28/15.
 */


/**
 *  A Guava Predicate to extract the current player's pieces from the positionToPieceMap
 *
 *  Example found here:
 *  http://stackoverflow.com/questions/17330487/creating-subset-of-hashmap-based-on-some-specifications
 */
public class PlayerPiece implements Predicate<Map.Entry<Position, Piece>> {
    // the player whose entries we return
    private Player player;

    public PlayerPiece(Player player) {
        this.player = player;
    }

    @Override
    public boolean apply(Map.Entry<Position, Piece> input) {
        return input.getValue().getOwner().equals(player);
    }
}
