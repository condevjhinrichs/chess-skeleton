package chess;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Describes a position on the Chess Board
 */
public class Position {
    public static final int MIN_ROW = 1;
    public static final int MAX_ROW = 8;
    public static final char MIN_COLUMN = 'a';
    public static final char MAX_COLUMN = 'h';
    private int row;
    private char column;

    private static final List<Character> COLUMNS = Lists.newArrayList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h');

    /**
     * Create a new position object
     *
     * @param column The column
     * @param row The row
     */
    public Position(char column, int row) throws ExceptionInInitializerError {
        if (!COLUMNS.contains(column) || row < MIN_ROW || MAX_COLUMN < row) {
            throw new ExceptionInInitializerError("The position given would be off the board");
        }

        this.row = row;
        this.column = column;
    }

    /**
     * Create a new Position object by parsing the string
     * @param colrow The column and row to use.  I.e. "a1", "h7", etc.
     */
    public Position(String colrow) {
        this(colrow.toCharArray()[0], Character.digit(colrow.toCharArray()[1], 10));
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    public boolean isOnBoard(int colMovement, int rowMovement) {
        int newColIndex = COLUMNS.indexOf(getColumn()) + colMovement;
        int newRow = getRow() + rowMovement;

        return 0 <= newColIndex && newColIndex <= 7 && 1 <= newRow && newRow <= 8;
    }

    /**
     * Changes the column value by a specified offset
     *
     * @param offset
     * @return char- the new column from the offset
     * @throws IndexOutOfBoundsException if the offset would produce a position not on the board
     */
    public char moveColumn(int offset) throws IndexOutOfBoundsException {
        int newColIndex = COLUMNS.indexOf(getColumn()) + offset;
        
        if (0 <= newColIndex && newColIndex <= 7) {
            return COLUMNS.get(newColIndex);
        } else {
            throw new IndexOutOfBoundsException("Moving " + offset + " columns is invalid.");
        }
    }

    /**
     * Changes the row value by the specified offset
     *
     * @param offset
     * @return int- the new row from the offset
     * @throws IndexOutOfBoundsException if the offset would produce a position not on the board
     */
    public int moveRow(int offset) throws IndexOutOfBoundsException {
        int newRowIndex = getRow() + offset;

        if (MIN_ROW <= newRowIndex && newRowIndex <= MAX_ROW) {
            return newRowIndex;
        } else {
            throw new IndexOutOfBoundsException("Moving " + offset + " rows is invalid.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (column != position.column) return false;

        //noinspection RedundantIfStatement
        if (row != position.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + (int) column;
        return result;
    }

    @Override
    public String toString() {
        return "" + column + row;
    }

}
