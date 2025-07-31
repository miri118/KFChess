package impl.command.listeners;

import java.util.Map;

import impl.command.*;
import impl.input.CursorPositionManager;
import impl.model.Piece;
import impl.model.board.Board;
import impl.observer.Subject;
import interfaces.ISubject;

public class IdleCommandListener{

    private final Map<String, Piece> pieces;
    private final Board board;
    private final CursorPositionManager cursorManager;
    private ISubject subject = new Subject();
    public IdleCommandListener(Map<String, Piece> pieces, Board board, CursorPositionManager cursorManager) {
        this.pieces = pieces;
        this.board = board;
        this.cursorManager = cursorManager;
    }

    public boolean supports(Command cmd) {
        return cmd.getType().equals("IDLE");
    }

    //TODO
    public void handle(Command cmd) {
        Piece piece = pieces.get(cmd.getPieceId());
        if (piece != null) {
            long now = System.currentTimeMillis();
            piece.onCommand(cmd, (int) now, pieces);
            // piece.drawOnBoard(board, (int) now);
            int[] cursorPos = cursorManager.getCursor(cmd.getPieceId());
            board.drawCursorOverlay(cmd.getPieceId(), cursorPos, board.getImg());
            board.repaint();
        }
    }
}