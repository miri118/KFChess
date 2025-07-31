package It1_interfaces.tests.model;

import impl.model.Img;
import impl.model.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private Img mockImg;

    @BeforeEach
    void setUp() {
        // Create dummy 160x160 image for 8x8 board with 20x20 cells
        BufferedImage dummyImage = new BufferedImage(160, 160, BufferedImage.TYPE_INT_ARGB);
        mockImg = new Img(dummyImage);
        board = new Board(20, 20, 1, 1, 8, 8, mockImg);
    }

    @Test
    void testGettersAndSetters() {
        board.setCellHPix(15);
        assertEquals(15, board.getCellHPix());

        board.setCellWPix(25);
        assertEquals(25, board.getCellWPix());

        board.setCellHtMeters(2);
        assertEquals(2, board.getCellHtMeters());

        board.setCellWMeters(3);
        assertEquals(3, board.getCellWMeters());

        board.setWCells(10);
        assertEquals(10, board.getWCells());

        board.setHCells(12);
        assertEquals(12, board.getHCells());

        BufferedImage anotherDummy = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Img newImg = new Img(anotherDummy);
        board.setImg(newImg);
        assertEquals(newImg, board.getImg());
    }

    @Test
    void testClone() {
        Board clone = board.clone();

        assertNotSame(board, clone);
        assertEquals(board.getCellHPix(), clone.getCellHPix());
        assertEquals(board.getCellWPix(), clone.getCellWPix());
        assertEquals(board.getCellHtMeters(), clone.getCellHtMeters());
        assertEquals(board.getCellWMeters(), clone.getCellWMeters());
        assertEquals(board.getWCells(), clone.getWCells());
        assertEquals(board.getHCells(), clone.getHCells());
        assertNotSame(board.getImg(), clone.getImg());
    }

    @Test
    void testCellStringToCoords_Valid() {
        int[] coords = board.cellStringToCoords("A1");
        assertArrayEquals(new int[]{0, 0}, coords);

        coords = board.cellStringToCoords("H8");
        assertArrayEquals(new int[]{7, 7}, coords);
    }

    @Test
    void testCellStringToCoords_InvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> board.cellStringToCoords("Z"));
        assertThrows(IllegalArgumentException.class, () -> board.cellStringToCoords("123"));
        assertThrows(IllegalArgumentException.class, () -> board.cellStringToCoords(null));
    }

    @Test
    void testCellStringToCoords_OutOfBounds() {
        assertThrows(IllegalArgumentException.class, () -> board.cellStringToCoords("I1"));
        assertThrows(IllegalArgumentException.class, () -> board.cellStringToCoords("A9"));
    }
}
