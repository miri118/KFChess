package impl.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class PlayerPanel {
    private String playerName;
    private int score;
    private List<String> moves;
    private Rectangle bounds;

    public PlayerPanel(String name, Dimension screenSize) {
        this.playerName = name;
        this.score = 0;
        this.moves = new ArrayList<>();
        this.bounds = new Rectangle(200, screenSize.height);
    }

    public void draw(Graphics2D g, int x) {
        // draw background
        g.setColor(new Color(50, 50, 50, 200));
        g.fillRect(x, 0, bounds.width, bounds.height);
        
        // headline
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(playerName, x + 20, 30);
        
        // score
        g.drawString("score: " + score, x + 20, 60);
        
        // moves
        g.drawString("moves:", x + 20, 90);
        for(int i=0; i < Math.min(moves.size(), 5); i++) {
            g.drawString(moves.get(i), x + 20, 120 + i*30);
        }
    }
    public void setPlayerName(String name) {
        this.playerName = name;
    }
    public Rectangle getBounds() {
        return bounds;
    }

    public String getName() {
        return playerName;
    }
}
