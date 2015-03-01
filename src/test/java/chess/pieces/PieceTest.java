package chess.pieces;

import chess.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jhinrichs on 2/24/15.
 */
public class PieceTest {

    @Test
    public void testGetOpponent() {
        Piece blackPiece = new Pawn(Player.Black);
        assertEquals(Player.White, blackPiece.getOpponent());

        Piece whitePiece = new Pawn(Player.White);
        assertEquals(Player.Black, whitePiece.getOpponent());

    }
}
