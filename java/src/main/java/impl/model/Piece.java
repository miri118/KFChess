package impl.model;

import java.util.Map;
import java.util.Set;

import impl.command.Command;
import impl.enums.CommandType;
import impl.model.board.Board;

public class Piece {

    private State visualState; // management of visual state
    private String pieceId;

    public Piece(String pieceId, State initState) {
        this.pieceId = pieceId;
        this.visualState = initState;
        // this.pieceState = initPieceState;
    }

    /**
     * Handle a command for this piece.
     */
    public void onCommand(Command cmd, int nowMs, Map<String, Piece> allPieces) {
        if (cmd.getType() == CommandType.MOVE || cmd.getType() == CommandType.JUMP) {
            if (cmd.getParams().size() != 2)
                return;

            int[] from = parseCell(cmd.getParams().get(0));
            int[] to = parseCell(cmd.getParams().get(1));

            if (!isAtExpectedSource(from))
                return;
            if (!isTargetLegal(to, allPieces))
                return;
            if (!isMoveAllowed(from, to, allPieces))
                return;

            this.visualState = this.visualState.processCommand(cmd, nowMs);
            this.visualState.update(nowMs);
            System.out.printf("[OK] %s moves from %s to %s%n", pieceId, formatCell(from), formatCell(to));
        }
    }

    
    private boolean isAtExpectedSource(int[] from) {
        int[] actual = getPosition();
        if (from[0] != actual[0] || from[1] != actual[1]) {
            System.out.printf("[ILLEGAL] %s tried to move from wrong position (expected %s, got %s)%n",
                    pieceId, formatCell(actual), formatCell(from));
            return false;
        }
        return true;
    }

    private boolean isTargetLegal(int[] to, Map<String, Piece> allPieces) {
        for (Piece p : allPieces.values()) {
            int[] pos = p.getPosition();
            if (pos[0] == to[0] && pos[1] == to[1]) {
                if (isSamePlayer(p.getPieceId(), this.pieceId)) {
                    System.out.printf("[ILLEGAL] %s tried to move onto own piece at %s%n", pieceId, formatCell(to));
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isMoveAllowed(int[] from, int[] to, Map<String, Piece> allPieces) {
        Set<Pair> occupied = getOccupiedCells(allPieces);
        Moves moves = visualState.getMoves();
        if (!moves.isValid(from, to, occupied)) {
            System.out.printf("[ILLEGAL] %s move from %s to %s is not allowed by Moves%n",
                    pieceId, formatCell(from), formatCell(to));
            return false;
        }
        return true;
    }

    // helper methods
    private int[] parseCell(String cell) {
        char colChar = Character.toUpperCase(cell.charAt(0));
        int col = colChar - 'A';
        int row = Integer.parseInt(cell.substring(1)) - 1;
        return new int[] { row, col };
    }

    private String formatCell(int[] cell) {
        return String.format("%c%d", 'A' + cell[1], cell[0] + 1);
    }

    private boolean isSamePlayer(String id1, String id2) {
        return id1.endsWith("B") && id2.endsWith("B") || id1.endsWith("W") && id2.endsWith("W");
    }

    private Set<Pair> getOccupiedCells(Map<String, Piece> allPieces) {
        Set<Pair> occ = new java.util.HashSet<>();
        for (Piece p : allPieces.values()) {
            int[] pos = p.getPosition();
            occ.add(new Pair(pos[0], pos[1]));
        }
        return occ;
    }

    /**
     * Reset the piece to idle state.
     */
    public void reset(int startMs) {
        Command idleCmd = new Command(startMs, pieceId, CommandType.IDLE);
        this.visualState.reset(idleCmd); // Pass an appropriate Command object if needed
    }

    /**
     * Update the piece state based on current time.
     */
    public void update(int nowMs) {
        this.visualState = this.visualState.update(nowMs);
    }

    /**
     * Draw the piece on the board with cooldown overlay.
     */

    public void drawOnBoard(Img frameImg, Board board, int nowMs) {
        // 1. get the cell position from the physics state
        int[] cell = visualState.getPhysics().getEndCell(); // [row, col]

        // 2. cast to pixel position
        int wPix = board.getCellWPix();
        int hPix = board.getCellHPix();

        // 3. get the current sprite image from the visual state
        Img sprite = this.visualState.getCurrentSprite(nowMs); // מימוש נדרש ב-State שלך
        System.out.println("cell = " + cell[0] + ", " + cell[1]);

        if (sprite == null) {
            System.out.println("sprite is null for pieceId: " + pieceId);
            return;
        }
        System.out.printf("Drawing piece %s at [%d,%d] -> pixels (%d,%d)%n",
                pieceId, cell[0], cell[1], cell[1] * wPix, cell[0] * hPix);

        sprite.drawOn(frameImg, cell[0], cell[1], wPix, hPix);
    }

    public int[] getPosition() {
        return visualState.getPhysics().getEndCell();
    }

    public State getState() {
        return visualState;
    }

    public void setState(State visualState) {
        this.visualState = visualState;
    }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

}