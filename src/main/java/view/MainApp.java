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
    private Group cells = new Group();
    private Group checkers = new Group();
    public static int size = 90;

    private Pane makeBoard() throws Exception {
        gameState.getBoard();
        fillBoard();
        Pane root = new Pane();
        root.setPrefSize(8 * size, 8 * size);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Rectangle rectangle = createCell(i, j);
                cells.getChildren().add(rectangle);
            }
        }
        root.getChildren().addAll(cells, checkers);
        return root;
    }

    private void fillBoard() throws FileNotFoundException {
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
        Scene scene = new Scene(makeBoard(), 8 * size, 8 * size);
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    private Rectangle createCell(int i, int j) {
        Rectangle rectangle = new Rectangle();
        rectangle.setHeight(size);
        rectangle.setWidth(size);
        if (gameState.board[i][j].color == model.Color.BROWN) rectangle.setFill(Color.BROWN);
        else rectangle.setFill(Color.BEIGE);
        rectangle.relocate(i * size, j * size);
        return rectangle;
    }

    private CheckerModel createChecker(int i, int j, Checker checker, boolean isDamka) throws FileNotFoundException {
        CheckerModel checkerModel = new CheckerModel(i, j, checker, isDamka);
        checkerModel.setOnMouseReleased(e -> {
            int newX = (int) Math.floor(e.getSceneX() / size);
            int newY = (int) Math.floor(e.getSceneY() / size);

            if (gameState.previousMoveColor == model.Color.BLACK && gameState.needtobyteforWhite() &&
                    checker.color == model.Color.WHITE && gameState.canMove(newX, newY, checker) != 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Now you must byte black checker!");
                alert.showAndWait();
            }

            if (gameState.previousMoveColor == model.Color.WHITE && gameState.needtobyteforBlack() &&
                    checker.color == model.Color.BLACK && gameState.canMove(newX, newY, checker) != 2) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Now you must byte white checker!");
                alert.showAndWait();
            }

            gameState.makeMove(newX, newY, checker);

            if (gameState.moveCount != 0 && gameState.board[1][0].hasChecker() && gameState.board[3][0].hasChecker() &&
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