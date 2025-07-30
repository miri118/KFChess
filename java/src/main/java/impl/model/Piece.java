package impl.model;

import impl.command.Command;
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
    public void onCommand(Command cmd, int nowMs) {
        if (isCommandPossible(cmd)) {
            this.visualState = this.visualState.processCommand(cmd, nowMs);
            this.visualState.update(nowMs);
        }
    }

    /**
     * Reset the piece to idle state.
     */
    public void reset(int startMs) {
        this.visualState.reset(null); // Pass an appropriate Command object if needed
    }

    /**
     * Update the piece state based on current time.
     */
    public void update(int nowMs) {

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

    // You may need to implement this method or assume its existence
    private boolean isCommandPossible(Command cmd) {
        // ...implementation...
        return true;
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

    // public PieceState getPieceState() {
    // return pieceState;
    // }
    // public void setPieceState(PieceState pieceState) {
    // this.pieceState = pieceState;
    // }

    public String getPieceId() {
        return pieceId;
    }

    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }

}