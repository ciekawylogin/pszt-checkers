import model.Model;
import view.View;
import controller.Controller;
import common.events.GameEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Glowna klasa programu
 */
public final class PsztCheckers {
    public static void main(String[] args) {
    	
    	// disable console output
		System.setOut(new PrintStream(new OutputStream() {
		
		    @Override
		    public void write(int arg0) throws IOException {
		
		    }
		}));
    	
        Model model = new Model();
        BlockingQueue<GameEvent> blocking_queue = new LinkedBlockingQueue<GameEvent>();
        View view = new View(blocking_queue);
        Controller controller = new Controller(model, view, blocking_queue);
        controller.go();
    }
}