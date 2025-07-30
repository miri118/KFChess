package impl.model;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import impl.command.Command;
import impl.enums.CommandType;
public class Graphics {
     private Path spritesFolder;
    private final boolean loop;
    private final double fps;
    private final double frameDurationMs;
    private final List<BufferedImage> frames = new ArrayList<>();
    private long startMs;

    public Graphics(Path spritesFolder, boolean loop, double fps) {
        this.spritesFolder = spritesFolder;
        this.loop = loop;
        this.fps = fps;
        this.frameDurationMs = 1000.0 / fps;
        this.startMs = System.currentTimeMillis();
        loadFrames();
    }

    public Graphics copy() {
        return new Graphics(spritesFolder, loop, fps);
    }

    public void reset(Command cmd) {
        CommandType type = cmd.getType();
        this.spritesFolder = spritesFolder.resolve(type.toString().toLowerCase());
        this.startMs = System.currentTimeMillis();
        loadFrames();
    }

    public void update(int nowMs) {
        // intentionally left empty – for future animation sync
    }

    public Img getImg() {
        if (frames.isEmpty()) {
            throw new IllegalStateException("No image frames loaded from: " + spritesFolder);
        }

        long now = System.currentTimeMillis();
        int totalFrames = frames.size();
        int currentFrame = (int) ((now - startMs) / frameDurationMs) % totalFrames;

        if (!loop && currentFrame >= totalFrames) {
            currentFrame = totalFrames - 1;
        }

        BufferedImage image = frames.get(currentFrame);
        return new Img(image);
    }

    private void loadFrames() {
        frames.clear();
        File folder = spritesFolder.toFile();

        File[] files = folder.listFiles((dir, name) ->
                name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));

        if (files == null || files.length == 0) {
            throw new IllegalStateException("No sprite images found in: " + spritesFolder);
        }

        Arrays.sort(files);
        for (File file : files) {
            try {
                frames.add(ImageIO.read(file));
            } catch (Exception e) {
                System.err.println("Failed to load image: " + file.getAbsolutePath());
            }
        }
    }

    public boolean isLoop() {
        return loop;
    }

    public double getFps() {
        return fps;
    }

    public Path getSpritesFolder() {
        return spritesFolder;
    }
}