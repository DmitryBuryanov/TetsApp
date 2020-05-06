package view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Checker;
import model.GameState;

import java.io.FileNotFoundException;

public class MainApp extends Application {

    GameState gameState = new GameState();
    public Group cells = new Group();
    public Group checkers = new Group();

    public Pane makeBoard() throws Exception {
        gameState.getBoard();
        fillBoard();
        Pane root = new Pane();
        root.setPrefSize(800, 800);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = createCell(i, j);
                cells.getChildren().add(rectangle);
            }
        }
        root.getChildren().addAll(cells, checkers);
        return root;
    }

    public void fillBoard() throws FileNotFoundException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Checker checker;
                if (gameState.board[i][j].hasChecker()) {
                    checker = gameState.board[i][j].getChecker();
                    CheckerModel checkerModel = createChecker(i, j, checker, checker.isDamka);
                    checkers.getChildren().add(checkerModel);
                }
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(makeBoard(), 800, 800);
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public Rectangle createCell(int i, int j) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(100);
        rectangle.setWidth(100);
        if (gameState.board[i][j].color == 1) rectangle.setFill(Color.BROWN);
        else rectangle.setFill(Color.BEIGE);
        rectangle.relocate(i * 100, j * 100);
        return rectangle;
    }

    public CheckerModel createChecker(int i, int j, Checker checker, boolean isDamka) throws FileNotFoundException {
        CheckerModel checkerModel = new CheckerModel(i, j, checker, isDamka);
        checkerModel.setOnMouseReleased(e -> {
            int newX = (int) Math.floor(e.getSceneX() / 100);
            int newY = (int) Math.floor(e.getSceneY() / 100);

            if (gameState.previousMoveColor == 1 && gameState.needtobyteforWhite() && checker.color == 0 &&
                    gameState.canMove(newX, newY, checker) != 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Now you must byte black checker!");
                alert.showAndWait();
            }

            if (gameState.previousMoveColor == 0 && gameState.needtobyteforBlack() && checker.color == 1 &&
                    gameState.canMove(newX, newY, checker) != 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Now you must byte white checker!");
                alert.showAndWait();
            }

            gameState.makeMove(newX, newY, checker);

            if (gameState.board[1][0].hasChecker() && gameState.board[3][0].hasChecker() &&
                    gameState.board[5][0].hasChecker() && gameState.board[7][0].hasChecker() &&
                    gameState.board[0][1].hasChecker() && gameState.board[2][1].hasChecker() &&
                    gameState.board[4][1].hasChecker() && gameState.board[6][1].hasChecker() &&
                    gameState.board[1][2].hasChecker() && gameState.board[3][2].hasChecker() &&
                    gameState.board[5][2].hasChecker() && gameState.board[7][2].hasChecker() &&
                    gameState.board[0][5].hasChecker() && gameState.board[2][5].hasChecker() &&
                    gameState.board[4][5].hasChecker() && gameState.board[6][5].hasChecker() &&
                    gameState.board[1][6].hasChecker() && gameState.board[3][6].hasChecker() &&
                    gameState.board[5][6].hasChecker() && gameState.board[7][6].hasChecker() &&
                    gameState.board[0][7].hasChecker() && gameState.board[2][7].hasChecker() &&
                    gameState.board[4][7].hasChecker() && gameState.board[6][7].hasChecker()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("We have winner");
                alert.setHeaderText(null);
                alert.setContentText("Game is over. Let's start again");
                alert.showAndWait();
            }

            checkers.getChildren().clear();
            try {
                fillBoard();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
        return checkerModel;
    }

}
