package impl.model;

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
        this.bounds = new Rectangle(200, screenSize.height); // רוחב 200, גובה מלא
    }

    public void draw(Graphics2D g, int x) {
        // רקע הפאנל
        g.setColor(new Color(50, 50, 50, 200));
        g.fillRect(x, 0, bounds.width, bounds.height);
        
        // כותרת
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(playerName, x + 20, 30);
        
        // ניקוד
        g.drawString("ניקוד: " + score, x + 20, 60);
        
        // מהלכים
        g.drawString("מהלכים:", x + 20, 90);
        for(int i=0; i < Math.min(moves.size(), 5); i++) {
            g.drawString(moves.get(i), x + 20, 120 + i*30);
        }
    }
}
