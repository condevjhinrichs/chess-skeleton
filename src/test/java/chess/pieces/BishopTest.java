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
public class BishopTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private Bishop bishop;
    private String testPosition;
    private Set<String> resultMoves;

    @Before
    public void setup() {
        gameState = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testGetPossibleMovesGameInit() {
        testPosition = "c1";
        gameState.reset();
        bishop = (Bishop) gameState.getPieceAt(testPosition);
        resultMoves = bishop.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesCorner() {
        testPosition = "a8";
        board.put(new Bishop(Player.White), new Position(testPosition));
        gameState.setFakeBoard(board);
        bishop = (Bishop) gameState.getPieceAt(testPosition);
        resultMoves = bishop.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(7, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesFromCenter() {
        testPosition = "d5";
        board.put(new Bishop(Player.White), new Position(testPosition));
        gameState.setFakeBoard(board);
        bishop = (Bishop) gameState.getPieceAt(testPosition);
        resultMoves = bishop.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(13, resultMoves.size());
    }

    @Test
    public void testGetPossibleMovesWithTraffic() {
        testPosition = "d5";
        board.put(new Bishop(Player.White), new Position(testPosition));

        board.put(new Bishop(Player.White), new Position("f7"));
        board.put(new Rook(Player.Black), new Position("a8"));
        board.put(new Knight(Player.White), new Position("c4"));
        board.put(new Pawn(Player.Black), new Position("f3"));
        gameState.setFakeBoard(board);

        bishop = (Bishop) gameState.getPieceAt(testPosition);
        resultMoves = bishop.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(6, resultMoves.size());
    }
}
