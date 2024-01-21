package sit.z523101.game;

import java.awt.event.KeyEvent;

public class Config {
    public static class Assets {
        public static final String IMAGE_ROOT = "/images/";
    }
    public static class KeyBinds {
        public static int MOVE_FORWARD = KeyEvent.VK_W;
        public static int MOVE_BACKWARD = KeyEvent.VK_S;
        public static int MOVE_LEFT = KeyEvent.VK_A;
        public static int MOVE_RIGHT = KeyEvent.VK_D;

        public static int ROTATE_LEFT = KeyEvent.VK_Q;
        public static int ROTATE_RIGHT = KeyEvent.VK_E;

        public static int RELOAD = KeyEvent.VK_R;
        public static int INTERACT = KeyEvent.VK_F;
    }

    public static class Player {
        public static double PLAYER_MOVE_SPEED = 2D;
        public static double PLAYER_ROTATION_SPEED = 2D;
        public static double MAX_ROTATION_SPEED = 1D;

        public static double START_POS_X = 5.5;
        public static double START_POS_Y = 5.5;
        public static double START_ROT = Math.PI / 2;

        public static double FOV = 50;
    }

    public static class Rabbit {
        public static double RABBIT_MOVE_SPEED = 1D;
    }

    public static class Screen {
        public static int SCREEN_WIDTH = 640;
        public static int SCREEN_HEIGHT = 480;
        public static double RENDER_DISTANCE = 10D;
        public static double DEBUG_RENDER_DISTANCE = 5D;
    }

    public static class Game {
        public static final double MAX_FPS = 60D;
        public static final double MAX_TPS = 60D;
        public static final long FPS_NS = (long) (1_000_000_000 / MAX_FPS);
        public static final long TPS_NS = (long) (1_000_000_000 / MAX_TPS);
        public static double MAP_TILE_SCALE = 1D;
    }
}
