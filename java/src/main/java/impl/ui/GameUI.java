package impl.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.net.URL;
import javax.imageio.ImageIO;

import impl.input.CursorPositionManager;
import impl.model.Img;
import impl.model.Piece;
import impl.model.board.Board;
import impl.model.board.BoardRenderer;

public class GameUI {
    private Img background;
    private Board board;
    private CursorPositionManager cursorManager;
    private Map<String, Piece> pieces;
    private PlayerPanel player1Panel;
    private PlayerPanel player2Panel;

    public GameUI(Board board, CursorPositionManager cursorManager, Map<String, Piece> pieces) {
        this.board = board;
        this.cursorManager = cursorManager;
        this.pieces = pieces;
        loadBackground();
        // for the player panels
        Dimension panelSize = new Dimension(200, background.getImg().getHeight());

        this.player1Panel = new PlayerPanel("player 1", panelSize);
        this.player2Panel = new PlayerPanel("player 2", panelSize);
    }

    private void loadBackground() {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("background.png");
            if (resourceUrl != null) {
                Path backgroundPath = Paths.get(resourceUrl.toURI());
                BufferedImage bgImage = ImageIO.read(backgroundPath.toFile());
                this.background = new Img(bgImage);
            } else {
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

    public void setPlayerNames(String player1Name, String player2Name) {
        player1Panel.setPlayerName(player1Name);
        player2Panel.setPlayerName(player2Name);
    }

    public BufferedImage renderFrame() {
        int margin = 30;
        int panelWidth = player1Panel.getBounds().width;
        int boardWidth = board.getImg().getImg().getWidth();
        int boardHeight = board.getImg().getImg().getHeight();

        int frameWidth = margin * 2 + boardWidth + 2 * panelWidth + 40;//space for player panels
        int frameHeight = margin * 2 + boardHeight;

        BufferedImage frame = new BufferedImage(frameWidth, frameHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = frame.createGraphics();

        // background
        g.setColor(new Color(30, 30, 70)); //const background color
        g.fillRect(0, 0, frameWidth, frameHeight);

        // place board image centerally
        int boardX = margin + panelWidth + 20;
        int boardY = margin;

        //draw board image
        BufferedImage boardFrame = BoardRenderer.renderFrame(board, pieces.values(), cursorManager);
        g.drawImage(boardFrame, boardX, boardY, null);

        // draw player panels
        int panelY = boardY;

        drawSinglePanel(g, "P1", margin, panelY, panelWidth, boardHeight);

        drawSinglePanel(g, "P2", boardX + boardWidth + 20, panelY, panelWidth, boardHeight);
        
        g.dispose();
        return frame;
    }

    private void drawPlayerPanels(Graphics2D g, int boardX, int boardY) {
        int panelWidth = player1Panel.getBounds().width;
        int panelHeight = player1Panel.getBounds().height;

        // pannel player 1
        player1Panel.draw(g, boardX - panelWidth - 20);

        // pannel player 2
        player2Panel.draw(g, boardX + board.getImg().getImg().getWidth() + 20);
    }

    private void drawSinglePanel(Graphics2D g, String playerId,
            int x, int y, int width, int height) {
        // background
        g.setColor(new Color(30, 30, 30, 200));
        g.fillRoundRect(x, y, width, height, 15, 15);

        // border
        g.setColor(playerId.equals("P1") ? Color.GREEN : Color.MAGENTA);
        g.drawRoundRect(x, y, width, height, 15, 15);

        // headline
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(playerId.equals("P1") ? player1Panel.getName() : player2Panel.getName(), x + 20, y + 30);

        // score
        g.drawString("score: 0", x + 20, y + 60);

        // moves
        g.drawString("moves:", x + 20, y + 90);
    }
}