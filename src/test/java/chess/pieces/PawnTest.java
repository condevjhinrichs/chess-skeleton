package chess.pieces;

import chess.Player;
import chess.Position;
import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;


/**
 * Created by jhinrichs on 2/24/15.
 */
public class PawnTest {

    private Pawn testPawn;
    private Pawn ally;
    private Pawn enemy;
    private Map<Position, Piece> positionToPieceMap;

    @Before
    public void setup() {
        testPawn = new Pawn(Player.White);
        ally = new Pawn(Player.White);
        enemy = new Pawn(Player.Black);

        positionToPieceMap = Maps.newHashMap();
    }

    @Test
    public void testGetMovesInit() {
        testPawn.setPosition(new Position('a', 2));
        Set<String> moves = testPawn.getPossibleMoves(positionToPieceMap);
        assertEquals(moves.size(), 2);
    }

    @Ignore
    public void testGetMovesCapturingEnemy() {
        testPawn.setPosition(new Position('a', 2));
        Set<String> moves = testPawn.getPossibleMoves(positionToPieceMap);
        assertEquals(moves.size(), 2);
    }

    @Ignore
    public void testGetMovesWithAllyInCapturePosition() {
        testPawn.setPosition(new Position('a', 2));
        Set<String> moves = testPawn.getPossibleMoves(positionToPieceMap);
        assertEquals(moves.size(), 2);
    }

    @Ignore
    public void testGetMovesWithTraffic() {
        testPawn.setPosition(new Position('a', 2));
        Set<String> moves = testPawn.getPossibleMoves(positionToPieceMap);
        assertEquals(moves.size(), 2);
    }
}
