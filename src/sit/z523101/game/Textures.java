package sit.z523101.game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public enum Textures {
    WALL_TEXTURE("wall.png"),
    STONE_WALL_TEXTURE("greystone.png"),
    MOSSY_STONE_WALL_TEXTURE("mossy.png"),
    ;

    public final int[] pixels;
    public final int width;
    public final int height;

    Textures(String path) {

        try (InputStream stream = getClass().getResourceAsStream(Config.Assets.IMAGE_ROOT + path)) {
            if (stream == null)
                throw new IllegalArgumentException("Could not find texture: " + path);
            BufferedImage buffer = ImageIO.read(stream);
            if (buffer == null)
                throw new IllegalArgumentException("Could not load texture: " + path);
            // if ((this.height = buffer.getHeight()) != (this.width = buffer.getWidth()))
            //     throw new IllegalArgumentException("The given texture should be a square: " + this.width + "x" + this.height + " from " + path);
            this.width = buffer.getWidth();
            this.height = buffer.getHeight();
            pixels = new int[width * height];
            buffer.getRGB(0, 0, width, height, pixels, 0, width);
            System.out.println("Loaded texture " + this.name() + " of size " + this.width + "x" + this.width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
