package impl.physics;

import impl.model.Command;
import impl.model.board.Board;

public class MovePhysics extends Physics implements PhysicsBehavior {
    public MovePhysics(int[] startCell, Board board, double speedMS) {
        super(startCell, board, speedMS);
    }

    @Override
    public void reset(Command cmd) {
        // Reset movement parameters based on command
    }

    @Override
    public Command update(int nowMs) {
        // Update position and return movement command
        return null;
    }

    @Override
    public boolean canBeCaptured() {
        return true;
    }

    @Override
    public boolean canCapture() {
        return true;
    }

    @Override
    public int[] getPos() {
        // Return current position during movement
        return getStartCell(); // Placeholder
    }
}
