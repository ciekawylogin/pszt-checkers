package view;

import java.util.Scanner;

import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameStartEvent;
import common.events.ProgramQuitEvent;
import common.CheckerMockup;
import common.Mockup;
import model.CheckerColor;
import model.GameLevel;
import model.Model;

import java.util.concurrent.BlockingQueue;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfx.messagebox.MessageBox;


// tylko do debugu
import java.util.logging.Logger;
import java.util.logging.Level;

public class View extends Application implements Runnable {
    static private BlockingQueue<GameEvent> blocking_queue = null;
    @FXML private Button start;
    @FXML private Button exit;
    @FXML private ChoiceBox difficulty;
    @FXML private ChoiceBox color;
    @FXML private TextField name;

    @FXML
    protected void beginGame(ActionEvent event) {
        CheckerColor checkerColor = null;
        GameLevel gameLevel = null;
        
        if (name.getText().equals("")) {
            MessageBox.show(new Stage(),
                "Your name must not be empty", "Warning", MessageBox.OK);
            System.out.println("pusto");
            
        } else if(blocking_queue != null) {
            
            checkerColor = getCheckerColorFromChoiceBox();
            gameLevel = getGameLevelFromChoiceBox();
            
            blocking_queue.add(new GameStartEvent(name.getText(), checkerColor, gameLevel));
            System.out.println(name.getText());
            System.out.println(color.getValue());
            System.out.println(difficulty.getValue());
            
        } else {
            throw new RuntimeException("View.beginGame - blockingQueue is null");
        }
        System.out.println("Start game");
    }
    
    /**
     * Zwraca kolor ze stringa ChoiceBoxa
     * @return CheckerColor
     */
    private CheckerColor getCheckerColorFromChoiceBox() {
        
        ColorIdentificator colorId = ColorIdentificator.getId((String)color.getValue());
        
        switch (colorId) {
        case WHITE_COLOR:
            return CheckerColor.WHITE;
        case BLACK_COLOR:
            return CheckerColor.BLACK;
        default:
            throw new RuntimeException("unrecognized color");
        }
    }
    
    /**
     * Zwraca poziom gry ze stringa ChoiceBoxa
     * @return GameLevel
     */
    private GameLevel getGameLevelFromChoiceBox() {
        
        LevelIdentificator levelId = LevelIdentificator.getId((String)difficulty.getValue());
        
        switch (levelId) {
        case EASY_LEVEL  :
            return GameLevel.EASY;
        case MEDIUM_LEVEL:
            return GameLevel.MEDIUM;
        case HARD_LEVEL  :
            return GameLevel.HARD;
        default:
            throw new RuntimeException("unrecognized game level");
        }
    }

    @FXML
    protected void exitProgram(ActionEvent event) {
        blocking_queue.add(new ProgramQuitEvent());
        Platform.exit();
        System.out.println("Exit");
    }

    public View() {}

    public View(BlockingQueue<GameEvent> blocking_queue) {
        this.blocking_queue = blocking_queue;
        //TODO utworzyc elementy widoku
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane page = (AnchorPane) FXMLLoader.load(View.class.getResource("source/menu.fxml"));
        Scene scene = new Scene(page, page.getMaxWidth()-10, page.getMaxHeight()-10);
        scene.getStylesheets().add(View.class.getResource("source/stylesheet.css").toExternalForm());
        primaryStage.initStyle(StageStyle.DECORATED);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkers");
        primaryStage.setResizable(false);
        primaryStage.show();
        
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
                checkIsEmptyField(mockup, i, j);
                
                checkIsBlackChecker(mockup, i,  j);
                checkIsBlackQueen(mockup, i, j);
                checkIsWhiteChecker(mockup, i, j);
                checkIsWhiteQueen(mockup, i, j);
            }
            System.out.println();
        }
        System.out.println("----------------------------------");
        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        int y = in.nextInt();
        //System.out.println("przed wyslaniem: x: "+x+", y: "+y);
        blocking_queue.add(new FieldClickEvent(x, y));
    }
    
    private void checkIsEmptyField(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.EMPTY_FIELD) {
            if(mockup.getField(j, i).isSelected()) {
                System.out.print("]_[ ");
            } else {
                System.out.print("[_] ");
            }
        }
    }
    
    private void checkIsBlackChecker(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.BLACK_CHECKER) {
            if(mockup.getField(j, i).isSelected()) {
                System.out.print("]@[ ");
            } else {
                System.out.print("[@] ");
            }
        }
    }
    
    private void checkIsBlackQueen(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.BLACK_QUEEN) {
            if(mockup.getField(j, i).isSelected()) {
                System.out.print("]2[ ");
            } else {
                System.out.print("[2] ");
            }
        }
    }
    
    private void checkIsWhiteChecker(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.WHITE_CHECKER) {
            if(mockup.getField(j, i).isSelected()) {
                System.out.print("]#[ ");
            } else {
                System.out.print("[#] ");
            }
        }
    }
    
    private void checkIsWhiteQueen(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.WHITE_QUEEN) {
            if(mockup.getField(j, i).isSelected()) {
                System.out.print("]3[ ");
            } else {
                System.out.print("[3] ");
            }
        }
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
