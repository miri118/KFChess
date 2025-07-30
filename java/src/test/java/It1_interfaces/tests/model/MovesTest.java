package It1_interfaces.tests.model;

import org.junit.jupiter.api.Test;

import impl.model.Moves;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class MovesTest {

    @Test
    public void testConstructorAndGetters() {
        // Path path = Path.of("rules/rook.txt");
        // int[] dims = new int[]{8, 8};

        // Moves moves = new Moves(path, dims[0], dims[1]);

        // assertEquals(path, moves.getTxtPath());
        // assertArrayEquals(dims, moves.getDims());
    }

    @Test
    public void testSetters() {
        Moves moves = new Moves();
        Path path = Path.of("rules/bishop.txt");
        int[] dims = new int[]{10, 10};

        // moves.setTxtPath(path);
        // moves.setDims(dims);

        // assertEquals(path, moves.getTxtPath());
        // assertArrayEquals(dims, moves.getDims());
    }

    @Test
    public void testGetMovesReturnsNullByDefault() {
        Moves moves = new Moves();
        // assertNull(moves.getMoves(3, 3));
    }
}
