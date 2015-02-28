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
        testPosition = "h2"; // on edge of board
        board.put(new Pawn(Player.White), new Position(testPosition));
        board.put(new Pawn(Player.Black), new Position("g3"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(3, resultMoves.size());
        assertTrue(resultMoves.contains("h2 h3"));
        assertTrue(resultMoves.contains("h2 h4"));
        assertTrue(resultMoves.contains("h2 g3"));


        // test black
        testPosition = "h7"; // on edge of board
        board.put(new Pawn(Player.Black), new Position(testPosition));
        board.put(new Pawn(Player.White), new Position("g6"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(3, resultMoves.size());
        assertTrue(resultMoves.contains("h7 h6"));
        assertTrue(resultMoves.contains("h7 h5"));
        assertTrue(resultMoves.contains("h7 g6"));
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

    @Test
    public void testGetPossibleMovesDiagonal() {
        // Pawns can move diagonally one square to capture enemies

        // test white
        testPosition = "c3";
        board.put(new Pawn(Player.White), new Position(testPosition));
        board.put(new Pawn(Player.White), new Position("b4"));
        board.put(new Pawn(Player.Black), new Position("d4"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
        assertTrue(resultMoves.contains("c3 c4"));
        assertTrue(resultMoves.contains("c3 d4"));


        // test black
        testPosition = "c6";
        board.put(new Pawn(Player.Black), new Position(testPosition));
        board.put(new Pawn(Player.Black), new Position("b5"));
        board.put(new Pawn(Player.White), new Position("d5"));
        gameState.setFakeBoard(board);
        pawn = (Pawn) gameState.getPieceAt(testPosition);

        resultMoves = pawn.getPossibleMoves(gameState.getPositionToPieceMap());
        assertEquals(2, resultMoves.size());
        assertTrue(resultMoves.contains("c6 c5"));
        assertTrue(resultMoves.contains("c6 d5"));
    }
}
