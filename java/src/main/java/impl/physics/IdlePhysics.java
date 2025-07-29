package impl.physics;

import impl.model.board.Board;
import impl.model.command.*;;

public class IdlePhysics extends Physics implements PhysicsBehavior {
    public IdlePhysics(int[] startCell, Board board, double speedMS) {
        super(startCell, board, speedMS);
    }

    @Override
    public void reset(impl.model.command.Command cmd) {
        // יישום ל-idle
    }

    @Override
    public Command update(int nowMs) {
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

