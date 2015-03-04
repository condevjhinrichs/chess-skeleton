package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jhinrichs on 2/28/15.
 */
public class KnightTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private Knight knight;
    private String testPosition;
    private Set<String> resultMoves;

    @Before
    public void setup() {
        gameState = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testGetPossibleMovesGameInit() {
        gameState.reset();
        knight = (Knight) gameState.getPieceAt("g8");
        resultMoves = knight.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesFromCenter() {
        testPosition = "d5";
        board.put(new Knight(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        knight = (Knight) gameState.getPieceAt(testPosition);
        resultMoves = knight.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(8, resultMoves.size());
        assertTrue(resultMoves.contains("d5 b4"));
        assertTrue(resultMoves.contains("d5 b6"));
        assertTrue(resultMoves.contains("d5 c7"));
        assertTrue(resultMoves.contains("d5 e7"));
        assertTrue(resultMoves.contains("d5 f4"));
        assertTrue(resultMoves.contains("d5 f6"));
        assertTrue(resultMoves.contains("d5 c3"));
        assertTrue(resultMoves.contains("d5 e3"));
    }

    @Test
    public void testGetPossibleMovesWithTraffic() {
        testPosition = "d5";
        board.put(new Knight(Player.White), new Position(testPosition));

        board.put(new Knight(Player.Black), new Position("b6"));
        board.put(new Bishop(Player.Black), new Position("e7"));
        board.put(new Pawn(Player.White), new Position("f4"));
        board.put(new Queen(Player.White), new Position("f6"));
        board.put(new Pawn(Player.White), new Position("c3"));
        board.put(new Knight(Player.Black), new Position("e3"));
        gameState.setFakeBoard(board);

        knight = (Knight) gameState.getPieceAt(testPosition);
        resultMoves = knight.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(5, resultMoves.size());
    }
}
