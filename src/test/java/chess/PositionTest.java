package chess;

import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Basic Unit Tests for the Position class
 */
public class PositionTest {

    @Test
    public void testStringParsingConstructor() {
        Position pos = new Position("d5");

        assertEquals("The column should be 'd'", 'd', pos.getColumn());
        assertEquals("The row should be 5", 5, pos.getRow());
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void testPositionConstructorThrows() throws ExceptionInInitializerError {
        new Position('x', 9);
    }

    @Test
    public void testPositionEquality() {
        Position one = new Position('a', 1);
        Position other = new Position('a', 1);

        assertEquals("The positions should equal each other", one, other);
    }

    @Test
    public void testIsOnBoard() {
        Position d4 = new Position('d', 4);

        // stay in place
        assertTrue(d4.isOnBoard( 0,  0));
        // move to e5
        assertTrue(d4.isOnBoard( 1,  1));
        // move to a1
        assertTrue(d4.isOnBoard(-3, -3));
        // move to d8
        assertTrue(d4.isOnBoard( 0,  4));
        // move to h4
        assertTrue(d4.isOnBoard( 4,  0));
        // move to f2
        assertTrue(d4.isOnBoard( 1, -2));

        // move off board in each of the eight directions:
        assertFalse(d4.isOnBoard( 5,  0));
        assertFalse(d4.isOnBoard(-4,  0));
        assertFalse(d4.isOnBoard( 0,  5));
        assertFalse(d4.isOnBoard( 0, -4));
        assertFalse(d4.isOnBoard( 5, -4));
        assertFalse(d4.isOnBoard(-4,  5));
        assertFalse(d4.isOnBoard( 5,  5));
        assertFalse(d4.isOnBoard(-4, -4));
    }

    @Test
    public void testMoveColumn() {
        Position c4 = new Position('c', 4);
        assertEquals('a', c4.moveColumn(-2));
        assertEquals('b', c4.moveColumn(-1));
        assertEquals('d', c4.moveColumn(1));
        assertEquals('e', c4.moveColumn(2));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testMoveColumnOffBoard() throws Exception {
        Position c4 = new Position('c', 4);
        c4.moveColumn(10);
    }

    @Test
    public void testMoveRow() {
        Position c4 = new Position('c', 4);
        assertEquals(1, c4.moveRow(-3));
        assertEquals(2, c4.moveRow(-2));
        assertEquals(7, c4.moveRow(3));
        assertEquals(8, c4.moveRow(4));
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testMoveRowOffBoard() throws Exception {
        Position c4 = new Position('c', 4);
        c4.moveRow(10);
    }
}
