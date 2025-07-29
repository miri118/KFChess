package impl.observer;

public class DocumentingMove {
    private String pieceId;
    private String from;
    private String to;
    private int timestamp;

    public DocumentingMove(String pieceId, String from, String to, int timestamp) {
        this.pieceId = pieceId;
        this.from = from;
        this.to = to;
        this.timestamp = timestamp;
    }
    public String getPieceId() {
        return pieceId;
    }
    public void setPieceId(String pieceId) {
        this.pieceId = pieceId;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public int getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    
}
