package impl.physics;

import impl.model.Command;
import impl.model.board.Board;

public class JumpPhysics extends Physics implements PhysicsBehavior {
    public JumpPhysics(int[] startCell, Board board, double speedMS) {
        super(startCell, board, speedMS);
    }

    @Override
    public void reset(Command cmd) {
        // Initialize jump state
    }

    @Override
    public Command update(int nowMs) {
        // Simulate jump movement
        return null;
    }

    @Override
    public boolean canBeCaptured() {
        return false; // Maybe invincible while jumping
    }

    @Override
    public boolean canCapture() {
        return true;
    }

    @Override
    public int[] getPos() {
        return getStartCell(); // Placeholder
    }
}
