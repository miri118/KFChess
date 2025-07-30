package impl.factory;

import impl.model.*;
import impl.model.board.Board;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URL;
import java.util.*;

public class PieceFactory {
    private final Board board;
    private final GraphicsFactory graphicsFactory;
    private final PhysicsFactory physicsFactory;
    private final StateFactory stateFactory;

    public PieceFactory(Board board) {
        this.board = board;
        this.graphicsFactory = new GraphicsFactory();
        this.physicsFactory = new PhysicsFactory(board);
        this.stateFactory = new StateFactory(board, graphicsFactory, physicsFactory);
    }

    public Piece loadPiece(String pieceId, int[] startCell, long timestamp) {
        Path pieceFolder = getPieceFolder(pieceId);
        Path movesPath = pieceFolder.resolve("moves.txt");
        Map<String, State> states = loadAllStates(pieceFolder.resolve("states"), startCell, movesPath);

        //const idle state to first state
        State initial = states.get("idle");
        if (initial == null) {
            throw new IllegalStateException("Missing idle state for piece: " + pieceId);
        }

        //by transitions.csv create transitions
        Path transitionsCsv = pieceFolder.resolve("transitions.csv");
        if (transitionsCsv.toFile().exists()) {
            List<String> lines = readLines(transitionsCsv);
            for (String line : lines) {
                String l = line.strip();
                if (l.isEmpty() || l.startsWith("#") || l.toLowerCase().startsWith("from_state"))
                    continue;
                String[] parts = l.split(",");
                if (parts.length < 3) continue;

                String from = parts[0].trim();
                String event = parts[1].trim().toLowerCase();
                String to = parts[2].trim();

                State fromState = states.get(from);
                State toState = states.get(to);
                if (fromState != null && toState != null) {
                    fromState.setTransition(event, toState);
                }
            }
        }

        return new Piece(pieceId, initial);
    }

    private Map<String, State> loadAllStates(Path statesFolder, int[] startCell, Path moves) {
        Map<String, State> map = new HashMap<>();
        File[] dirs = statesFolder.toFile().listFiles(File::isDirectory);
        if (dirs == null) return map;

        for (File dir : dirs) {
            String stateName = dir.getName();
            Path statePath = dir.toPath();
            State state = stateFactory.load(statePath, moves, startCell);
            map.put(stateName, state);
        }
        return map;
    }

    private static List<String> readLines(Path file) {
        try {
            return java.nio.file.Files.readAllLines(file);
        } catch (Exception e) {
            return List.of();
        }
    }

    private Path getPieceFolder(String pieceId) {
        try {
            URL resourceUrl = getClass().getClassLoader().getResource("pieces");
            if (resourceUrl == null) {
                throw new IllegalStateException("Resource folder 'pieces' not found");
            }
            Path baseFolder = Paths.get(resourceUrl.toURI());
            return baseFolder.resolve(pieceId.substring(0, 2));
        } catch (Exception e) {
            throw new RuntimeException("Failed to locate folder for piece: " + pieceId, e);
        }
    }
}
