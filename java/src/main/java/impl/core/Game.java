package impl.core;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import impl.enums.CommandType;
import impl.model.Command;
import impl.model.Piece;
import impl.model.board.Board;

import java.util.HashMap;
import java.util.List;

public class Game {
    private Map<String, Piece> pieces;
    private Board board;
    private Queue<Command> userInputQueue;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Initialize the game with pieces, board, and optional event bus.
     */
    public Game(List<Piece> piecesList, Board board) {
        this.pieces = new HashMap<>();
        for (Piece p : piecesList) {
            this.pieces.put(p.getPieceId(), p);
        }
        this.board = board;
    }

    // helpers
    /**
     * Return the current game time in milliseconds.
     */
    public int gameTimeMs() {
        return (int)(System.nanoTime() / 1_000_000); 
    }

    /**
     * Return a brand-new Board wrapping a copy of the background pixels
     * so we can paint sprites without touching the pristine board.
     */
    public Board cloneBoard() {
        return board.clone(); // Assuming Board has a clone method
    }

    /**
     * Start the user input thread for mouse handling.
     */
    public void startUserInputThread() {
        new Thread(() -> {
            while (true) {
                try {
                    Command cmd = getUserInput();
                    if (cmd != null) {
                        userInputQueue.offer(cmd);
                    }
                    Thread.sleep(100); // adjust as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break; // exit on interrupt
                }
            }
        }).start();
    }

    private Command getUserInput() {
        // try {
        //     if (scanner.hasNextLine()) {
        //         String line = scanner.nextLine().trim();
        //         // דוגמה: פקודה בצורה של "move piece1 right"
        //         String[] parts = line.split("\\s+");
        //         if (parts.length >= 3 && parts[0].equalsIgnoreCase("move")) {
        //             String pieceId = parts[1];
        //             String direction = parts[2];
        //             return new Command(timestamp, pieceId, CommandType.MOVE, params);
        //         }
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        return null;
}

    // ─── main public entrypoint ──────────────────────────────────────────────
    /**
     * Main game loop.
     */
    public void run() {
        startUserInputThread();

        int startMs = gameTimeMs();
        for (Piece p : pieces.values()) {
            p.reset(startMs);
        }

        // ─────── main loop ──────────────────────────────────────────────────
        while (!isWin()) {
            int now = gameTimeMs();

            // (1) update physics & animations
            for (Piece p : pieces.values()) {
                p.update(now);
            }

            // (2) handle queued Commands from mouse thread
            // while (!userInputQueue.isEmpty()) {
            //     Command cmd = userInputQueue.poll();
            //     processInput(cmd);
            // }

            // (3) draw current position
            draw();
            if (!show()) {
                break;
            }

            // (4) detect captures
            resolveCollisions();
        }

        announceWin();
        // cv2.destroyAllWindows(); // Not needed in Java
    }

    // ─── drawing helpers ────────────────────────────────────────────────────
    public void processInput(Command cmd) {
        int now = gameTimeMs();
        pieces.get(cmd.getPieceId()).onCommand(cmd, now);
    }

    /**
     * Draw the current game state.
     */
    public void draw() {
        // ...existing code...
    }

    /**
     * Show the current frame and handle window events.
     */
    public boolean show() {
        // ...existing code...
        return true;
    }

    // ─── capture resolution ────────────────────────────────────────────────
    /**
     * Resolve piece collisions and captures.
     */
    public void resolveCollisions() {
        // ...existing code...
    }

    // ─── board validation & win detection ───────────────────────────────────
    /**
     * Check if the game has ended.
     */
    public boolean isWin() {
        // ...existing code...
        return false;
    }

    /**
     * Announce the winner.
     */
    public void announceWin() {
        // ...existing code...
    }
}