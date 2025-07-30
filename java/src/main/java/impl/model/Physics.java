package impl.model;
import impl.command.Command;
import impl.model.board.Board;

public class Physics {
    private int[] startCell;
    private int[] endCell;
    private Board board;
    private double speedMS;
    private long startMs;
    private double durationMs;

    public Physics(int[] startCell, Board board, double speedMS) {
        this.startCell = startCell;
        this.endCell = startCell.clone();
        this.board = board;
        this.speedMS = speedMS;
        this.startMs = System.currentTimeMillis();
        this.durationMs = 0;
    }

    public void reset(Command command, int[] newTarget) {
        this.startCell = this.endCell;
        this.endCell = newTarget;
        this.startMs = System.currentTimeMillis();
        this.durationMs = computeDuration(startCell, endCell, speedMS);
    }

    private double computeDuration(int[] start, int[] end, double speed) {
        if (speed <= 0.0001) return 0;

        double dx = end[0] - start[0];
        double dy = end[1] - start[1];
        double distanceCells = Math.sqrt(dx * dx + dy * dy);
        double distanceMeters = board.cellToMeters(distanceCells);
        return (distanceMeters / speed) * 1000.0;
    }

    public int[] getCurrentCell(int nowMs) {
        if (durationMs <= 0) return endCell;

        double progress = Math.min(1.0, (nowMs - startMs) / durationMs);
        int currentRow = (int) (startCell[0] + (endCell[0] - startCell[0]) * progress);
        int currentCol = (int) (startCell[1] + (endCell[1] - startCell[1]) * progress);
        return new int[]{ currentRow, currentCol };
    }

    public int[] getStartCell() {
        return startCell;
    }

    public int[] getEndCell() {
        return endCell;
    }

    public double getSpeedMS() {
        return speedMS;
    }

    public void setSpeedMS(double speedMS) {
        this.speedMS = speedMS;
    }

    public long getStartMs() {
        return startMs;
    }

    public double getDurationMs() {
        return durationMs;
    }
}
