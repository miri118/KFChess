package impl.physics;

import impl.command.Command;
import impl.model.board.Board;

public class ShortRestPhysics extends Physics implements PhysicsBehavior {
    public ShortRestPhysics(int[] startCell, Board board, double speedMS) {
        super(startCell, board, speedMS);
    }

    @Override
    public void reset(Command cmd) {
        // Start short rest timer
    }

    @Override
    public Command update(int nowMs) {
        // Resting – no movement
        return null;
    }

    @Override
    public boolean canBeCaptured() {
        return true;
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
