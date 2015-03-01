package chess;

import chess.pieces.Piece;
import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jhinrichs on 3/1/15.
 */
public class PlayerFilterTest {

    @Test
    public void testFilterEntriesApply() {
        GameState gameState = new GameState();
        gameState.reset();
        Player testPlayer = gameState.getCurrentPlayer();
        Map<Position, Piece> originalMap = gameState.getPositionToPieceMap();
        PlayerFilter playerFilter = new PlayerFilter(testPlayer);
        Map<Position, Piece> playerPieces = Maps.filterEntries(originalMap, playerFilter);

        assertTrue(originalMap.entrySet().size() > playerPieces.entrySet().size());

        for(Map.Entry<Position, Piece> positionPiece : playerPieces.entrySet()) {
            Player pieceOwner = positionPiece.getValue().getOwner();
            assertEquals(pieceOwner, testPlayer);
        }
    }
}
