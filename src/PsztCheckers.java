import model.Model;
import view.View;
import controller.Controller;
import common.events.GameEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Glowna klasa programu
 */
public final class PsztCheckers {
    public static void main(String[] args) {
        Model model = new Model();
        BlockingQueue<GameEvent> blocking_queue = new LinkedBlockingQueue<GameEvent>();
        View view = new View(blocking_queue);
        Thread thread = new Thread(view);
        thread.start();
        Controller controller = new Controller(model, view, blocking_queue);
        controller.go();
    }
}