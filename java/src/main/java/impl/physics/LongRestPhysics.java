package impl.physics;

import impl.model.Command;
import impl.model.board.Board;

public class LongRestPhysics extends Physics implements PhysicsBehavior {
    public LongRestPhysics(int[] startCell, Board board, double speedMS) {
        super(startCell, board, speedMS);
    }

    @Override
    public void reset(Command cmd) {
        // Start long rest logic
    }

    @Override
    public Command update(int nowMs) {
        // Resting – no movement
        return null;
    }

    @Override
    public boolean canBeCaptured() {
        return false; // maybe immune while in deep rest
    }

    @Override
    public boolean canCapture() {
        return false;
    }

    @Override
    public int[] getPos() {
        return getStartCell();
    }
}
