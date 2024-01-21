package sit.z523101.game;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class Screen extends JFrame {
    private final BufferedImage image;
    private BufferStrategy bs;
    public int[] pixels;
    private final Vector2d _ray_end;
    private final Vector2d _ray_dir;
    private final boolean debug;

    public Screen(boolean debug) {
        this.debug = debug;
        this.image = new BufferedImage(Config.Screen.SCREEN_WIDTH, Config.Screen.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.pixels = ((DataBufferInt) this.image.getRaster().getDataBuffer()).getData();
        this.setSize(Config.Screen.SCREEN_WIDTH * (this.debug ? 2 : 1), Config.Screen.SCREEN_HEIGHT);
        this.setResizable(false);
        this.setTitle("Rabbit Chasing Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.bs = getBufferStrategy();
        this._ray_end = new Vector2d();
        this._ray_dir = new Vector2d();
    }

    private void render_debug(Graphics g, Player player, Map map) {
        g.setColor(Color.BLACK);
        g.clearRect(Config.Screen.SCREEN_WIDTH, 0, Config.Screen.SCREEN_WIDTH, Config.Screen.SCREEN_HEIGHT);

        int ratioX = Config.Screen.SCREEN_WIDTH / map.width;
        int ratioY = Config.Screen.SCREEN_HEIGHT / map.height;

        int y = 0;
        int x;

        g.setColor(Color.RED);

        for (Map.MapElement[] line : map.grid) {
            x = 0;
            for (Map.MapElement element : line) {
                if (element != null) {
                    g.drawRect(Config.Screen.SCREEN_WIDTH + x * ratioX, y * ratioY, ratioX, ratioY);
                }
                x++;
            }
            y++;
        }
        g.setColor(Color.GREEN);
        g.drawOval((int) (Config.Screen.SCREEN_WIDTH + player.position.x * ratioX - ratioX / 2), (int) (player.position.y * ratioY - ratioY / 2), ratioX, ratioY);
        g.setColor(Color.BLUE);
        g.drawLine(
                (int) (Config.Screen.SCREEN_WIDTH + player.position.x * ratioX),
                (int) (player.position.y * ratioY),
                (int) (Config.Screen.SCREEN_WIDTH + player.position.x * ratioX + player.direction.x * Config.Screen.DEBUG_RENDER_DISTANCE * ratioX),
                (int) (player.position.y * ratioY + player.direction.y * Config.Screen.DEBUG_RENDER_DISTANCE * ratioY)
        );
    }

    private static final double DEG2RAD_HALF = Math.PI / 180D;

    public void update(Graphics g, Player player, Map map) {
        int n = 0;
        while (n < pixels.length / 2)
            pixels[n++] = Color.DARK_GRAY.getRGB();
        while (n < pixels.length)
            pixels[n++] = Color.gray.getRGB();

        if (this.debug) {
            this.render_debug(g, player, map);
            g.setColor(Color.ORANGE);
        }

        int ratioX = Config.Screen.SCREEN_WIDTH / map.width;
        int ratioY = Config.Screen.SCREEN_HEIGHT / map.height;

        // double view_angle = Math.sin(Config.Player.FOV * DEG2RAD_HALF);

        for (int x = 0; x < Config.Screen.SCREEN_WIDTH; x++) {
            double cameraX = 2 * x / (double) (Config.Screen.SCREEN_WIDTH) - 1;

            this._ray_dir.set(player.plane);
            this._ray_dir.mul(-cameraX);
            this._ray_dir.add(player.direction);
            // this._ray_dir.normalize();
            this._ray_end.set(player.position);

            Map.WallSide side = map.raycast(this._ray_end, this._ray_dir, Config.Screen.RENDER_DISTANCE, true);
            if (this.debug && x % 10 == 0)
                g.drawLine(
                        (int) (Config.Screen.SCREEN_WIDTH + player.position.x * ratioX),
                        (int) (player.position.y * ratioY),
                        (int) (Config.Screen.SCREEN_WIDTH + this._ray_end.x * ratioX),
                        (int) (this._ray_end.y * ratioY)
                );
            if (side == null)
                continue;

            double perpWallDist = (side == Map.WallSide.RIGHT || side == Map.WallSide.LEFT)
                    ? Math.abs((player.position.x - this._ray_end.x) / this._ray_dir.x)
                    : Math.abs((player.position.y - this._ray_end.y) / this._ray_dir.y);
            int lineHeight;
            if (perpWallDist > 0) lineHeight = Math.abs((int) (Config.Screen.SCREEN_HEIGHT / perpWallDist));
            else lineHeight = Config.Screen.SCREEN_HEIGHT;
            //calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + Config.Screen.SCREEN_HEIGHT / 2;
            if (drawStart < 0)
                drawStart = 0;
            int drawEnd = lineHeight / 2 + Config.Screen.SCREEN_HEIGHT / 2;
            if (drawEnd >= Config.Screen.SCREEN_HEIGHT)
                drawEnd = Config.Screen.SCREEN_HEIGHT - 1;
            //add a texture
            Map.MapElement texNum = Map.WallSide.LAST_ELEMENT;
            //x coordinate on the texture
            int texX = (int) (Map.WallSide.LAST_IMPACT * (texNum.texture.width - 1));
            //calculate y coordinate on texture
            for (int y = drawStart; y < drawEnd; y++) {
                int texY = (((y * 2 - Config.Screen.SCREEN_HEIGHT + lineHeight) << 6) / lineHeight) / 2;
                int color = texNum.texture.pixels[texX + texY * texNum.texture.width];
                color = (int)((double)(color & 0xFF) * side.light)
                    | ((int)((double)((color >> 8) & 0xFF) * side.light) << 8)
                    | ((int)((double)((color >> 16) & 0xFF) * side.light) << 16)
                    | color & 0xFF000000;
                // int
                this.pixels[x + y * (Config.Screen.SCREEN_WIDTH)] = color;
            }
        }
    }

    public void render(Player player, Map map) {
        if (this.bs == null) {
            this.createBufferStrategy(3);
            this.bs = getBufferStrategy();
            return;
        }
        Graphics g = this.bs.getDrawGraphics();
        this.update(g, player, map);
        g.drawImage(this.image, 0, 0, this.image.getWidth(), this.image.getHeight(), null);
        this.bs.show();
    }
}
