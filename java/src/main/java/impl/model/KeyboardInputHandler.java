package impl.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import impl.model.board.Board;
import impl.model.command.Command;
import impl.model.command.CommandProducer;
import impl.model.command.CommandQueue;

public class KeyboardInputHandler {
    private final CommandProducer commandProducer;
    private final CommandQueue commandQueue;
    private final CursorPositionManager cursorManager;
    private final Board board;
    private final Map<String, Piece> pieces;
    private final Map<String, String> selectedPieceIds = new HashMap<>();

    public KeyboardInputHandler(
            CommandProducer commandProducer,
            CommandQueue commandQueue,
            CursorPositionManager cursorManager,
            Board board,
            Map<String, Piece> pieces) {
        this.commandProducer = commandProducer;
        this.commandQueue = commandQueue;
        this.cursorManager = cursorManager;
        this.board = board;
        this.pieces = pieces;
    }

    public CursorPositionManager getCursorManager() {
        return this.cursorManager;
    }

    public int[] getCursor(String playerId) {
        return cursorManager.getCursor(playerId);
    }

    public void onKeyPressed(String key, int timestamp) {
        System.out.println("[KEYBOARD] Key pressed: " + key + " at " + timestamp);
        // Player 1
        handlePlayerInput("P1", key.toUpperCase(), timestamp, "ENTER", "BACK_SPACE",
                Map.of("UP", new int[] { -1, 0 }, "DOWN", new int[] { 1, 0 }, "LEFT", new int[] { 0, -1 }, "RIGHT",
                        new int[] { 0, 1 }));
        // Player 2
        handlePlayerInput("P2", key.toUpperCase(), timestamp, "SPACE", "SHIFT",
                Map.of("W", new int[] { -1, 0 }, "S", new int[] { 1, 0 }, "A", new int[] { 0, -1 }, "D",
                        new int[] { 0, 1 }));
    }

    private String findPieceAt(int[] pos, String playerId) {
        for (Map.Entry<String, Piece> entry : pieces.entrySet()) {
            int[] p = entry.getValue().getPosition();
            if (p[0] == pos[0] && p[1] == pos[1] &&
                    ((playerId.equals("P1") && entry.getKey().endsWith("B")) ||
                            (playerId.equals("P2") && entry.getKey().endsWith("W")))) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void handlePlayerInput(String playerId, String key, int timestamp, String confirmKey, String jumpKey,
            Map<String, int[]> movementKeys) {
        
        if (movementKeys.containsKey(key)) {
            int[] delta = movementKeys.get(key);
            cursorManager.move(playerId, delta[0], delta[1]);
            int[] newPos = cursorManager.getCursor(playerId);
            System.out.println("[CURSOR] " + playerId + " moved to: " + Arrays.toString(newPos));
            board.renderFrame(pieces.values(), cursorManager); // עדכן רינדור
            // Draw cursor overlay (without repaint)
            // 1. רענון הלוח (למחוק שכבת קורסורים ישנה)
            // 1. רענון הלוח (למחוק שכבת קורסורים ישנה)
            Img boardImg = (Img) board.getImg();
            BufferedImage base = boardImg.getImg();
            BufferedImage updated = new BufferedImage(base.getWidth(), base.getHeight(), base.getType());
            Graphics2D g2d = updated.createGraphics();
            g2d.drawImage(base, 0, 0, null);
            g2d.dispose();
            boardImg.setImg(updated);
            g2d.dispose();
        } else if (key.equals(confirmKey)) {
            System.out.println("[COMMAND] " + playerId + " pressed confirm (" + key + ")");
            sendMoveCommand(playerId, timestamp);
        } else if (key.equals(jumpKey)) {
            System.out.println("[COMMAND] " + playerId + " pressed jump (" + key + ")");
            sendJumpCommand(playerId, timestamp);
        } else {
            System.out.println("[IGNORED] Key '" + key + "' is not mapped for " + playerId);
        }
    }

    private void sendMoveCommand(String pieceId, int timestamp) {
        int[] fromPos = pieces.get(pieceId).getPosition();
        String from = String.format("%c%d", 'A' + fromPos[1], fromPos[0] + 1);
        int[] dest = cursorManager.getCursor(pieceId);
        String to = String.format("%c%d", 'A' + dest[1], dest[0] + 1);
        Command cmd = commandProducer.createMoveCommand(pieceId, from, to, timestamp);
        commandQueue.add(cmd);
    }

    private void sendJumpCommand(String pieceId, int timestamp) {
        int[] fromPos = pieces.get(pieceId).getPosition();
        String from = String.format("%c%d", 'A' + fromPos[1], fromPos[0] + 1);
        int[] dest = cursorManager.getCursor(pieceId);
        String to = String.format("%c%d", 'A' + dest[1], dest[0] + 1);
        Command cmd = commandProducer.createJumpCommand(pieceId, from, to, timestamp);
        commandQueue.add(cmd);
    }
}
