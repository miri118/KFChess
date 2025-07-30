package impl.factory;
import impl.factory.dto.PhysicsDto;
import impl.model.Physics;
import impl.model.board.Board;
    public class PhysicsFactory {
    private final Board board;

    public PhysicsFactory(Board board) {
        this.board = board;
    }

    public Physics load(int[] startCell, PhysicsDto dto) {
        double speed =dto != null ? dto.speed_m_per_sec : 0.0;
        return new Physics(startCell, board, speed);
    }
}
