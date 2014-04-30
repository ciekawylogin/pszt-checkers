package view;

import common.events.GameEvent;
import common.Mockup;

import java.lang.UnsupportedOperationException;
import java.util.concurrent.BlockingQueue;

public class View {
    private BlockingQueue blocking_queue;
    
    public View(BlockingQueue<GameEvent> blocking_queue) {
    	this.blocking_queue = blocking_queue;
    	// @TODO utworzyć elementy widoku
    }

    /**
     * Inicjuje widok, np. odpala okno gry (na razie nic nie wyswietla - bezposrednio potem
     * zostanie zawolana metoda draw)
     */
	public void init() {
		// @TODO write me
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * Wyświetla stan gry na podstawie podanej makiety
	 */
	public void draw(Mockup mockup) {
		// @TODO write me
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
