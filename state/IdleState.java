package It1_interfaces.state;

import It1_interfaces.model.Piece;
import It1_interfaces.physics.JumpPhysics;
import It1_interfaces.physics.MovePhysics;
import It1_interfaces.physics.Physics;
public class IdleState implements PieceState {
    @Override
    public void onMove(Piece piece) {
        piece.setPieceState(new MovingState());
        Physics p = piece.getState().getPhysics();
        piece.getState().setPhysics(new MovePhysics(p.getStartCell(), p.getBoard(), p.getSpeedMS()));
    }

    @Override
    public void onRest(Piece piece) {
        System.out.println("Already idle");
    }

    @Override
    public void onRemove(Piece piece) {
        piece.setPieceState(new RemovedState());
    }

    @Override
    public void onJump(Piece piece) {
        piece.setPieceState(new JumpingState());
        Physics p = piece.getState().getPhysics();
        piece.getState().setPhysics(new JumpPhysics(p.getStartCell(), p.getBoard(), p.getSpeedMS()));
    }
}

