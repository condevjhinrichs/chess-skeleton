package chess;

import chess.pieces.Piece;
import com.google.common.base.Predicate;

import java.util.Map;

/**
 * Created by jhinrichs on 2/28/15.
 */


/**
 * This is a Predicate to override filterEntries for a Map<Position, Piece>.
 * It returns only those entries for which the Piece's owner (player) is that of the PlayerFilter's Player
 *
 *  Example found here:
 *  http://stackoverflow.com/questions/17330487/creating-subset-of-hashmap-based-on-some-specifications
 */
public class PlayerFilter implements Predicate<Map.Entry<Position, Piece>> {
    // the player whose entries we return
    private Player player;

    public PlayerFilter(Player player) {
        this.player = player;
    }

    @Override
    public boolean apply(Map.Entry<Position, Piece> input) {
        return input.getValue().getOwner().equals(player);
    }
}
