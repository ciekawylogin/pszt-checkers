package controller;

import model.CheckerColor;
import model.GameLevel;
import model.Model;
import model.Player;
import view.View;
import common.Mockup;
import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameFinishEvent;
import common.events.GameStartEvent;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    /// Model 
    private final Model model;
     
    /// Widok 
    private final View view;
    
    /// Kolejka blokujaca do przyjmowania zdarzen od widoku
    private final BlockingQueue <GameEvent> blocking_queue;
    
    /**
     * Konstruktor sterownika, do wolania w mainie programu
     * 
     * @param model Model
     * @param view Widok
     * @param blocking_queue Kolejka zdarzen od widoku
     */
    public Controller(final Model model, final View view, final BlockingQueue<GameEvent> blocking_queue) {
        this.model = model;
        this.view = view;
        this.blocking_queue = blocking_queue;
    }
    
    /**
     * Metoda inicjalizuje program (tj. inicjalizuje sterownik, a nastepnie, jezli trzeba, 
     * wola metody inicjalizujace model i widok), po czym wchodzi w g�owna pele programu.
     */
    public void go() {
    	//TODO
    	blocking_queue.add(new GameStartEvent("default", CheckerColor.WHITE, GameLevel.EASY));
    	
    	view.init();
    	while(true) {
    		processEvents();
    	}
    }

    /**
     * Pobiera obiekt z kolejki zdarzen (by  moze czekajac na niego), po czym obsluguje go
     * 
     * @TODO zastosowac wzorzec strategii
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
            	} else {
            		// kliknieto puste pole, ignorujemy
            		System.out.println("empty field clicked; ignoring");
            		
            	}
            	
            	// sprawdzenie warunku zwyciestwa przez jednego z graczy
            	Player player = model.checkIfAnyPlayerWon();
            	if (player != null) {
            		blocking_queue.add(new GameFinishEvent(false, player));
            	} 
            	// sprawdzenie warunku remisu
            	if (model.checkIfWithdraw()) {
            		blocking_queue.add(new GameFinishEvent(false, null));
            	}
            	
            } else if(event_class == GameStartEvent.class) {
            	// kliknieto przycisk rozpoczecia gry
            	//GameStartEvent game_start_event = (GameStartEvent) event;
            	model.startGame();
            	
            } else if(event_class == GameFinishEvent.class) {
            	//TODO
            	// zakonczyc wszystko
            	
            } else {
            	throw new RuntimeException("unrecognized event taken from blockingQueue");
            }
            // po kazdym zdarzeniu odswiezamy
        	refreshView();
		}
		catch(InterruptedException exception) {
			// nie powinno sie zdarzyc
			throw new RuntimeException("unexpected exception");
		}
	}

	/**
	 * Kaz widokowi sie odswiezyc
	 */
	private void refreshView() {
		Mockup mockup = model.getMockup();
		view.draw(mockup);
	}

}
 
