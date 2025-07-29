package impl.model;

import java.util.HashMap;
import java.util.Map;

public class CursorPositionManager {

    private final int maxRow, maxCol;
    private final Map<String, int[]> cursors = new HashMap<>();

    public CursorPositionManager(int maxRow, int maxCol) {
        this.maxRow = maxRow;
        this.maxCol = maxCol;
        cursors.put("P1", new int[] { 0, 0 });
        cursors.put("P2", new int[] { 7, 7 });
    }

    public int[] getCursor(String playerId) {
        return cursors.get(playerId);
    }

    public void move(String playerId, int dRow, int dCol) {
        int[] pos = cursors.get(playerId);
        int newRow = Math.max(0, Math.min(maxRow - 1, pos[0] + dRow));
        int newCol = Math.max(0, Math.min(maxCol - 1, pos[1] + dCol));
        cursors.put(playerId, new int[] { newRow, newCol });
    }
}
