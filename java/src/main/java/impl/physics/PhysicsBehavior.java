package impl.physics;

import impl.model.Command;

public interface PhysicsBehavior {
    void reset(Command cmd);
    Command update(int nowMs);
    boolean canBeCaptured();
    boolean canCapture();
    int[] getPos();
}

