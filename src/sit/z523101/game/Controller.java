package sit.z523101.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
    public final Vector2d velocity;
    public double rotation;
    private int key_mask;

    public Controller() {
        velocity = new Vector2d();
        this.rotation = 0;
        this.key_mask = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Config.KeyBinds.MOVE_FORWARD) {
            if ((0b000001 & this.key_mask) == 0) {
                this.velocity.y += 1;
                this.key_mask |= 0b000001;
            }
        } else if (key == Config.KeyBinds.MOVE_BACKWARD) {
            if ((0b000010 & this.key_mask) == 0) {
                this.velocity.y -= 1;
                this.key_mask |= 0b000010;
            }
        } else if (key == Config.KeyBinds.MOVE_LEFT) {
            if ((0b000100 & this.key_mask) == 0) {
                this.velocity.x += 1;
                this.key_mask |= 0b000100;
            }
        } else if (key == Config.KeyBinds.MOVE_RIGHT) {
            if ((0b001000 & this.key_mask) == 0) {
                this.velocity.x -= 1;
                this.key_mask |= 0b001000;
            }
        } else if (key == Config.KeyBinds.ROTATE_LEFT) {
            if ((0b010000 & this.key_mask) == 0) {
                this.rotation -= 1;
                this.key_mask |= 0b010000;
            }
        } else if (key == Config.KeyBinds.ROTATE_RIGHT) {
            if ((0b100000 & this.key_mask) == 0) {
                this.rotation += 1;
                this.key_mask |= 0b100000;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == Config.KeyBinds.MOVE_FORWARD) {
            if ((0b000001 & this.key_mask) != 0) {
                this.velocity.y -= 1;
                this.key_mask ^= 0b000001;
            }
        } else if (key == Config.KeyBinds.MOVE_BACKWARD) {
            if ((0b000010 & this.key_mask) != 0) {
                this.velocity.y += 1;
                this.key_mask ^= 0b000010;
            }
        } else if (key == Config.KeyBinds.MOVE_LEFT) {
            if ((0b000100 & this.key_mask) != 0) {
                this.velocity.x -= 1;
                this.key_mask ^= 0b000100;
            }
        } else if (key == Config.KeyBinds.MOVE_RIGHT) {
            if ((0b001000 & this.key_mask) != 0) {
                this.velocity.x += 1;
                this.key_mask ^= 0b001000;
            }
        } else if (key == Config.KeyBinds.ROTATE_LEFT) {
            if ((0b010000 & this.key_mask) != 0) {
                this.rotation += 1;
                this.key_mask ^= 0b010000;
            }
        } else if (key == Config.KeyBinds.ROTATE_RIGHT) {
            if ((0b100000 & this.key_mask) != 0) {
                this.rotation -= 1;
                this.key_mask ^= 0b100000;
            }
        }
    }
}
