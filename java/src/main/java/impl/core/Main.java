package impl.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javax.imageio.ImageIO;

import impl.command.CommandProducer;
import impl.command.CommandQueue;
import impl.input.CursorPositionManager;
import impl.input.KeyboardInputHandler;
import impl.model.*;
import impl.model.board.Board;
import impl.physics.Physics;
import impl.ui.GameUI;
import impl.ui.PlayerNameDialog;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. load the board image
        String boardPath = "C:\\KFChess\\CTD25\\java\\src\\main\\resources\\board.png";
        BufferedImage image = ImageIO.read(new File(boardPath));
        Img imgBoard = new Img(image);

        // 2. create the board
        Board board = new Board(60, 60, 0, 0, 8, 8, imgBoard);

        // יצירת תור הפקודות (ריק כרגע)
        CommandQueue commandQueue = new CommandQueue();

        // יצירת יצרן הפקודות עם בנאי ברירת מחדל (ללא פרמטרים)
        CommandProducer commandProducer = new CommandProducer();

        // יצירת מנהל הסמנים
        CursorPositionManager cursorManager = new CursorPositionManager(8, 8);

        // 3. load the pieces
        Map<String, Piece> pieces = new HashMap<>();
        Map<String, int[]> locationPieces = getInitialPieceLocations();
        for (Map.Entry<String, int[]> piece : locationPieces.entrySet()) {
            String pieceId = piece.getKey();
            int[] pieceCell = piece.getValue();

            Path baseFolder;
            try {
                URL resourceUrl = Main.class.getClassLoader().getResource("pieces");
                if (resourceUrl == null) {
                    throw new IllegalStateException("Resource folder 'pieces' not found");
                }
                baseFolder = Paths.get(resourceUrl.toURI());
            } catch (URISyntaxException e) {
                throw new RuntimeException("Failed to load resource folder 'pieces'", e);
            }
            Path spriteFolder = baseFolder.resolve(pieceId.substring(0, 2)).resolve("states").resolve("idle")
                    .resolve("sprites");
            Path pathMoves = baseFolder.resolve(pieceId.substring(0, 2)).resolve("moves.txt");

            Graphics graphic = createGraphics(spriteFolder);
            Physics physics = createPhysics(pieceCell, board);
            Moves moves = createMoves(pathMoves, pieceCell);
            State state = createState(graphic, physics, moves);
            Piece pieceObj = createPiece(pieceId, state);
            pieces.put(pieceId, pieceObj);
        }
        // 4. יצירת מנהל קלט מהמקלדת
        KeyboardInputHandler inputHandler = new KeyboardInputHandler(
                commandProducer,
                commandQueue,
                cursorManager,
                board,
                pieces);

        // 5. יצירת ממשק המשחק
        GameUI gameUI = new GameUI(board, cursorManager, pieces);
        String[] names = PlayerNameDialog.askPlayerNames();
        gameUI.setPlayerNames(names[0], names[1]);
        // 6. הצגת הלוח עם קלט
        System.out.println("מציגים לוח עם GameUI");
        board.showWithGameUI(gameUI, inputHandler);
    }

    private static Graphics createGraphics(Path spritesFolder) {
        Graphics graphic = new Graphics(spritesFolder, null, true, 5.0);
        return graphic;
    }

    private static Physics createPhysics(int[] cell, Board board) {
        return new Physics(cell, board, (int) (System.currentTimeMillis() / 1000000));
    }

    private static Moves createMoves(Path movesPath, int[] dims) {
        return new Moves(movesPath, dims);
    }

    private static State createState(Graphics graphic, Physics physics, Moves moves) {
        return new State(moves, graphic, physics);
    }

    public static Piece createPiece(String pieceId, State state) {
        Piece piece = new Piece(pieceId, state);
        return piece;
    }

    private static Map<String, int[]> getInitialPieceLocations() {
        Map<String, int[]> locations = new HashMap<>();
        // r n b q k b n r
        locations.put("RB_1", new int[] { 0, 0 });
        locations.put("NB_1", new int[] { 0, 1 });
        locations.put("BB_1", new int[] { 0, 2 });
        locations.put("QB", new int[] { 0, 3 });
        locations.put("KB", new int[] { 0, 4 });
        locations.put("BB_2", new int[] { 0, 5 });
        locations.put("NB_2", new int[] { 0, 6 });
        locations.put("RB_2", new int[] { 0, 7 });
        locations.put("PB_1", new int[] { 1, 0 });
        locations.put("PB_2", new int[] { 1, 1 });
        locations.put("PB_3", new int[] { 1, 2 });
        locations.put("PB_4", new int[] { 1, 3 });
        locations.put("PB_5", new int[] { 1, 4 });
        locations.put("PB_6", new int[] { 1, 5 });
        locations.put("PB_7", new int[] { 1, 6 });
        locations.put("PB_8", new int[] { 1, 7 });
        // locations.put("PB_8", new int[]{1, 7});
        // R N B Q K B N R
        locations.put("RW_1", new int[] { 7, 0 });
        locations.put("NW_1", new int[] { 7, 1 });
        locations.put("BW_1", new int[] { 7, 2 });
        locations.put("QW", new int[] { 7, 3 });
        locations.put("KW", new int[] { 7, 4 });
        locations.put("BW_2", new int[] { 7, 5 });
        locations.put("NW_2", new int[] { 7, 6 });
        locations.put("RW_2", new int[] { 7, 7 });
        locations.put("PW_1", new int[] { 6, 0 });
        locations.put("PW_2", new int[] { 6, 1 });
        locations.put("PW_3", new int[] { 6, 2 });
        locations.put("PW_4", new int[] { 6, 3 });
        locations.put("PW_5", new int[] { 6, 4 });
        locations.put("PW_6", new int[] { 6, 5 });
        locations.put("PW_7", new int[] { 6, 6 });
        locations.put("PW_8", new int[] { 6, 7 });
        return locations;
    }
}
