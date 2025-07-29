package impl.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.net.URL;
import javax.imageio.ImageIO;

import impl.model.board.Board;
import impl.model.board.BoardRenderer;

public class GameUI {
    private Img background;
    private Board board;
    private CursorPositionManager cursorManager;
    private Map<String, Piece> pieces;

    public GameUI(Board board, CursorPositionManager cursorManager, Map<String, Piece> pieces) {
        this.board = board;
        this.cursorManager = cursorManager;
        this.pieces = pieces;
        loadBackground();
    }

    private void loadBackground() {
        try {
            // טעינת רקע מתיקיית resources
            URL resourceUrl = getClass().getClassLoader().getResource("background.png");
            if (resourceUrl != null) {
                Path backgroundPath = Paths.get(resourceUrl.toURI());
                BufferedImage bgImage = ImageIO.read(backgroundPath.toFile());
                this.background = new Img(bgImage);
            } else {
                // רקע ברירת מחדל אם אין תמונה
                BufferedImage defaultBg = new BufferedImage(1000, 800, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = defaultBg.createGraphics();
                g.setColor(new Color(30, 30, 70));
                g.fillRect(0, 0, 1000, 800);
                g.dispose();
                this.background = new Img(defaultBg);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load background", e);
        }
    }

    public BufferedImage renderFrame() {
        // 1. ציור רקע
        BufferedImage frame = new BufferedImage(
            background.getImg().getWidth(),
            background.getImg().getHeight(),
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = frame.createGraphics();
        g.drawImage(background.getImg(), 0, 0, null);

        // 2. חישוב מיקום הלוח במרכז
        int boardX = (frame.getWidth() - board.getImg().getImg().getWidth()) / 2;
        int boardY = (frame.getHeight() - board.getImg().getImg().getHeight()) / 2;

        // 3. ציור הלוח עם הכלים (באמצעות BoardRenderer הקיים)
        BufferedImage boardFrame = BoardRenderer.renderFrame(board, pieces.values(), cursorManager);
        g.drawImage(boardFrame, boardX, boardY, null);

        // 4. ציור פאנלי שחקנים
        drawPlayerPanels(g, boardX, boardY);

        g.dispose();
        return frame;
    }

    private void drawPlayerPanels(Graphics2D g, int boardX, int boardY) {
        int panelWidth = 200;
        int panelHeight = board.getImg().getImg().getHeight();

        // פאנל שחקן שמאל
        drawSinglePanel(g, "P1", boardX - panelWidth - 20, boardY, panelWidth, panelHeight);

        // פאנל שחקן ימין
        drawSinglePanel(g, "P2", boardX + board.getImg().getImg().getWidth() + 20, 
                       boardY, panelWidth, panelHeight);
    }

    private void drawSinglePanel(Graphics2D g, String playerId, 
                               int x, int y, int width, int height) {
        // רקע פאנל
        g.setColor(new Color(30, 30, 30, 200));
        g.fillRoundRect(x, y, width, height, 15, 15);

        // מסגרת
        g.setColor(playerId.equals("P1") ? Color.GREEN : Color.MAGENTA);
        g.drawRoundRect(x, y, width, height, 15, 15);

        // כותרת
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(playerId.equals("P1") ? "שחקן 1" : "שחקן 2", x + 20, y + 30);

        // ניקוד
        g.drawString("ניקוד: 0", x + 20, y + 60);

        // כותרת מהלכים
        g.drawString("מהלכים:", x + 20, y + 90);
    }
}