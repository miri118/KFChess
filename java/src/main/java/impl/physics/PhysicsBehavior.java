package impl.physics;

import impl.command.Command;

public interface PhysicsBehavior {
    void reset(Command cmd);
    Command update(int nowMs);
    boolean canBeCaptured();
    boolean canCapture();
    int[] getPos();
}

