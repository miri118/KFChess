package impl.core;

import java.util.Map;
import java.awt.image.BufferedImage;
import impl.command.Command;
import impl.command.CommandQueue;
import impl.model.Piece;
import impl.model.board.Board;
import impl.ui.GameUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {
    private Map<String, Piece> pieces;
    private Board board;
    private CommandQueue commandQueue;
    // private final Scanner scanner = new Scanner(System.in);
    private GameUI gameUI;
    private final long startNs;

    /**
     * Initialize the game with pieces, board, and optional event bus.
     */
    public Game(List<Piece> piecesList, Board board, GameUI gameUI, CommandQueue queue) {
        this.pieces = new HashMap<>();
        for (Piece p : piecesList) {
            this.pieces.put(p.getPieceId(), p);
        }
        this.board = board;
        this.gameUI = gameUI;
        this.commandQueue = queue;
        this.startNs = System.nanoTime();
    }

    // helpers

    public int gameTimeMs() {
        return (int) ((System.nanoTime() - startNs) / 1_000_000);
    }

    private void resetAllPieces(int startMs) {
        for (Piece p : pieces.values()) {
            p.reset(startMs);
        }
    }

    public void run() {
        int startMs = gameTimeMs();
        resetAllPieces(startMs);
        // ----------------------------------------
        for (Piece p : pieces.values()) {
            if (p.getPieceId().endsWith("B")) {
                int[] pos = p.getPosition();
                System.out.printf("Black Piece %s at %s%n", p.getPieceId(),
                        (char) ('A' + pos[1]) + "" + (pos[0] + 1));
            }
        }
        // ----------------------------------------------------------
        while (!isWin()) {
            int now = gameTimeMs();

            // 1. update all pieces
            for (Piece p : pieces.values()) {
                p.update(now);
            }

            // 2. process commands
            while (!commandQueue.isEmpty()) {
                Command cmd = commandQueue.poll();
                processInput(cmd, now);
            }

            // 3. draw the game state
            draw();

            // 4. show the game UI
            if (!show())
                break;

            // 5. crashing recognize collisions
            resolveCollisions();

            // take a breath
            try {
                Thread.sleep(16); // -60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        announceWin();
    }

    private void processInput(Command cmd, int now) {
        Piece p = pieces.get(cmd.getPieceId());
        if (p != null) {
            p.onCommand(cmd, now, pieces);
        }
    }

    private void draw() {
        BufferedImage frame = gameUI.renderFrame();
        board.updateImageOnUI(frame);
    }

    private boolean show() {
        return true; // Assuming the UI is always shown
    }

    /**
     * recognize collisions and resolve them
     * by removing all but the last piece in each cell
     */
    private void resolveCollisions() {
        Map<String, List<Piece>> cellToPieces = new HashMap<>();
        for (Piece p : pieces.values()) {
            String posKey = Arrays.toString(p.getPosition());
            cellToPieces.computeIfAbsent(posKey, k -> new ArrayList<>()).add(p);
        }

        for (List<Piece> group : cellToPieces.values()) {
            if (group.size() > 1) {
                // eat all, Keep the last piece in the group
                for (int i = 0; i < group.size() - 1; i++) {
                    pieces.remove(group.get(i).getPieceId());
                }
            }
        }
    }

    // win - if there is only one king left
    private boolean isWin() {
        long kings = pieces.values().stream()
                .filter(p -> p.getPieceId().startsWith("KW") || p.getPieceId().startsWith("KB"))
                .count();
        return kings < 2;
    }

    // announce the winner
    private void announceWin() {
        boolean blackWins = pieces.values().stream().anyMatch(p -> p.getPieceId().startsWith("KB"));
        String winner = blackWins ? "Black wins!" : "White wins!";
        System.out.println(winner);
    }

    public Board cloneBoard() {
        return board.clone(); // Assuming Board has a clone method
    }
}