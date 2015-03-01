package chess;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Basic unit tests for the GameState class
 */
public class GameStateTest {

    private GameState state;
    private Map<Piece, Position> board;
    private String testPosition;

    @Before
    public void setUp() {
        state = new GameState();
        board = Maps.newHashMap();
    }

    @Test
    public void testStartsEmpty() {
        // Make sure all the positions are empty
        for (char col = Position.MIN_COLUMN; col <= Position.MAX_COLUMN; col++) {
            for (int row = Position.MIN_ROW; row <= Position.MAX_ROW; row++) {
                assertNull("All pieces should be empty", state.getPieceAt(String.valueOf(col) + row));
            }
        }
    }

    @Test
    public void testInitialGame() {
        // Start the game
        state.reset();

        // White should be the first player
        Player current = state.getCurrentPlayer();
        assertEquals("The initial player should be White", Player.White, current);

        // Spot check a few pieces
        Piece whiteRook = state.getPieceAt("a1");
        assertTrue("A rook should be at a1", whiteRook instanceof Rook);
        assertEquals("The rook at a1 should be owned by White", Player.White, whiteRook.getOwner());


        Piece blackQueen = state.getPieceAt("d8");
        assertTrue("A queen should be at d8", blackQueen instanceof Queen);
        assertEquals("The queen at d8 should be owned by Black", Player.Black, blackQueen.getOwner());
    }

    @Test
    public void testGetMovesList() {
        state.reset();
        Set<String> moves = state.getMovesList();

        assertEquals("There should be 20 possible first moves", 20, moves.size());
    }

    @Test
    public void testGetPossibleMovesList() {
        state.reset();
        List<String> moves = state.getPossibleMovesList();

        assertEquals("There should be 20 possible first moves", 20, moves.size());
    }

    @Test
    public void testGetPieceAt() {
        state.reset();
        assertEquals(state.getPieceAt("a1").getClass(), Rook.class);
        assertEquals(state.getPieceAt(new Position('a', 1)).getClass(), Rook.class);
    }

    @Test
    public void testGetMovesListInCheck() {
        board.put(new King(Player.White), new Position("d1"));
        board.put(new Rook(Player.White), new Position("a6"));
        board.put(new King(Player.Black), new Position("d8"));
        board.put(new Queen(Player.Black), new Position("c1"));
        board.put(new Bishop(Player.Black), new Position("d2"));
        state.setFakeBoard(board);

        // White king has 1 possible move
        assertEquals(1, state.getPieceAt("d1").getPossibleMoves(state.getPositionToPieceMap()).size());

        // White rook has 14 possible moves
        assertEquals(14, state.getPieceAt("a6").getPossibleMoves(state.getPositionToPieceMap()).size());

        Set<String> whitePossibleMoves = state.getMovesList();
        assertEquals(1, whitePossibleMoves.size());
        assertTrue(whitePossibleMoves.contains("d1 e2"));

    }

    @Test
    public void testMakeMoveIfValidWithInvalidMove() {
        state.reset();
        assertFalse(state.makeMoveIfValid("blah"));
        assertFalse(state.makeMoveIfValid("2 a5"));
        assertFalse(state.makeMoveIfValid("a2 a6"));
        assertFalse(state.makeMoveIfValid("c5 d7"));
    }

    @Test
    public void testMakeMoveIfValidToEmptySpace() {
        testPosition = "d1";
        Player owner = Player.White;
        board.put(new Rook(owner), new Position(testPosition));
        board.put(new King(owner), new Position("e1"));
        state.setFakeBoard(board);

        assertTrue(state.makeMoveIfValid("d1 d5"));
        assertNull(state.getPieceAt("d1"));
        assertEquals(state.getPieceAt("d5").getOwner(), owner);
    }

    @Test
    public void testMakeMoveIfValidToOccupiedSpace() {
        testPosition = "d1";
        Player owner = Player.White;
        board.put(new Rook(owner), new Position(testPosition));
        board.put(new Rook(Player.Black), new Position("d5"));
        board.put(new King(owner), new Position("e1"));
        state.setFakeBoard(board);

        assertTrue(state.makeMoveIfValid("d1 d5"));
        assertNull(state.getPieceAt("d1"));
        assertEquals(state.getPieceAt("d5").getOwner(), owner);
    }

    @Test
    public void testSwitchCurrentPlayer() {
        assertEquals(state.getCurrentPlayer(), Player.White);
        state.switchCurrentPlayer();
        assertEquals(state.getCurrentPlayer(), Player.Black);
        state.switchCurrentPlayer();
        assertEquals(state.getCurrentPlayer(), Player.White);
    }

    @Test
    public void testGetOpponent() {
        assertEquals(state.getCurrentPlayer(), Player.White);
        assertEquals(state.getOpponent(), Player.Black);
        state.switchCurrentPlayer();
        assertEquals(state.getCurrentPlayer(), Player.Black);
        assertEquals(state.getOpponent(), Player.White);
    }

    @Test
    public void testIsInCheckAtInit() {
        state.reset();
        assertFalse(state.isInCheck());
    }

    @Test
    public void testIsInCheck() {
        // test check for Black
        state.switchCurrentPlayer();
        board.put(new King(Player.Black), new Position("e1"));
        board.put(new Rook(Player.White), new Position("e5"));
        state.setFakeBoard(board);
        assertTrue(state.isInCheck());
    }
}
