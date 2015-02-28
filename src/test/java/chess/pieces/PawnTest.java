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
 * Created by jhinrichs on 2/24/15.
 */
public class PawnTest {

    private GameState gameState;
    private Map<Piece, Position> board;
    private Pawn pawn;
    private String testPosition;
    private Set<String> resultMoves;

    @Before
    public void setup() {
        gameState = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testGetPossibleMovesHomeRow() {
        // Pawns can move one or two spaces ahead from home row

        // test white
        testPosition = "a2"; // on edge of board
        board.put(new Pawn(Player.White), new Position(testPosition));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
        assertTrue(resultMoves.contains("a2 a3"));
        assertTrue(resultMoves.contains("a2 a4"));

        // test black
        testPosition = "a7"; // on edge of board
        board.put(new Pawn(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
        assertTrue(resultMoves.contains("a7 a6"));
        assertTrue(resultMoves.contains("a7 a5"));
    }

    @Test
    public void testGetPossibleMovesFromHomeRowWithCapture() {
        // There's a third possible move if an enemy is to the diagonal

        // test white
        testPosition = "g2"; // on edge of board
        board.put(new Pawn(Player.White), new Position(testPosition));
        board.put(new Pawn(Player.Black), new Position("f3"));
        board.put(new Pawn(Player.White), new Position("h3"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(3, resultMoves.size());
        assertTrue(resultMoves.contains("g2 g3"));
        assertTrue(resultMoves.contains("g2 g4"));
        assertTrue(resultMoves.contains("g2 f3"));


        // test black
        testPosition = "g7"; // on edge of board
        board.put(new Pawn(Player.Black), new Position(testPosition));
        board.put(new Pawn(Player.White), new Position("f6"));
        board.put(new Pawn(Player.Black), new Position("h6"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(3, resultMoves.size());
        assertTrue(resultMoves.contains("g7 g6"));
        assertTrue(resultMoves.contains("g7 g5"));
        assertTrue(resultMoves.contains("g7 f6"));
    }

    @Test
    public void testGetPossibleMovesAfterHasMoved() {
        // Pawns can only move one square forward after they have left their home row

        // test white
        testPosition = "d3";
        board.put(new Pawn(Player.White), new Position(testPosition));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(1, resultMoves.size());
        assertTrue(resultMoves.contains("d3 d4"));


        // test black
        testPosition = "d6";
        board.put(new Pawn(Player.Black), new Position(testPosition));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(1, resultMoves.size());
        assertTrue(resultMoves.contains("d6 d5"));
    }

    @Test
    public void testGetPossibleMovesBlocked() {
        // If there's a piece directly ahead, it can't move

        // test white
        testPosition = "d3";
        board.put(new Pawn(Player.White), new Position(testPosition));
        board.put(new Pawn(Player.Black), new Position("d4"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());


        // test black
        testPosition = "d6";
        board.put(new Pawn(Player.Black), new Position(testPosition));
        board.put(new Pawn(Player.White), new Position("d5"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(0, resultMoves.size());
    }
}
