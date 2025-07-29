package impl.model.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.function.Function;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import impl.input.CursorPositionManager;
import impl.input.KeyboardInputHandler;
import impl.model.Img;
import impl.model.Piece;
import impl.ui.GameUI;

public class Board {
    private int cellHPix;
    private int cellWPix;
    private int cellHtMeters;
    private int cellWMeters;
    private int wCells;
    private int hCells;
    private JFrame frame;
    private JLabel label;
    private Img img;

    public Board(int cellHPix, int cellWPix, int cellHtMeters, int cellWMeters,
            int wCells, int hCells, Img img) {
        this.cellHPix = cellHPix;
        this.cellWPix = cellWPix;
        this.cellHtMeters = cellHtMeters;
        this.cellWMeters = cellWMeters;
        this.wCells = wCells;
        this.hCells = hCells;
        this.img = img;

        int targetWidth = wCells * cellWPix;
        int targetHeight = hCells * cellHPix;
        BufferedImage original = img.getImg();
        if (original.getWidth() == targetWidth && original.getHeight() == targetHeight) {
            this.img = img;
        } else {
            // scale the image to fit the board dimensions
            Image scaled = original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
            BufferedImage resized = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resized.createGraphics();
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();
            this.img = new Img(resized);
        }
    }

    public int[] cellStringToCoords(String cell) {
        if (cell == null || cell.length() != 2) {
            throw new IllegalArgumentException("Invalid cell format: " + cell);
        }

        char colChar = Character.toUpperCase(cell.charAt(0));
        char rowChar = cell.charAt(1);

        int col = colChar - 'A';
        int row = Character.getNumericValue(rowChar) - 1;

        if (col < 0 || col >= wCells || row < 0 || row >= hCells) {
            throw new IllegalArgumentException("Cell out of board bounds: " + cell);
        }

        return new int[] { row, col };
    }

    public void repaint() {
    }

    public void renderFrame(Collection<Piece> pieces, CursorPositionManager cursorManager) {
        BufferedImage frame = BoardRenderer.renderFrame(this, pieces, cursorManager);
        if (label != null) {
            label.setIcon(new ImageIcon(frame));
        }
    }

    public void drawCursorOverlay(String playerId, int[] cell, Img boardImg) {
        if (!(boardImg instanceof Img)) {
            throw new IllegalArgumentException("boardImg must be instance of Img");
        }
        BufferedImage buffer = ((Img) boardImg).getImg();
        Graphics2D g = buffer.createGraphics();
        Color color = playerId.equals("P1") ? new Color(0, 0, 255, 100) : new Color(0, 255, 0, 100);
        int x = cell[1] * getCellWPix();
        int y = cell[0] * getCellHPix();
        g.setColor(color);
        g.fillRect(x, y, getCellWPix(), getCellHPix());
        g.dispose();
    }

    public void showWithInput(CursorPositionManager cursorManager,
            KeyboardInputHandler inputHandler,
            Collection<Piece> pieces) {
        SwingUtilities.invokeLater(() -> {
            int width = getWCells() * getCellWPix();
            int height = getHCells() * getCellHPix();

            // פונקציה בונה לנו פריים מלא מכל הרשימות
            Function<BufferedImage, BufferedImage> renderFrame = raw -> BoardRenderer.renderFrame(this, pieces,
                    cursorManager);

            BufferedImage raw = this.img.getImg();
            BufferedImage initial = renderFrame.apply(raw);

            label = new JLabel(new ImageIcon(initial));
            frame = new JFrame("Chess Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(label);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            frame.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    String key = switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> "UP";
                        case KeyEvent.VK_DOWN -> "DOWN";
                        case KeyEvent.VK_LEFT -> "LEFT";
                        case KeyEvent.VK_RIGHT -> "RIGHT";
                        case KeyEvent.VK_ENTER -> "ENTER";
                        case KeyEvent.VK_SPACE -> "SPACE";
                        case KeyEvent.VK_W -> "W";
                        case KeyEvent.VK_A -> "A";
                        case KeyEvent.VK_S -> "S";
                        case KeyEvent.VK_D -> "D";
                        default -> null;
                    };
                    if (key != null) {
                        inputHandler.onKeyPressed(key, (int) System.currentTimeMillis());
                        // ברגע שלחצו – מבצעים רינדור מחדש של לוח+כלים+סמנים
                        BufferedImage next = renderFrame.apply(raw);
                        label.setIcon(new ImageIcon(next));
                    }
                }
            });
        });
    }

    public void showWithGameUI(GameUI gameUI, KeyboardInputHandler inputHandler) {
    SwingUtilities.invokeLater(() -> {
        frame = new JFrame("KONG FU CHESS");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage rendered = gameUI.renderFrame();
        System.out.println("rendered frame: " + rendered.getWidth() + "x" + rendered.getHeight());

        JLabel label = new JLabel(new ImageIcon(rendered));
        frame.getContentPane().add(label); // ← חובה!
        
        frame.setPreferredSize(new Dimension(rendered.getWidth() + 40, rendered.getHeight() + 40));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String key = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> "UP";
                    case KeyEvent.VK_DOWN -> "DOWN";
                    case KeyEvent.VK_LEFT -> "LEFT";
                    case KeyEvent.VK_RIGHT -> "RIGHT";
                    case KeyEvent.VK_ENTER -> "ENTER";
                    case KeyEvent.VK_SPACE -> "SPACE";
                    case KeyEvent.VK_W -> "W";
                    case KeyEvent.VK_A -> "A";
                    case KeyEvent.VK_S -> "S";
                    case KeyEvent.VK_D -> "D";
                    default -> null;
                };
                if (key != null) {
                    inputHandler.onKeyPressed(key, (int) System.currentTimeMillis());
                    label.setIcon(new ImageIcon(gameUI.renderFrame()));
                }
            }
        });
    });
}


    // Clone the board with a copy of the image.
    public Board clone() {
        return new Board(
                this.cellHPix,
                this.cellWPix,
                this.cellHtMeters,
                this.cellWMeters,
                this.wCells,
                this.hCells,
                this.img != null ? (Img) this.img.clone() : null);
    }

    public int getCellHPix() {
        return cellHPix;
    }

    public void setCellHPix(int cellHPix) {
        this.cellHPix = cellHPix;
    }

    public int getCellWPix() {
        return cellWPix;
    }

    public void setCellWPix(int cellWPix) {
        this.cellWPix = cellWPix;
    }

    public int getCellHtMeters() {
        return cellHtMeters;
    }

    public void setCellHtMeters(int cellHtMeters) {
        this.cellHtMeters = cellHtMeters;
    }

    public int getCellWMeters() {
        return cellWMeters;
    }

    public void setCellWMeters(int cellWMeters) {
        this.cellWMeters = cellWMeters;
    }

    public int getWCells() {
        return wCells;
    }

    public void setWCells(int wCells) {
        this.wCells = wCells;
    }

    public int getHCells() {
        return hCells;
    }

    public void setHCells(int hCells) {
        this.hCells = hCells;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }
}
