package sit.z523101.game;

public class Map {
    public enum MapElement {
        WALL(Textures.WALL_TEXTURE),
        STONE_WALL(Textures.STONE_WALL_TEXTURE),
        MOSSY_STONE_WALL(Textures.MOSSY_STONE_WALL_TEXTURE),
        ;
        public final Textures texture;
        MapElement(Textures texture) {
            this.texture = texture;
        }

        private static final MapElement[] VALUES = MapElement.values();
    }
    public final MapElement[][] grid;
    public final int width;
    public final int height;

    public Map(int[][] elements) {
        if ((this.height = elements.length) == 0)
            throw new IllegalArgumentException("You cannot provide an empty map");
        this.width = elements[0].length;
        this.grid = new MapElement[this.height][];

        for (int y = 0; y < this.height; y++) {
            int[] line = elements[y];
            if (line == null || line.length != this.width)
                throw new IllegalArgumentException("All lines must have the same size");

            MapElement[] newline = this.grid[y] = new MapElement[this.width];
            for (int x = 0; x < this.width; x++)
                newline[x] = line[x] == 0 ? null : MapElement.VALUES[line[x] - 1];
        }
    }

    public enum WallSide {
        FRONT(0, 1),
        BACK(0, -1),
        RIGHT(1, 0),
        LEFT(-1, 0),
        ;
        public final Vector2d normal;

        public double light;

        public static double AMBIENT_LIGHT = 0.5D;
        public static final Vector2d DIRECTIONAL_LIGHT = new Vector2d(1, 2);
        private static final Vector2d NORMALIZED_DIR_LIGHT = new Vector2d();

        private static final WallSide[] VALUES = values();

        static {
            updateLight();
        }

        public static double LAST_IMPACT = 0D;
        public static MapElement LAST_ELEMENT = null;

        WallSide(double normalX, double normalY) {
            this.normal = new Vector2d(normalX, normalY);
            this.normal.normalize();
        }

        public static void updateLight() {
            NORMALIZED_DIR_LIGHT.set(DIRECTIONAL_LIGHT);
            NORMALIZED_DIR_LIGHT.add(AMBIENT_LIGHT);
            NORMALIZED_DIR_LIGHT.normalize();
            for (WallSide side : VALUES)
                side.update();
        }

        private void update() {
            double angle = NORMALIZED_DIR_LIGHT.angle(this.normal) / Math.PI;
            if (angle < 0)
                angle += 1;
            else if (angle > 1)
                angle -= 1;
            angle *= 1 - AMBIENT_LIGHT;
            this.light = AMBIENT_LIGHT + angle;
            System.out.println(this.name() +  this.light);
        }
    }

    public WallSide raycast(Vector2d start, Vector2d dir, double max_dist, boolean set) {
        double sx = Math.sqrt(1 + (dir.y / dir.x) * (dir.y / dir.x));
        double sy = Math.sqrt(1 + (dir.x / dir.y) * (dir.x / dir.y));

        double xLength, yLength;

        int xPos = (int) start.x;
        int yPos = (int) start.y;

        int xStep, yStep;

        if (dir.x < 0) {
            xStep = -1;
            xLength = (start.x - xPos) * sx;
        } else {
            xStep = 1;
            xLength = (xPos + 1 - start.x) * sx;
        }

        if (dir.y < 0) {
            yStep = -1;
            yLength = (start.y - yPos) * sy;
        } else {
            yStep = 1;
            yLength = (yPos + 1 - start.y) * sy;
        }

        double length = 0;
        boolean xSide;
        WallSide side = null;

        while (length < max_dist)
        {
            if (xLength < yLength) {
                xPos += xStep;
                length = xLength;
                xLength += sx;
                xSide = true;
            } else {
                yPos += yStep;
                length = yLength;
                yLength += sy;
                xSide = false;
            }

            if (xPos >= 0 && xPos < this.width && yPos >= 0 && yPos < this.height) {
                if ((WallSide.LAST_ELEMENT = this.grid[yPos][xPos]) != null) {
                    if (xSide) {
                        // WallSide.LAST_IMPACT = Math.abs(yLength - yPos);
                        WallSide.LAST_IMPACT = (start.y + ((xPos - start.x + (1 - xStep) / 2.) / dir.x) * dir.y);
                        side = xStep == -1 ? WallSide.FRONT : WallSide.BACK;
                        if (set) {
                            start.y = WallSide.LAST_IMPACT;
                            start.x = xPos + (1 - xStep) / 2.;
                        }
                    } else {
                        // WallSide.LAST_IMPACT = Math.abs(xLength - xPos);
                        WallSide.LAST_IMPACT = (start.x + ((yPos - start.y + (1 - yStep) / 2.) / dir.y) * dir.x);
                        side = yStep == -1 ? WallSide.RIGHT : WallSide.LEFT;
                        if (set) {
                            start.y = yPos + (1 - yStep) / 2.;
                            start.x = WallSide.LAST_IMPACT;
                        }
                    }
                    WallSide.LAST_IMPACT -= Math.floor(WallSide.LAST_IMPACT);
                    break;
                }
            }
        }
        return side;
    }
}
