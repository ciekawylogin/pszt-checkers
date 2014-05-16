package view;

import java.util.Scanner;

import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.Mockup;
import model.Model;

import java.io.IOException;
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
		// @TODO write me
		//Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, "test");
		//throw new UnsupportedOperationException("Not yet implemented");
		
		System.out.println("----------------");
		System.out.println("game state: "+mockup.getGameState());
		System.out.println("player 1: "+mockup.getPlayer(0)+" player 2: "+mockup.getPlayer(1));
		System.out.println("board:");
		for(int i=0; i < Model.getBoardSize(); ++i) {
			for(int j=0; j< Model.getBoardSize(); ++j) {
				System.out.print("\t" + mockup.getField(j, i).isSelected() + "\t" + mockup.getField(j, i).getCheckerMockup());
			}
			System.out.println();
		}
		
		System.out.println("----------------");

		Scanner in = new Scanner(System.in);
		int x = in.nextInt();
		int y = in.nextInt();
		blocking_queue.add(new FieldClickEvent(x, y));
	}
	
}
