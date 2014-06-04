package controller;

import model.Model;
import model.Player;
import view.View;
import common.Mockup;
import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameStartEvent;
import common.events.GameFinishEvent;

import java.util.concurrent.BlockingQueue;

public class Controller {
	public static Controller instance; // debug only, remove it from final version
	
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
        this.instance = this;
    }

    /**
     * Metoda inicjalizuje program (tj. inicjalizuje sterownik, a nastepnie, jezli trzeba,
     * wola metody inicjalizujace model i widok), po czym wchodzi w glowna pele programu.
     */
    public void go() {
        Thread thread = new Thread(view);
        thread.start();
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
                final GameStartEvent gameStartEvent = (GameStartEvent)event;
                model.startGame(gameStartEvent.getPlayerName(), gameStartEvent.getGameLevel(), 
                        gameStartEvent.getCheckerColor());
                refreshView();
                model.makeAIMove();
                refreshView();
                break;
                
            case GAME_FINISH:
                refreshView();
                break;
                
            case FIELD_CLICK:
                // kliknieto pole
                final FieldClickEvent fieldClickEvent = (FieldClickEvent)event;
                boolean isHumanPlayerMoveComplete = 
                        model.processHumanMove(fieldClickEvent.getFieldX(), fieldClickEvent.getFieldY());
                refreshView();
                checkEndGameConditions();
                
                if(isHumanPlayerMoveComplete) {
                    makeAIMove();
                    checkEndGameConditions();
                }
                break;
                
            case PROGRAM_QUIT:
                System.exit(0);
                break;
                
            default:
                throw new RuntimeException("unrecognized event taken from blockingQueue");
            
            }
            
            // po kazdym zdarzeniu odswiezamy
            //refreshView();
            
        }
        catch(InterruptedException exception) {
            // nie powinno sie zdarzyc
            throw new RuntimeException("unexpected exception");
        }
    }

    /**
     * Kaz widokowi sie odswiezyc
     */
    public void refreshView() {
        Mockup mockup = model.getMockup();
        view.draw(mockup);
    }
    
    /**
     * Wykonanie ruchu CPU
     */
    private void makeAIMove() {
        boolean isCPUMoveComplete = false;
        
        while(!isCPUMoveComplete && model.isAITurn()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isCPUMoveComplete = model.makeAIMove();
            refreshView();
            isCPUMoveComplete |= checkEndGameConditions();
        }
    }
    
    
    /**
     * Sprawdza czy ktorys z graczy wygral badz czy nastapil remis
     */
    private boolean checkEndGameConditions() {
        // sprawdzenie warunku zwyciestwa przez jednego z graczy
        final Player player = model.checkIfAnyPlayerWon();
        if (player != null) {
            blocking_queue.add(new GameFinishEvent(false, player));
            return true;
        } 
        // sprawdzenie warunku remisu
        else if (model.checkIfWithdraw()) {
            blocking_queue.add(new GameFinishEvent(false, null));
            return true;
        }
        
        return false;
    }
}
