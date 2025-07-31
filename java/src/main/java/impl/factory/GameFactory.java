package impl.factory;

import impl.command.CommandProducer;
import impl.command.CommandQueue;
import impl.core.Game;
import impl.input.*;
import impl.model.*;
import impl.model.board.Board;
import impl.ui.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class GameFactory {

    public void start() throws Exception {
        // 1. load the board image
        URL boardPath = getClass().getClassLoader().getResource("board.png");
        BufferedImage image = ImageIO.read(boardPath);
        Img imgBoard = new Img(image);

        // 2. create the board
        Board board = new Board(60, 60, 0, 0, 8, 8, imgBoard);

        // 3. load the pieces
        Map<String, Piece> piecesMap = loadPiecesFromCsv(board);
        List<Piece> piecesList = new ArrayList<>(piecesMap.values());

        // 4. create input handlers
        CommandQueue queue = new CommandQueue();
        CommandProducer producer = new CommandProducer();
        CursorPositionManager cursor = new CursorPositionManager(8, 8);
        KeyboardInputHandler input = new KeyboardInputHandler(producer, queue, cursor, board, piecesMap);

        // 5. UI
        GameUI ui = new GameUI(board, cursor, piecesMap);
        String[] names = PlayerNameDialog.askPlayerNames();
        ui.setPlayerNames(names[0], names[1]);

        // 6. create the game from pieces, board, UI and command queue
        Game game = new Game(piecesList, board, ui, queue);

        // 7. show the board with the game UI
        board.showWithGameUI(ui, input);

        // 8. run the game loop on a separate thread
        new Thread(game::run).start();
    }

    private Map<String, Piece> loadPiecesFromCsv(Board board) {
        Map<String, Piece> pieces = new HashMap<>();
        PieceFactory factory = new PieceFactory(board);

        Path csvPath = getCsvPath("board.csv");
        try {
            List<String> lines = Files.readAllLines(csvPath);
            int idCount = 1;
            for (int row = 0; row < lines.size(); row++) {
                String[] cells = lines.get(row).split(",");
                for (int col = 0; col < cells.length; col++) {
                    String type = cells[col].trim();
                    if (type.isEmpty()) continue;

                    String pieceId = type + "_" + idCount++;
                    int[] startCell = new int[]{row, col};
                    Piece piece = factory.loadPiece(pieceId, startCell, System.currentTimeMillis());
                    pieces.put(pieceId, piece);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read board.csv", e);
        }

        return pieces;
    }

    private Path getCsvPath(String name) {
        try {
            URL url = getClass().getClassLoader().getResource(name);
            if (url == null) throw new RuntimeException("Missing CSV: " + name);
            return Paths.get(url.toURI());
        } catch (Exception e) {
            throw new RuntimeException("Failed to locate CSV: " + name, e);
        }
    }
}