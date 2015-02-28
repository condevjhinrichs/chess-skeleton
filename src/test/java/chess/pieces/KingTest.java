package chess.pieces;

import chess.GameState;
import chess.Player;
import chess.Position;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by jhinrichs on 2/28/15.
 */
public class KingTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private King king;
    private String testPosition;
    private Set<String> resultMoves;

    @Before
    public void setup() {
        gameState = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testGetPossibleMovesGameInit() {
        testPosition = "e8";
        gameState.reset();
        king = (King) gameState.getPieceAt(testPosition);
        resultMoves = king.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesFromCenter() {
        testPosition = "d5";
        board.put(new King(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        king = (King) gameState.getPieceAt(testPosition);
        resultMoves = king.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(8, resultMoves.size());
        assertTrue(resultMoves.contains("d5 d4"));
        assertTrue(resultMoves.contains("d5 e4"));
        assertTrue(resultMoves.contains("d5 e5"));
        assertTrue(resultMoves.contains("d5 e6"));
        assertTrue(resultMoves.contains("d5 d6"));
        assertTrue(resultMoves.contains("d5 c6"));
        assertTrue(resultMoves.contains("d5 c5"));
        assertTrue(resultMoves.contains("d5 c4"));
    }

    @Test
    public void testGetPossibleMovesWithTraffic() {
        testPosition = "d1";
        board.put(new King(Player.White), new Position(testPosition));

        board.put(new Knight(Player.Black), new Position("c1"));
        board.put(new Pawn(Player.White), new Position("e2"));
        gameState.setFakeBoard(board);

        king = (King) gameState.getPieceAt(testPosition);
        resultMoves = king.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(4, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesGoingIntoCheck() {
        testPosition = "d1";
        board.put(new King(Player.White), new Position(testPosition));

        board.put(new Rook(Player.Black), new Position("c5"));
        board.put(new Pawn(Player.White), new Position("e2"));
        gameState.setFakeBoard(board);

        king = (King) gameState.getPieceAt(testPosition);
        resultMoves = king.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
        assertTrue(resultMoves.contains("d1 d2"));
        assertTrue(resultMoves.contains("d1 e1"));
    }

    @Test
    public void testGetPossibleMovesInCheck() {
        testPosition = "d1";
        board.put(new King(Player.White), new Position(testPosition));

        board.put(new Queen(Player.Black), new Position("c1"));
        board.put(new Bishop(Player.Black), new Position("d2"));
        gameState.setFakeBoard(board);

        king = (King) gameState.getPieceAt(testPosition);
        resultMoves = king.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(1, resultMoves.size());
        assertTrue(resultMoves.contains("d1 e2"));
    }
}
