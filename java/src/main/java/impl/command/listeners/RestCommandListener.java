package impl.command.listeners;

import java.awt.Graphics2D;
import java.util.Map;

import impl.command.*;
import impl.input.CursorPositionManager;
import impl.model.Piece;
import impl.model.board.Board;
/**
 * Listener for handling "LONG_REST" and "SHORT_REST" commands
 */
public class RestCommandListener{
    private final Map<String, Piece> pieces;
    private final Board board;
    private final CursorPositionManager cursorManager;
    
    public RestCommandListener(Map<String, Piece> pieces, Board board, CursorPositionManager cursorManager) {
        this.pieces = pieces;
        this.board = board;
        this.cursorManager = cursorManager;
    }

    public boolean supports(Command cmd) {
        String type = cmd.getType().toString().toUpperCase();
        return type.equals("LONG_REST") || type.equals("SHORT_REST");
    }

    //TODO
    public void handle(Command cmd) {
        Piece piece = pieces.get(cmd.getPieceId());
        if (piece != null) {
            long now = System.currentTimeMillis();
            piece.onCommand(cmd, (int) now);
            // piece.drawOnBoard(board, (int) now);
            int[] cursorPos = cursorManager.getCursor(cmd.getPieceId());
            board.drawCursorOverlay(cmd.getPieceId(), cursorPos, board.getImg());
            board.repaint(); // ← כן לשים!
        }
    }
}
