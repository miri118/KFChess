package It1_interfaces.state;

import It1_interfaces.model.Piece;

public class RemovedState implements PieceState {
    @Override
    public void onMove(Piece piece) {
        System.out.println("Cannot move, removed");
    }

    @Override
    public void onRest(Piece piece) {
        System.out.println("Cannot rest, removed");
    }

    @Override
    public void onRemove(Piece piece) {
        System.out.println("Already removed");
    }

    @Override
    public void onJump(Piece piece) {
        System.out.println("Cannot jump, removed");
    }
}

