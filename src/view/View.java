package view;

import common.events.GameEvent;
import common.Mockup;

import java.lang.UnsupportedOperationException;
import java.util.concurrent.BlockingQueue;

public class View {
    private BlockingQueue<GameEvent> blocking_queue;
    
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
	 * 
	 * W zależności od stanu gry podanego w makiecie może rysować różne ekrany (ekran powitalny,
	 * ekran gry etc.) - patrz dokumentacja klasy GameStateMockup
	 */
	public void draw(Mockup mockup) {
		// @TODO write me
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
