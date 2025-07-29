package impl.observer;

import java.util.ArrayList;
import java.util.List;

import impl.model.board.Board;

import java.awt.Color;

public class Table {
    List<DocumentingMove> documentingMovesManagementList = new ArrayList<>();
    private int wPix;
    private int hPix;

    public Table(int wPix, int hPix) {
        this.wPix = wPix;
        this.hPix = hPix;
    }

    public void drawOnBackground(Board background) {
        if (background == null || background.getImg() == null || documentingMovesManagementList == null
                || documentingMovesManagementList.isEmpty())
            return;
        // Img img = background.getImg();
        int startX = 0;
        int startY = 0;
        int totalW = wPix;
        int totalH = hPix;
        int rowHeight = 20;
        float fontSize = 1.2f;
        Color textColor = Color.BLACK;
        int maxRows = totalH / rowHeight;
        int displayedRows = Math.min(documentingMovesManagementList.size(), maxRows);
        for (int i = 0; i < displayedRows; i++) {
            DocumentingMove move = documentingMovesManagementList.get(i);
            if (move == null)
                continue;
            int y = startY + i * rowHeight;
            // img.putText("pieceId: " + move.getPieceId(), startX + 10, y, fontSize,
            // textColor, 1);
            // img.putText("from: " + move.getFrom(), startX + 150, y, fontSize, textColor,
            // 1);
            // img.putText("to: " + move.getTo(), startX + 300, y, fontSize, textColor, 1);
            // img.putText("timestamp: " + move.getTimestamp(), startX + 450, y, fontSize,
            // textColor, 1);
        }
    }
}
