package controller;

import model.Model;
import view.View;
import common.events.GameEvent;

import java.util.concurrent.BlockingQueue;;

public class Controller {
    /// Model 
    private final Model model;
     
    /// Widok 
    private final View view;
    
    /// Kolejka blokująca do przyjmowania zdarzeń od widoku
    private final BlockingQueue <GameEvent> blocking_queue;
    
    /**
     * Konstruktor sterownika, do wołania w mainie programu
     * 
     * @param model Model
     * @param view Widok
     * @param blocking_queue Kolejka zdarzeń od widoku
     */
    public Controller(final Model model, final View view, final BlockingQueue<GameEvent> blocking_queue) {
        this.model = model;
        this.view = view;
        this.blocking_queue = blocking_queue;
    }
    
    /**
     * Metoda inicjalizuje program (tj. inicjalizuje sterownik, a następnie, jeśli trzeba, 
     * woła metody inicjalizujące model i widok), po czym wchodzi w główną pętlę programu.
     */
    public void go() {
    	view.init();
    	while(true) {
    		processEvents();
    	}
    }

    /**
     * Pobiera obiekt z kolejki zdarzeń (być może czekając na niego), po czym obsługuje go
     */
	private void processEvents() {
		try {
			GameEvent event = this.blocking_queue.take();
			// @TODO: zrob cos z event 
		}
		catch(InterruptedException exception)
		{
			// nie powinno sie zdarzyć
			throw new RuntimeException("unexpected exception");
		}
	}

}
 
