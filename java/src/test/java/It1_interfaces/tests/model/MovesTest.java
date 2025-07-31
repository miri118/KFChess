package It1_interfaces.tests.model;

import impl.model.Moves;
import impl.model.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MovesTest {

    private Moves moves;

    @BeforeEach
    void setUp() {
        Path movesFile = Path.of("src/test/resources/sample_moves.txt");
        moves = new Moves(movesFile, 8, 8);
    }

    @Test
    void testBasicMoveAllowed() {
        assertTrue(moves.isDstCellValid(0, 1, false), "basic move (0,1) should be allowed");
        assertTrue(moves.isDstCellValid(0, -1, false), "basic move (0,-1) should be allowed");
    }

    @Test
    void testCaptureMoveOnlyWhenPieceExists() {
        assertTrue(moves.isDstCellValid(1, 1, true), "(1,1) with capture=true → allowed");
        assertFalse(moves.isDstCellValid(1, 1, false), "(1,1) with capture=false → not allowed");
    }

    @Test
    void testNonCaptureMoveOnlyWhenNoPiece() {
        assertTrue(moves.isDstCellValid(-1, -1, false), "non-capture move should be allowed only when empty");
        assertFalse(moves.isDstCellValid(-1, -1, true), "non-capture move should fail when destination has piece");
    }

    @Test
    void testUnknownMoveRejected() {
        assertFalse(moves.isDstCellValid(5, 5, false), "undefined move should be rejected");
    }

    @Test
    void testIsValid_FullFlow() {
        Set<Pair> occupied = new HashSet<>();
        occupied.add(new Pair(2, 2)); // simulate an occupied square

        int[] src = {1, 1};
        int[] dstClear = {1, 2};  // (0,1) → allowed
        int[] dstBlocked = {3, 3}; // path not clear

        assertTrue(moves.isValid(src, dstClear, occupied));
        assertFalse(moves.isValid(src, dstBlocked, occupied));
    }
}
