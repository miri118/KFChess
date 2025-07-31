package impl.input;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import impl.command.Command;
import impl.command.CommandProducer;
import impl.command.CommandQueue;
import impl.model.Img;
import impl.model.Piece;
import impl.model.board.Board;

public class KeyboardInputHandler {
    private final CommandProducer commandProducer;
    private final CommandQueue commandQueue;
    private final CursorPositionManager cursorManager;
    private final Board board;
    private final Map<String, Piece> pieces;
    private final Map<String, String> selectedPieceByPlayer = new HashMap<>();

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
        String upperKey = key.toUpperCase();

        // player 1 - green – WASD + Space + Shift
        if ("WASD".contains(upperKey) || upperKey.equals("SPACE")) {
            handlePlayerInput("P1", upperKey, timestamp, "SPACE", "SHIFT", Map.of(
                    "W", new int[] { -1, 0 },
                    "S", new int[] { 1, 0 },
                    "A", new int[] { 0, -1 },
                    "D", new int[] { 0, 1 }));
            return;
        }

        // player 2 - purple – Arrow keys + Enter + Backspace
        if (upperKey.equals("UP") || upperKey.equals("DOWN") ||
                upperKey.equals("LEFT") || upperKey.equals("RIGHT") || upperKey.equals("ENTER")) {
            handlePlayerInput("P2", upperKey, timestamp, "ENTER", "BACK_SPACE", Map.of(
                    "UP", new int[] { -1, 0 },
                    "DOWN", new int[] { 1, 0 },
                    "LEFT", new int[] { 0, -1 },
                    "RIGHT", new int[] { 0, 1 }));
            return;
        }

        // ignore unhandled keys
        System.out.println("[IGNORED] Key '" + key + "' is not handled");
    }

    private String findPieceAt(int[] pos, String playerId) {
        for (Map.Entry<String, Piece> entry : pieces.entrySet()) {
            int[] p = entry.getValue().getPosition();
            boolean isControlled = (playerId.equals("P1") && entry.getKey().endsWith("W")) ||
                    (playerId.equals("P2") && entry.getKey().endsWith("B"));
            if (p[0] == pos[0] && p[1] == pos[1] && isControlled) {
                System.out.println("[DEBUG1] Found piece " + entry.getKey() + " at " + Arrays.toString(pos));
                return entry.getKey();
            }
        }
        System.out.println("[DEBUG2] No piece found at: " + Arrays.toString(pos));
        return null;
    }

    private void handlePlayerInput(String playerId, String key, int timestamp, String confirmKey, String jumpKey,
            Map<String, int[]> movementKeys) {

        if (movementKeys.containsKey(key)) {
            int[] delta = movementKeys.get(key);
            cursorManager.move(playerId, delta[0], delta[1]);
            int[] newPos = cursorManager.getCursor(playerId);
            System.out.println("[CURSOR] " + playerId + " moved to: " + Arrays.toString(newPos));
            board.renderFrame(pieces.values(), cursorManager); // update the board display
            // Draw cursor overlay (without repaint)
            // Img boardImg = (Img) board.getImg();
            // BufferedImage base = boardImg.getImg();
            // BufferedImage updated = new BufferedImage(base.getWidth(), base.getHeight(),
            // base.getType());
            // Graphics2D g2d = updated.createGraphics();
            // g2d.drawImage(base, 0, 0, null);
            // g2d.dispose();
            // // boardImg.setImg(updated);
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

    private void sendMoveCommand(String playerId, int timestamp) {
        int[] dest = cursorManager.getCursor(playerId);
        String to = String.format("%c%d", 'A' + dest[1], dest[0] + 1);

        if (!selectedPieceByPlayer.containsKey(playerId)) {
            // try to select a piece at the destination
            String pieceId = findPieceAt(dest, playerId);
            if (pieceId != null) {
                selectedPieceByPlayer.put(playerId, pieceId);
                String from = String.format("%c%d", 'A' + dest[1], dest[0] + 1);
                Command cmd = commandProducer.createSelectCommand(pieceId, from, timestamp);
                commandQueue.add(cmd);
                System.out.printf("[COMMAND] %s selected %s at %s%n", playerId, pieceId, from);
            } else {
                System.out.printf("[COMMAND] %s tried to select but no piece at %s%n", playerId, to);
            }
        } else {
            // כבר בחר כלי – עכשיו זה מהלך MOVE
            String pieceId = selectedPieceByPlayer.get(playerId);
            int[] fromPos = pieces.get(pieceId).getPosition();
            String from = String.format("%c%d", 'A' + fromPos[1], fromPos[0] + 1);

            Command cmd = commandProducer.createMoveCommand(pieceId, from, to, timestamp);
            commandQueue.add(cmd);
            System.out.printf("[COMMAND] %s issued MOVE %s: %s -> %s%n", playerId, pieceId, from, to);

            selectedPieceByPlayer.remove(playerId); // נקה בחירה
        }
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
