package impl.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import impl.command.*;
import impl.enums.PlayerType;
import impl.model.Piece;
import interfaces.IObserver;

public class Score implements IObserver {

    private int player1Score;
    private int player2Score;

    static final char PLAYER1 = 'B';
    static final char PLAYER2 = 'W';

    Map<String, Integer> scoresPerPiece;

    Map<String, Piece> pieces = new HashMap<>();

    public Score(Map<String, Piece> pieces) {
        this.player1Score = 0;
        this.player2Score = 0;
        this.scoresPerPiece = new HashMap<>();
        this.pieces = pieces;
    }

    public void initialMap() {
        scoresPerPiece.put("P", 1);
        scoresPerPiece.put("N", 3);
        scoresPerPiece.put("B", 3);
        scoresPerPiece.put("R", 5);
        scoresPerPiece.put("Q", 9);
        scoresPerPiece.put("K", 100);
    }

    public void updatePlayer1Score(int score) {
        this.player1Score += score;
    }

    public void updatePlayer2Score(int score) {
        this.player2Score += score;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    @Override
    public void notify(Command cmd) {
        String dest = cmd.getParams().get(1).toString();
        Piece piece = pieces.get(dest);
        if (piece == null) {
            int score = scoresPerPiece.get(cmd.getPieceId().substring(0, 1));
            if (cmd.getPieceId().charAt(0) == PLAYER1) {
                updatePlayer1Score(score);
            } else if (cmd.getPieceId().charAt(0) == PLAYER2) {
                updatePlayer2Score(score);
            }
        }

    }

}
