package impl.observer;

import java.util.ArrayList;
import java.util.List;

import impl.command.*;
import impl.enums.PlayerType;
import interfaces.IObserver;

public class DocumentingMovesManagement implements IObserver {

    private List<DocumentingMove> documentingMoves = new ArrayList<>();
    private Table table;
    private PlayerType playerType;

    public List<DocumentingMove> getDocumentingMoves() {
        return documentingMoves;
    }

    @Override
    public void notify(Command cmd) {
        String pieceId = cmd.getPieceId();
        String fromPosition = cmd.getParams().size() > 0 ? cmd.getParams().get(0).toString() : "";
        String toPosition = cmd.getParams().size() > 1 ? cmd.getParams().get(1).toString() : "";
        int timestamp = cmd.getTimestamp();
        DocumentingMove move = new DocumentingMove(pieceId, fromPosition, toPosition, timestamp);
        documentingMoves.add(move);
        //playerType
        //table.drawOnBackground();
        // Logic to document moves when notified
        // This could involve logging the moves or updating a UI component
        System.out.println("Moves have been documented.");
    }

}