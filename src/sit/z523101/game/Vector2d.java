package sit.z523101.game;

public class Vector2d {
    public double x;
    public double y;

    public Vector2d() {
        this(0);
    }
    public Vector2d(double value) {
        this(value, value);
    }
    public Vector2d(Vector2d vec) {
        this(vec.x, vec.y);
    }
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }
    public void set(Vector2d vec) {
        this.x = vec.x;
        this.y = vec.y;
    }
    public void mul(double value) {
        this.x *= value;
        this.y *= value;
    }
    public void mul(Vector2d vec) {
        this.x *= vec.x;
        this.y *= vec.y;
    }
    public void add(double value) {
        this.x += value;
        this.y += value;
    }
    public void add(Vector2d vec) {
        this.x += vec.x;
        this.y += vec.y;
    }
    public void sub(double value) {
        this.x -= value;
        this.y -= value;
    }
    public void sub(Vector2d vec) {
        this.x -= vec.x;
        this.y -= vec.y;
    }
    public void rotate(double angle) {
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        double temp = this.x;
        this.x = this.x * c - this.y * s;
        this.y = temp * s + this.y * c;
    }
    public void fromAngle(double angle) {
        this.x = Math.cos(angle);
        this.y = Math.sin(angle);
    }
    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y);
    }
    public double distance(Vector2d vec) {
        double dx = this.x - vec.x;
        double dy = this.y - vec.y;

        return Math.sqrt(dx*dx + dy*dy);
    }
    public void cross(Vector2d vec) {
        this.x = vec.y;
        this.y = -vec.x;
    }
    public final double dot(Vector2d v1) {
        return (this.x*v1.x + this.y*v1.y);
    }
    public final double angle(Vector2d v1) {
        double vDot = this.dot(v1) / (this.length()*v1.length());

        if(vDot < -1.0)
            vDot = -1.0;
        if(vDot > 1.0)
            vDot = 1.0;
        return(Math.acos(vDot));

    }

    public void normalize() {
        double length = this.length();

        if (length != 0) {
            this.x /= length;
            this.y /= length;
        }
    }

    public static Vector2d angleToDirection(double angle) {
        return new Vector2d(Math.cos(angle), Math.sin(angle));
    }
}
