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

/**
 * Created by jhinrichs on 2/27/15.
 */
public class RookTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private Piece rook;
    private String testPosition;
    private Set<String> resultMoves;

    @Before
    public void setup() {
        gameState = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testGetPossibleMovesGameInit() {
        testPosition = "a1";
        gameState.reset();
        rook = gameState.getPieceAt(testPosition);
        resultMoves = rook.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesEdge() {
        testPosition = "c1";
        board.put(new Rook(Player.White), new Position(testPosition));
        gameState.setFakeBoard(board);
        rook = gameState.getPieceAt(testPosition);
        resultMoves = rook.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(14, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesFromCenter() {
        testPosition = "d5";
        board.put(new Rook(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        rook = gameState.getPieceAt(testPosition);
        resultMoves = rook.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(14, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesWithTraffic() {
        testPosition = "d5";
        board.put(new Rook(Player.White), new Position(testPosition));

        board.put(new Rook(Player.Black), new Position("d4"));
        board.put(new Bishop(Player.Black), new Position("d8"));
        board.put(new Pawn(Player.White), new Position("c5"));
        board.put(new Knight(Player.White), new Position("h5"));
        gameState.setFakeBoard(board);

        rook = gameState.getPieceAt(testPosition);
        resultMoves = rook.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(7, resultMoves.size());
    }
}
