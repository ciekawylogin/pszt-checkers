package controller;

import model.Model;
import view.View;
import common.Mockup;
import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameStartEvent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    	blocking_queue.add(new GameStartEvent());
    	
    	view.init();
    	while(true) {
    		processEvents();
    	}
    }

    /**
     * Pobiera obiekt z kolejki zdarzeń (być może czekając na niego), po czym obsługuje go
     * 
     * @TODO zastosować wzorzec strategii
     */
	private void processEvents() {
		try {
			GameEvent event = this.blocking_queue.take();
            Class<? extends GameEvent> event_class = event.getClass();
            if(event_class == FieldClickEvent.class) {
            	// kliknieto pole
            	FieldClickEvent field_click_event = (FieldClickEvent)event;
            	int x = field_click_event.getFieldX();
            	int y = field_click_event.getFieldY();
            	if(model.isCheckerSelected(x, y)) {
            		model.unselectChecker();
            	} else if(model.isCurrentPlayerCheckerOnPosition(x, y)) {
            		if(model.isAnyCheckerSelected()) {
                		model.unselectChecker();
            		}
            		model.selectChecker(x, y);
            	} else if(model.isAnyCheckerSelected()) {
            		model.moveSelectedCheckerTo(x, y);
            		model.unselectChecker();
            	} else {
            		// kliknieto puste pole (?)
            		// ignorujemy
            		System.out.println("empty field clicked; ignoring");
            		
            	}
            	if(model.hasPlayer1Won()) {
            		// @TODO czy trzeba to obsluzyc???
            	}
            	refreshView();
            	if(model.isAITurn())
            	{
            		model.makeAIMove();
            	}
            } else if(event_class == GameStartEvent.class) {
            	// kliknieto przycisk rozpoczecia gry
            	GameStartEvent game_start_event = (GameStartEvent) event;
            	model.startGame();
            } else {
            	/// ????
            }
            // po kazdym zdarzeniu odswiezamy
        	refreshView();
		}
		catch(InterruptedException exception) {
			// nie powinno sie zdarzyć
			throw new RuntimeException("unexpected exception");
		}
	}

	/**
	 * Każe widokowi się odświeżyć
	 */
	private void refreshView() {
		Mockup mockup = model.getMockup();
		view.draw(mockup);
	}

}
 
