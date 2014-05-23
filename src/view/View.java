package view;

import java.io.IOException;
import java.util.Scanner;

import common.events.FieldClickEvent;
import common.events.GameEvent;
import common.events.GameFinishEvent;
import common.events.GameStartEvent;
import common.events.ProgramQuitEvent;
import common.Coordinate;
import common.CheckerMockup;
import common.GameStateMockup;
import common.Mockup;
import common.MoveMockup;
import model.CheckerColor;
import model.GameLevel;
import model.Model;

import java.util.concurrent.BlockingQueue;

import javafx.animation.PathTransition;
import javafx.animation.PathTransitionBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import jfx.messagebox.MessageBox;

public class View extends Application implements Runnable {
    static private BlockingQueue<GameEvent> blocking_queue;
    static private Mockup mockup;
    static private Stage stage;
    static private Image imageWhiteChecker;
    static private Image imageWhiteQueen;
    static private Image imageBlackChecker;
    static private Image imageBlackQueen;
    static private GridPane board;
    static private AnchorPane checkers;
    static private Button fields[][];
    static private ImageView checkersOnBoard[][];
    static private MoveMockup lastMove;
    static private ImageView playerChecker;
    static private Text state;
    static private Text message;
    @FXML private ChoiceBox<?> difficulty;
    @FXML private ChoiceBox<?> color;
    @FXML private TextField name;
    
    @FXML
    protected void beginGame(ActionEvent event) {
        CheckerColor checkerColor = null;
        GameLevel gameLevel = null;
        
        if (name.getText().equals("")) {
            MessageBox.show(new Stage(),
                "Your name must not be empty", "Warning", MessageBox.OK);
            
        } else if(blocking_queue != null) {
            
            checkerColor = getCheckerColorFromChoiceBox();
            gameLevel = getGameLevelFromChoiceBox();
            
            blocking_queue.add(new GameStartEvent(name.getText(), checkerColor, gameLevel));
            System.out.println(name.getText());
            System.out.println(color.getValue());
            System.out.println(difficulty.getValue());
            
            showBoard();
        } else {
            throw new RuntimeException("View.beginGame - blockingQueue is null");
        }
        System.out.println("Start game");
    }

