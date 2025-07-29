package It1_interfaces.state;
import It1_interfaces.model.Piece;

public class JumpingState implements PieceState {
    @Override
    public void onMove(Piece piece) {
        System.out.println("Currently jumping");
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
        System.out.println("Already jumping");
    }
}