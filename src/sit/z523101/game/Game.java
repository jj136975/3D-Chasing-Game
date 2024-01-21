package sit.z523101.game;

public class Game implements Runnable {
    private final Map map;
    private final Player player;
    private final Controller controller;
    private final Screen screen;

    private final Thread thread;
    private boolean running;

    public Game() {
        this.map = new Map(new int[][]{
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 2, 2, 3, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 3, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 2, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        });
        this.controller = new Controller();
        this.player = new Player(Config.Player.START_POS_X, Config.Player.START_POS_Y, Config.Player.START_ROT);
        this.screen = new Screen(true);

        thread = new Thread(this);
        start();
    }


    private synchronized void start() {
        running = true;
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long delta_fps = 0;
        long delta_tps = 0;
        this.screen.requestFocus();
        this.screen.addKeyListener(this.controller);
        while(running) {
            long now = System.nanoTime();
            delta_fps += now - lastTime;
            delta_tps += now - lastTime;
            lastTime = now;

            if (delta_tps >= Config.Game.TPS_NS) {
                double elapsed = (double)delta_tps / 1_000_000_000;
                // @Todo: Update map entities
                this.player.update(this.map, this.controller, elapsed);
                delta_tps = 0L;
            }
            if (delta_fps >= Config.Game.FPS_NS) {
                this.screen.render(this.player, this.map);
                delta_fps = 0L;
            }
        }
    }
}
