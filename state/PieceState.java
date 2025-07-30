package It1_interfaces.state;

import It1_interfaces.model.Piece;

public interface PieceState {
    void onMove(Piece piece);
    void onRest(Piece piece);
    void onRemove(Piece piece);
    void onJump(Piece piece);
}

