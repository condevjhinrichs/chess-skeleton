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
public class QueenTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private Queen queen;
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
        queen = (Queen) gameState.getPieceAt("d1");
        resultMoves = queen.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesFromCenter() {
        testPosition = "d5";
        board.put(new Queen(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        queen = (Queen) gameState.getPieceAt(testPosition);
        resultMoves = queen.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(27, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesWithTraffic() {
        testPosition = "d5";
        board.put(new Queen(Player.White), new Position(testPosition));

        board.put(new Queen(Player.Black), new Position("d4"));
        board.put(new Bishop(Player.Black), new Position("d8"));
        board.put(new Pawn(Player.White), new Position("c5"));
        board.put(new Knight(Player.White), new Position("h5"));
        board.put(new Pawn(Player.White), new Position("f7"));
        board.put(new Knight(Player.Black), new Position("g2"));
        gameState.setFakeBoard(board);

        queen = (Queen) gameState.getPieceAt(testPosition);
        resultMoves = queen.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(17, resultMoves.size());
    }
}
