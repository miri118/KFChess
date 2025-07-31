package impl.factory;

import impl.model.Graphics;
import impl.model.Moves;
import impl.model.Physics;
import impl.model.State;
import impl.model.board.Board;
import impl.factory.dto.ConfigDto;
import impl.factory.dto.GraphicsDto;
import impl.factory.dto.PhysicsDto;
import impl.util.JsonUtils;

import java.nio.file.Path;
import java.util.function.Supplier;

public class StateFactory {
    private final Board board;
    private final GraphicsFactory graphicsFactory;
    private final PhysicsFactory physicsFactory;

    public StateFactory(Board board, GraphicsFactory graphicsFactory, PhysicsFactory physicsFactory) {
        this.board = board;
        this.graphicsFactory = graphicsFactory;
        this.physicsFactory = physicsFactory;
    }

    public State load(Path stateFolder, Path movesPath, int[] startCell, String stateName, Supplier<State> selfLoader) {
        Path configPath = stateFolder.resolve("config.json");

        ConfigDto config = JsonUtils.read(configPath, ConfigDto.class);

        GraphicsDto g = config.graphics != null ? config.graphics : new GraphicsDto();
        PhysicsDto p = config.physics != null ? config.physics : new PhysicsDto();

        Graphics graphics = graphicsFactory.load(stateFolder.resolve("sprites"), g.is_loop, g.frames_per_sec);
        Physics physics = physicsFactory.load(startCell, p);
        Moves moves = new Moves(movesPath, board.getHCells(), board.getWCells());

        State state = new State(moves, graphics, physics, stateName, selfLoader);
        return state;
    }
}
