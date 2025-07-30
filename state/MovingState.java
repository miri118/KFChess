package It1_interfaces.state;

import It1_interfaces.model.Piece;
import It1_interfaces.physics.JumpPhysics;
import It1_interfaces.physics.Physics;

public class MovingState implements PieceState {
    @Override
    public void onMove(Piece piece) {
        System.out.println("Already moving");
    }

    @Override
    public void onRest(Piece piece) {
        piece.setPieceState(new IdleState());
    }

    @Override
    public void onRemove(Piece piece) {
        piece.setPieceState(new RemovedState());
    }

    @Override
    public void onJump(Piece piece) {
        piece.setPieceState(new JumpingState());
        Physics p = piece.getState().getPhysics();
        piece.getState().setPhysics(new JumpPhysics(p.getPos(), p.getBoard(), p.getSpeedMS()));
    }
}
