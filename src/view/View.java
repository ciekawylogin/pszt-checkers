package view;

import common.events.GameEvent;
import common.Mockup;

import java.lang.UnsupportedOperationException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

// tylko do debugu
import java.util.logging.Logger;
import java.util.logging.Level;

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
		//throw new UnsupportedOperationException("Not yet implemented");
	}
	
	/**
	 * Wyświetla stan gry na podstawie podanej makiety
	 * 
	 * W zależności od stanu gry podanego w makiecie może rysować różne ekrany (ekran powitalny,
	 * ekran gry etc.) - patrz dokumentacja klasy GameStateMockup
	 */
	public void draw(Mockup mockup) {

        // @TODO na razie jest wersja testowa, zmienic na cos docelowego
        System.out.println("Stan gry: " + mockup.getGameState());
        System.out.println("Gracz 1: " + mockup.getPlayer(0));      
        System.out.println("Gracz 2: " + mockup.getPlayer(1));
        System.out.println("Pola: ");       
        for(int x=0; x<8; ++x)
        {
            for(int y=0; y<8; ++y)  
            {
                System.out.print(" " + mockup.getField(x, y));
            }
            System.out.print("\n");
        }
		// @TODO write me
		Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "test");
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
}
