import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public Cell[][] board = new Cell[8][8];
    public Group cells = new Group();
    public Group checkers = new Group();

    public Parent getBoard() {
        Pane root = new Pane();
        root.setPrefSize(800, 800);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Cell(i, j, (i + j) % 2);
                cells.getChildren().add(board[i][j]);

                if (j < 3 && board[i][j].color == 1) {
                    board[i][j].setChecker(new Checker(i, j, 1));
                }
                if (j > 4 && board[i][j].color == 1) {
                    board[i][j].setChecker(new Checker(i, j, 0));
                }

                if (board[i][j].getChecker() != null) checkers.getChildren().add(board[i][j].getChecker());
            }
        }

        root.getChildren().addAll(cells, checkers);
        return root;
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(getBoard(), 800, 800);
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
