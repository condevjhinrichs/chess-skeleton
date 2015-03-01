package chess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Basic unit tests for the CLITest
 */
@RunWith(MockitoJUnitRunner.class)
public class CLITest {

    private CLI cli;

    @Mock
    private PrintStream testOut;

    @Mock
    private InputStream testIn;

    /**
     * Make sure the CLI initially prints a welcome message
     */
    @Test
    public void testCLIWritesWelcomeMessage() {
        new CLI(testIn, testOut);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(testOut, times(1)).println(captor.capture());

        String message = captor.getValue();

        assertTrue("The CLI should initially print a welcome message", message.startsWith("Welcome"));
    }

    /**
     * Test that the CLI can initially accept input
     */
    @Test
    public void testHelpCommand() throws Exception {
        runCliWithInput("help");

        List<String> output = captureOutput();
        assertEquals("Should have 13 output calls", 13, output.size());
    }

    @Test
    public void testNewCommand() throws Exception {
        runCliWithInput("new");
        List<String> output = captureOutput();

        assertEquals("Should have had 6 calls to print strings", 6, output.size());
        assertEquals("It should have printed the board first", 701, output.get(2).length());
        assertEquals("It should have printed the board again", 701, output.get(4).length());
    }

    @Test
    public void testBoardCommand() throws Exception {
        runCliWithInput("new", "board");
        List<String> output = captureOutput();

        assertEquals("Should have had 9 output calls", 9, output.size());
        assertEquals("It should have printed the board three times", output.get(2), output.get(4));
    }

    @Test
    public void testListCommand() throws Exception {
        runCliWithInput("list");
        List<String> output = captureOutput();

        assertEquals("It should print a title for the action", "White's Possible Moves:", output.get(4));
        assertTrue("It should print the moves list", output.get(5).contains("a2 a4"));
        assertEquals("It should have printed the board twice", output.get(2), output.get(output.size() - 2));
    }

    @Test
    public void testMoveRejectsInvalidInput() {
        testInvalidInput("move c5 g8");
        testInvalidInput("movea2 a3");
    }

    private void testInvalidInput(String input) {
        runCliWithInput(input);
        List<String> output = captureOutput();
        String boardBeforeMove = output.get(2);
        String boardAfterMove = output.get(5);

        assertTrue("The user should be given a message", output.get(4).contains("Invalid move"));
        assertEquals("The board should not have changed", boardBeforeMove, boardAfterMove);
        assertEquals("It should still be White's turn", "White's Move", output.get(6));
    }

    @Test
    public void testMoveForValidInput() throws Exception {
        testValidInput("move a2 a4");
        testValidInput("  move    a2 a4");
        testValidInput("    move    a2    a4  ");
    }

    private void testValidInput(String input) {
        runCliWithInput(input);
        List<String> output = captureOutput();
        String boardBeforeMove = output.get(2);
        String boardAfterMove = output.get(4);

        assertTrue("The board should be updated", !boardBeforeMove.equals(boardAfterMove));
        assertEquals("It should be Black's turn", "Black's Move", output.get(5));
    }

    @Test
    public void testListCommandAfterMove() throws Exception {
        runCliWithInput("move a2 a4", "list");
        List<String> output = captureOutput();

        assertEquals("It should print a title", "Black's Possible Moves:", output.get(6));
        assertTrue("It should print the moves list", output.get(7).contains("f7 f5"));
    }


    private List<String> captureOutput() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        // 9 times means we printed Welcome, the input prompt twice, and the 'help' screen
        verify(testOut, atLeastOnce()).println(captor.capture());

        return captor.getAllValues();
    }

    private CLI runCliWithInput(String... inputLines) {
        StringBuilder builder = new StringBuilder();
        for (String line : inputLines) {
            builder.append(line).append(System.getProperty("line.separator"));
        }

        ByteArrayInputStream in = new ByteArrayInputStream(builder.toString().getBytes());
        cli = new CLI(in, testOut);
        cli.startEventLoop();

        return cli;
    }
}
