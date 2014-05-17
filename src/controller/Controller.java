package controller;

import model.CheckerColor;
import model.GameLevel;
import model.Model;
import model.Player;
import view.View;
import common.Mockup;
import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameStartEvent;
import common.events.GameFinishEvent;
import common.events.ProgramQuitEvent;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller {
    // Model
    private final Model model;

    // Widok
    private final View view;

    // Kolejka blokujaca do przyjmowania zdarzen od widoku
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
            
            EventClassIdentificator classId = (EventClassIdentificator) controller.EventClassIdentificator.getId(event_class);
            
            switch(classId) {
            
            case GAME_START:
                // kliknieto przycisk rozpoczecia gry
                GameStartEvent gameStartEvent = (GameStartEvent)event;
                model.startGame(gameStartEvent.getPlayerName(), gameStartEvent.getGameLevel(), 
                        gameStartEvent.getCheckerColor());
                break;
                
            case GAME_FINISH:
                //TODO
                // powrot do glownego menu
                
                // tymczasowo:
                System.out.println("koniec.");
                System.exit(0);
                break;
                
            case FIELD_CLICK:
                // kliknieto pole
                FieldClickEvent fieldClickEvent = (FieldClickEvent)event;
                final int x = fieldClickEvent.getFieldX();
                final int y = fieldClickEvent.getFieldY();
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
                    fieldClickEvent = null;
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
                
                break;
                
            case PROGRAM_QUIT:
                System.exit(0);
                break;
                
            default:
                throw new RuntimeException("unrecognized event taken from blockingQueue");
            
            }
            
            // po kazdym zdarzeniu odswiezamy
            System.out.println("refresh");
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
