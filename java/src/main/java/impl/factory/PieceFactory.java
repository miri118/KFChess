package impl.factory;
import java.nio.file.Path;

import impl.model.State;
import impl.model.board.Board;


public class PieceFactory {
    private Board board;
    private Path piecesRoot;

    /**
     * Initialize piece factory with board and
     * generates the library of piece templates from the pieces directory.
     */
    public PieceFactory(Board board, Path piecesRoot) {
        this.board = board;
        this.piecesRoot = piecesRoot;
        // ...existing code...
    }

    /**
     * Build a state machine for a piece from its directory.
     */
    private State buildStateMachine(Path pieceDir) {
        // ...existing code...
        return null;
    }

    /**
     * Create a piece of the specified type at the given cell.
     */
    // public Piece createPiece(String pType, int[] cell) {
    //     Piece piece = new Piece(pType, buildStateMachine(piecesRoot.resolve(pType)), new PieceState());
    //     return piece;
    // }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Path getPiecesRoot() {
        return piecesRoot;
    }

    public void setPiecesRoot(Path piecesRoot) {
        this.piecesRoot = piecesRoot;
    }
}