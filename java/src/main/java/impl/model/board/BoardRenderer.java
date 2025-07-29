package impl.model.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

import impl.input.CursorPositionManager;
import impl.model.*;

public class BoardRenderer {
    public static BufferedImage renderFrame(Board board,
            Collection<Piece> pieces,
            CursorPositionManager cursorManager) {
                
        Img boardImg = board.getImg();
        BufferedImage raw = boardImg.getImg();
        int width = board.getWCells() * board.getCellWPix();
        int height = board.getHCells() * board.getCellHPix();

        // 1. רקע הלוח
        BufferedImage frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = frame.createGraphics();
        g.drawImage(raw, 0, 0, width, height, null);

        Img frameImg = new Img(frame);

        long now = (int) System.currentTimeMillis();
        // 2. ציור הכלים
        for (Piece p : pieces) {
            p.drawOnBoard(frameImg, board, (int)now);
        }

        // 3. ציור הסמנים
        for (String playerId : Arrays.asList("P1", "P2")) {
            int[] pos = cursorManager.getCursor(playerId);
            Color color = playerId.equals("P1")
                    ? new Color(0, 255, 0, 100)
                    : new Color(128, 0, 128, 100);
            int x = pos[1] * board.getCellWPix();
            int y = pos[0] * board.getCellHPix();
            g.setColor(color);
            g.fillRect(x, y, board.getCellWPix(), board.getCellHPix());
        }

        g.dispose();
        return frame;
    }
}
