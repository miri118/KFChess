package impl.factory;

import impl.command.CommandProducer;
import impl.command.CommandQueue;
import impl.input.CursorPositionManager;
import impl.input.KeyboardInputHandler;
import impl.model.Img;
import impl.model.Piece;
import impl.model.board.Board;
import impl.ui.GameUI;
import impl.ui.PlayerNameDialog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class GameFactory {
    public void start() throws Exception {
        // טען תמונת לוח
        String boardPath = "src/main/resources/board.png";
        BufferedImage image = ImageIO.read(new File(boardPath));
        Img imgBoard = new Img(image);

        // צור לוח
        Board board = new Board(60, 60, 0, 0, 8, 8, imgBoard);

        // תור פקודות
        CommandQueue queue = new CommandQueue();
        CommandProducer producer = new CommandProducer();
        CursorPositionManager cursor = new CursorPositionManager(8, 8);

        // טען כלים לפי board.csv
        Map<String, Piece> pieces = loadPiecesFromCsv(board);

        // קלט
        KeyboardInputHandler input = new KeyboardInputHandler(producer, queue, cursor, board, pieces);

        // UI
        GameUI ui = new GameUI(board, cursor, pieces);
        String[] names = PlayerNameDialog.askPlayerNames();
        ui.setPlayerNames(names[0], names[1]);

        // הפעל משחק
        board.showWithGameUI(ui, input);
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
                    int[] startCell = new int[] { row, col };
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
