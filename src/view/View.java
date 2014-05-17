package view;

import java.util.Scanner;

import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameStartEvent;
import common.CheckerMockup;
import common.Mockup;
import model.CheckerColor;
import model.GameLevel;
import model.Model;

import java.util.concurrent.BlockingQueue;
//import java.util.logging.Logger;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



// tylko do debugu
import java.util.logging.Logger;
import java.util.logging.Level;

public class View extends Application implements Runnable {
    static private BlockingQueue<GameEvent> blocking_queue;
    @FXML private Button start;
    @FXML private Button exit;
    @FXML private ChoiceBox difficulty;
    @FXML private ChoiceBox color;
    @FXML private TextField name;

    @FXML
    protected void beginGame(ActionEvent event) {
        if(blocking_queue != null) {
            blocking_queue.add(new GameStartEvent("default", CheckerColor.WHITE, GameLevel.EASY));
        } else {
            throw new RuntimeException("View.beginGame - blockingQueue is null");
        }
        System.out.println("Start game");
    }

    @FXML
    protected void exitProgram(ActionEvent event) {
        System.out.println("Exit");
    }

    public View() {}

    public View(BlockingQueue<GameEvent> blocking_queue) {
        this.blocking_queue = blocking_queue;
        //TODO utworzyc elementy widoku
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            AnchorPane page = (AnchorPane) FXMLLoader.load(View.class.getResource("menu.fxml"));
            Scene scene = new Scene(page);
            scene.getStylesheets().add(View.class.getResource("stylesheet.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Checkers");
            primaryStage.show();
        }
        catch (Exception ex) {
            Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Inicjuje widok, np. odpala okno gry (na razie nic nie wyswietla - bezposrednio potem
     * zostanie zawolana metoda draw)
     */
    public void init() {
        //TODO write me
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Wyswietla stan gry na podstawie podanej makiety
     *
     * W zaleznosci od stanu gry podanego w makiecie moze rysowac rozne ekrany (ekran powitalny,
     * ekran gry etc.) - patrz dokumentacja klasy GameStateMockup
     */
    public void draw(Mockup mockup) {
        //TODO

        System.out.println("--------------------------------");
        System.out.println("game state: "+mockup.getGameState());
        System.out.println("player 1: "+mockup.getPlayer(0)+" player 2: "+mockup.getPlayer(1));
        System.out.println("board:");
        System.out.print("   ");
        for(int i=0; i < Model.getBoardSize(); ++i) {
            System.out.print(" "+i+"  ");
        }

        System.out.println();
        for(int i=0; i < Model.getBoardSize(); ++i) {
            System.out.print(" "+i+" ");
            for(int j=0; j< Model.getBoardSize(); ++j) {
                //System.out.print("\t" + mockup.getField(j, i).isSelected() + "\t" + mockup.getField(j, i).getCheckerMockup());
                if(mockup.getField(j, i).getCheckerMockup()== CheckerMockup.EMPTY_FIELD) {
                    if(mockup.getField(j, i).isSelected()) {
                        System.out.print("]_[ ");
                    } else {
                        System.out.print("[_] ");
                    }
                } else if(mockup.getField(j, i).getCheckerMockup()== CheckerMockup.BLACK_CHECKER) {
                    if(mockup.getField(j, i).isSelected()) {
                        System.out.print("]@[ ");
                    } else {
                        System.out.print("[@] ");
                    }
                } else if(mockup.getField(j, i).getCheckerMockup()== CheckerMockup.BLACK_QUEEN) {
                    if(mockup.getField(j, i).isSelected()) {
                        System.out.print("]2[ ");
                    } else {
                        System.out.print("[2] ");
                    }
                } else if(mockup.getField(j, i).getCheckerMockup()== CheckerMockup.WHITE_CHECKER) {
                    if(mockup.getField(j, i).isSelected()) {
                        System.out.print("]#[ ");
                    } else {
                        System.out.print("[#] ");
                    }
                } else if(mockup.getField(j, i).getCheckerMockup()== CheckerMockup.WHITE_QUEEN) {
                    if(mockup.getField(j, i).isSelected()) {
                        System.out.print("]3[ ");
                    } else {
                        System.out.print("[3] ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        int y = in.nextInt();
        blocking_queue.add(new FieldClickEvent(x, y));
    }

    @Override
    public void run() {
        launch();
    }

    /**
     * Funkcja do celow testowych (tymczasowo)
     */
    public void displayEnd(final String playerName) {
        System.out.println("KONIEC GRY");
        System.out.println("wygral gracz: "+playerName);
    }
}