    @FXML
    protected void showMenu(ActionEvent event) {
        AnchorPane page;
        try {
            page = (AnchorPane) FXMLLoader.load(getClass().getResource("source/menu.fxml"));
            Scene scene = new Scene(page, page.getMaxWidth(), page.getMaxHeight());
            scene.getStylesheets().add(getClass().getResource("source/menu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        blocking_queue.add(new GameFinishEvent(false, null));
    }
    
    protected void showBoard() {
        AnchorPane page;
        try {
            page = (AnchorPane) FXMLLoader.load(getClass().getResource("source/board.fxml"));
            Scene scene = new Scene(page, page.getMaxWidth(), page.getMaxHeight());
            scene.getStylesheets().add(getClass().getResource("source/board.css").toExternalForm());
            board = (GridPane)scene.lookup("#board");
            playerChecker = (ImageView)scene.lookup("#playerChecker");
            state = (Text)scene.lookup("#state");
            message = (Text)scene.lookup("#message");
            checkers = (AnchorPane)scene.lookup("#checkers");
            fields = new Button[Model.getBoardSize()][Model.getBoardSize()];
            
            for(int i = 0; i < Model.getBoardSize(); ++i) {
                for(int j = 0; j < Model.getBoardSize(); ++j) {
                    final Button b = new Button();
                    b.setMinSize(50, 50);
                    if ((i+j) % 2 == 0) {
                        b.setStyle("-fx-background-image: url('" + getClass().getResource("source/black_tile.png").toExternalForm() + "');"
                                 + "-fx-background-color: transparent;");
                    } else {
                        b.setStyle("-fx-background-image: url('" + getClass().getResource("source/white_tile.png").toExternalForm() + "');"
                                 + "-fx-background-color: transparent;");
                    }
                    final String style = b.getStyle();
                    b.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            b.setStyle(style + "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.9) , 20 , 0.3 , 1 , 0 );");
                            blocking_queue.add(new FieldClickEvent(GridPane.getColumnIndex(b), GridPane.getRowIndex(b)));
                        }
                    });
                    fields[j][i] = b;
                    b.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> arg0, Boolean outFocus, Boolean onFocus)
                        {
                            if (outFocus) {
                                b.setStyle(style);
                            }
                        }
                    });
                    board.add(b, j, i);
                }
            }
            fillBoard();
            
            for(int i = 0; i < Model.getBoardSize(); ++i) {
                scene.lookup("#row"+i).toFront();
                scene.lookup("#col"+i).toFront();
            }

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void fillBoard() {
        checkersOnBoard = new ImageView[Model.getBoardSize()][Model.getBoardSize()];
        for(int i = 0; i < Model.getBoardSize(); ++i) {
            for(int j = 0; j < Model.getBoardSize(); ++j) {
                ImageView image = null;
                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.EMPTY_FIELD) {
                    continue;
                }
                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.BLACK_CHECKER) {
                    image = new ImageView(imageBlackChecker);
                }
                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.BLACK_QUEEN) {
                    image = new ImageView(imageBlackQueen);
                }
                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.WHITE_CHECKER) {
                    image = new ImageView(imageWhiteChecker);
                }
                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.WHITE_QUEEN) {
                    image = new ImageView(imageWhiteQueen);
                }
                
                image.relocate(j*50+10, i*50+10);
                checkers.getChildren().add(image); 
                checkersOnBoard[j][i] = image;
            }
        }
        checkers.toFront();
    }
    
    protected void animateLastMove(MoveMockup move) {
        if((move != null) && (lastMove != move)) {
            lastMove = move;
            final int startX = move.getStartX();
            final int startY = move.getStartY();
            final int endX = move.getEndX();
            final int endY = move.getEndY();
            
            System.out.println("ruch z " + startX + " " + startY + " na " + endX + " " + endY);
            if (checkersOnBoard[startX][startY] != null) {
                System.out.println("before " + checkersOnBoard[startX][startY] + " " + (checkersOnBoard[startX][startY].getLayoutX()-10)/50 
                        + " " + (checkersOnBoard[startX][startY].getLayoutY()-10)/50);

                checkersOnBoard[startX][startY].relocate(endX*50+10, endY*50+10);
                
                double sqrt = 10*Math.sqrt(2); 
                Path path = new Path();
                path.getElements().add(new MoveTo(sqrt-(endX-startX)*50, sqrt-(endY-startY)*50));
                path.getElements().add(new LineTo((endX-startX)+sqrt, (endY-startY)+sqrt));
                
                checkersOnBoard[startX][startY].toFront();
                
                PathTransition pathTransition = PathTransitionBuilder.create()
                        .node(checkersOnBoard[startX][startY])
                        .path(path)
                        .duration(Duration.millis(1000))
                        .cycleCount(1)
                        .build();
                
                pathTransition.playFromStart();
                
                checkersOnBoard[endX][endY] = checkersOnBoard[startX][startY];
                checkersOnBoard[startY][startX] = null;
    
                System.out.println("after " + checkersOnBoard[endX][endY] + " " + (checkersOnBoard[endX][endY].getLayoutX()-10)/50 
                        + " " + (checkersOnBoard[endX][endY].getLayoutY()-10)/50);
                System.out.println();
                
                pathTransition.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println("HANDLE KURWA");
                        for(Coordinate xy : mockup.getDeletedCheckers()) {
                            System.out.println("usuwany " + xy.getX() + " " + xy.getY());
                            checkers.getChildren().remove(checkersOnBoard[xy.getX()][xy.getY()]);
                        }
                            
                        for(int i = 0; i < Model.getBoardSize(); ++i) {
                            for(int j = 0; j < Model.getBoardSize(); ++j) {
                                fields[j][i].setStyle(fields[j][i].getStyle().replace("-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.9) , 20 , 0.3 , 1 , 0 );", ""));
                                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.BLACK_QUEEN) {
                                    checkersOnBoard[j][i].setImage(imageBlackQueen);
                                }
                                if(mockup.getField(j, i).getCheckerMockup() == CheckerMockup.WHITE_QUEEN) {
                                    checkersOnBoard[j][i].setImage(imageWhiteQueen);
                                }
                            }
                        }
                    }
                });
            }
            else
                System.out.println("NULL: "+startX + " " + startY + " na " + endX + " " + endY);

        }
    }
    
    protected Button getButton(final int column, final int row) {
        for(Node node : board.getChildren()) {
            if (node.getClass() == Button.class) {

                if (GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row) {
                    return (Button)node;
                }
            }
        }
        return null;
    }
    
    /**
     * Zwraca kolor ze stringa ChoiceBoxa
     * @return CheckerColor
     */
    protected CheckerColor getCheckerColorFromChoiceBox() {
        
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
    
    public View() {
        imageWhiteChecker = new Image(getClass().getResourceAsStream("source/white.png"));
        imageWhiteQueen   = new Image(getClass().getResourceAsStream("source/white_queen.png"));
        imageBlackChecker = new Image(getClass().getResourceAsStream("source/black.png"));
        imageBlackQueen   = new Image(getClass().getResourceAsStream("source/black_queen.png"));
    }

    public View(BlockingQueue<GameEvent> blocking_queue) {
        View.blocking_queue = blocking_queue;
    }

    /**
     * Inicjuje widok
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        AnchorPane page = (AnchorPane) FXMLLoader.load(getClass().getResource("source/menu.fxml"));
        Scene scene = new Scene(page, page.getMaxWidth()-10, page.getMaxHeight()-10);
        scene.getStylesheets().add(getClass().getResource("source/menu.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                blocking_queue.add(new ProgramQuitEvent());
                Platform.exit();
                System.out.println("Exit");
            }
        });
    }

    /**
     * Wyswietla stan gry na podstawie podanej makiety
     *
     * W zaleznosci od stanu gry podanego w makiecie moze rysowac rozne ekrany (ekran powitalny,
     * ekran gry etc.) - patrz dokumentacja klasy GameStateMockup
     */
    public void draw(final Mockup mockup) {
        View.mockup = mockup;
        System.out.println("NOWY MOCKUP KURWA");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(mockup.getGameState() == GameStateMockup.WHITE_PLAYER_MOVE 
                        || mockup.getGameState() == GameStateMockup.WHITE_PLAYER_REPEAT_MOVE) {
                    playerChecker.setImage(imageWhiteChecker);
                }
                if(mockup.getGameState() == GameStateMockup.BLACK_PLAYER_MOVE 
                        || mockup.getGameState() == GameStateMockup.BLACK_PLAYER_REPEAT_MOVE) {
                    playerChecker.setImage(imageBlackChecker);
                }
                
                state.setText((new Communicate(mockup.getGameState())).getMessage());
                if(mockup.getLastMove() != null) {
                    message.setText("Ostatni ruch:\n"
                        + "(" + mockup.getLastMove().getStartX()
                        + "," + mockup.getLastMove().getStartY() + ") -> "
                        + "(" + mockup.getLastMove().getEndX()
                        + "," + mockup.getLastMove().getEndY() + ")");
                }
                animateLastMove(mockup.getLastMove());
                System.out.println("--------------------------------");

                System.out.println();
                for(int i = 0; i < Model.getBoardSize(); ++i) {
                    System.out.print(" "+i+" ");
                    for(int j = 0; j < Model.getBoardSize(); ++j) {
                        checkIsEmptyField(View.mockup, j, i);
                        checkIsBlackChecker(View.mockup, j,  i);
                        checkIsBlackQueen(View.mockup, j, i);
                        checkIsWhiteChecker(View.mockup, j, i);
                        checkIsWhiteQueen(View.mockup, j, i);
                    }
                    System.out.println();
                }
            }
        });

    }
    
    private void checkIsEmptyField(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(i, j).getCheckerMockup() == CheckerMockup.EMPTY_FIELD) {
            if(mockup.getField(i, j).isSelected()) {
                System.out.print("]_[ ");
            } else {
                System.out.print("[_] ");
            }
        }
    }
    
    private void checkIsBlackChecker(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(i, j).getCheckerMockup() == CheckerMockup.BLACK_CHECKER) {
            if(mockup.getField(i, j).isSelected()) {
                System.out.print("]@[ ");
            } else {
                System.out.print("[@] ");
            }
        }
    }
    
    private void checkIsBlackQueen(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(i, j).getCheckerMockup() == CheckerMockup.BLACK_QUEEN) {
            if(mockup.getField(i, j).isSelected()) {
                System.out.print("]2[ ");
            } else {
                System.out.print("[2] ");
            }
        }
    }
    
    private void checkIsWhiteChecker(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(i, j).getCheckerMockup() == CheckerMockup.WHITE_CHECKER) {
            if(mockup.getField(i, j).isSelected()) {
                System.out.print("]#[ ");
            } else {
                System.out.print("[#] ");
            }
        }
    }
    
    private void checkIsWhiteQueen(final Mockup mockup, final int i, final int j) {
        if(mockup.getField(i, j).getCheckerMockup() == CheckerMockup.WHITE_QUEEN) {
            if(mockup.getField(i, j).isSelected()) {
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