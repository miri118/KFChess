package impl.factory;

import java.nio.file.Path;
import impl.model.Graphics;

public class GraphicsFactory {
    public Graphics load(Path spritesFolder, boolean loop, double fps) {
        return new Graphics(spritesFolder, loop, fps);
    }
}