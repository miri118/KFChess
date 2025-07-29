package impl.factory;

import impl.model.board.Board;
import impl.physics.Physics;

public class PhysicsFactory { // very light for now
    private Board board;

    /**
     * Initialize physics factory with board.
     */
    public PhysicsFactory(Board board) {
        this.board = board;
    }

    /**
     * Create a physics object with the given configuration.
     */
    public Physics create(Object start_cell, Object cfg) {
        // ...implementation...
        return null;
    }
}
