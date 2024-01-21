package sit.z523101.game;

public class Player {
    public final Vector2d position;
    public final Vector2d plane;
    private double rotation;
    public final Vector2d direction;
    private int score;
    private final Vector2d _move_dest;

    public Player(double x, double y, double rotation) {
        this.position = new Vector2d(x, y);
        this.rotation = rotation;
        this.direction = Vector2d.angleToDirection(rotation);
        this.plane = new Vector2d();
        this.plane.cross(this.direction);
        this.score = 0;
        this._move_dest = new Vector2d();
    }

    public void update(Map map, Controller controller, double elapsed) {
        if (controller.rotation != 0) {
            this.rotation += controller.rotation * Config.Player.PLAYER_ROTATION_SPEED * elapsed;
            this.direction.fromAngle(this.rotation);
            this.plane.cross(this.direction);
        }

        this._move_dest.set(controller.velocity);
        this._move_dest.normalize();
        this._move_dest.mul(Config.Player.PLAYER_MOVE_SPEED * elapsed);
        this._move_dest.rotate(this.rotation - Config.Player.START_ROT);
        this._move_dest.add(this.position);

        // if (map.raytrace(this.position, this._move_dest))
        //     System.out.println("Collision detected!");
        this.position.set(this._move_dest);
        // System.out.println("X: " + this.position.x + " Y: " + this.position.y + " Rot: " + this.rotation);
    }
}
