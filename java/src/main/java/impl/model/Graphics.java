package impl.model;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;

import impl.command.Command;
import impl.enums.CommandType;
import impl.model.board.Board;

public class Graphics {
    private Path spritesFolder;
    private Board board;
    private boolean loop;
    private double fps;

    /**
     * Initialize graphics with sprites folder, cell size, loop setting, and FPS.
     */
    public Graphics(Path spritesFolder, Board board, boolean loop, double fps) {
        this.spritesFolder = spritesFolder;
        this.board = board;
        this.loop = loop;
        this.fps = fps;
    }

    /**
     * Create a shallow copy of the graphics object.
     */
    public Graphics copy() {
        return new Graphics(spritesFolder, board, loop, fps);
    }

    /**
     * Reset the animation with a new command.
     */
    public void reset(Command cmd) {
        CommandType type = cmd.getType();
        Path newSpriteFolder = spritesFolder.resolve(type.toString().toLowerCase());
        setSpritesFolder(newSpriteFolder);
    }

    /**
     * Advance animation frame based on game-loop time, not wall time.
     */
    public void update(int nowMs) {
    }

    /**
     * Get the current frame image.
     */
    public Img getImg() {
        Path fullPath = spritesFolder;

        System.out.println("Loading from: " + fullPath.toAbsolutePath());


        File folder = fullPath.toFile();
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".png") || name.endsWith(".jpg"));

        if (files == null || files.length == 0) {
            throw new IllegalStateException("No image frames found in sprites folder: " + fullPath);
        }

        Arrays.sort(files);

        long now = System.currentTimeMillis();
        int totalFrames = files.length;
        double frameDurationMs = 1000.0 / fps;
        int currentFrame = (int) ((now / frameDurationMs) % totalFrames);

        if (!loop && currentFrame >= totalFrames) {
            currentFrame = totalFrames - 1;
        }

        String path = files[currentFrame].getAbsolutePath();
        return new Img(null).read(path);
    }

    // Getters and setters
    public Path getSpritesFolder() {
        return spritesFolder;
    }

    public void setSpritesFolder(Path spritesFolder) {
        this.spritesFolder = spritesFolder;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }
}