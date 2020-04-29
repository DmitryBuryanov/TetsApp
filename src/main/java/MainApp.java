import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainApp extends Application {

    public Cell[][] board = new Cell[8][8];
    public Group cells = new Group();
    public Group checkers = new Group();

    public int previousMoveColor = 1;

    public Parent getBoard() throws FileNotFoundException {
        Pane root = new Pane();
        root.setPrefSize(800, 800);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell cell = new Cell(i, j, (i + j) % 2);
                board[i][j] = cell;

                Checker checker = null;

                if (j < 3 && board[i][j].color == 1) {
                    checker = newChecker(i, j, 1, 0, false);
                }
                if (j > 4 && board[i][j].color == 1) {
                    checker = newChecker(i, j, 0, 0, false);
                }

                if (checker != null) {
                    cell.setChecker(checker);
                    checkers.getChildren().add(board[i][j].getChecker());
                }
                cells.getChildren().add(cell);
            }
        }
        root.getChildren().addAll(cells, checkers);
        return root;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(getBoard(), 800, 800);
        stage.setScene(scene);
        stage.setTitle("Checkers");
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public int canMove(int newX, int newY, Checker checker) {
        if (checker.color == previousMoveColor) return 0;

        if (board[newX][newY].hasChecker() || (newX + newY) % 2 != 1) return 0;

        else {
            int nowX = (int) Math.floor(checker.getOldX() / 100);
            int nowY = (int) Math.floor(checker.getOldY() / 100);
            if (!checker.isDamka) {
                if (checker.color == 1 && Math.abs(newX - nowX) == 1 && newY - nowY == 1 ||
                        checker.color == 0 && Math.abs(newX - nowX) == 1 && newY - nowY == -1) {
                    return 1;
                }
                int evilX = (newX + nowX) / 2;
                int evilY = (newY + nowY) / 2;
                if (board[evilX][evilY].hasChecker() && board[evilX][evilY].getChecker().color != checker.color &&
                        Math.abs(newX - nowX) == 2 && Math.abs(newY - nowY) == 2) {

                    return 2;
                }
            } else {
                int lx = (newX - nowX) / Math.abs(newX - nowX);
                int ly = (newY - nowY) / Math.abs(newY - nowY);
                int xx = nowX + lx;
                int yy = nowY + ly;
                int countChecker = 0;
                while (xx != newX && yy != newY) {
                    if (board[xx][yy].hasChecker() && board[xx][yy].getChecker().color == checker.color) return 0;
                    if (board[xx][yy].hasChecker() && board[xx][yy].getChecker().color != checker.color)
                        countChecker += 1;
                    xx += lx;
                    yy += ly;
                }
                if (countChecker == 0) return 1;
                if (countChecker == 1) return 2;
            }
        }
        return 0;
    }

    public Checker newChecker(int x, int y, int color, int moveType, boolean isDamka) throws FileNotFoundException {
        Checker checker = new Checker(x, y, color, moveType, isDamka);

        checker.setOnMouseReleased(e -> {
            int newX = (int) Math.floor(checker.getLayoutX() / 100);
            int newY = (int) Math.floor(checker.getLayoutY() / 100);
            int moveResult;

            if (newX < 0 || newY < 0 || newX > 7 || newY > 7) moveResult = 0;
            else moveResult = canMove(newX, newY, checker);

            if (moveResult == 0) {
                checker.getBack();
            } else {
                if (needtobyteforWhite() && checker.color == 0 && moveResult != 2) {
                    moveResult = 0;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Now you must byte black checker!");
                    alert.showAndWait();
                }

                if (needtobyteforBlack() && checker.color == 1 && moveResult != 2) {
                    moveResult = 0;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Now you must byte white checker!");
                    alert.showAndWait();
                }

                int nowX = (int) Math.floor(checker.getOldX() / 100);
                int nowY = (int) Math.floor(checker.getOldY() / 100);

                if (moveResult == 0) {
                    checker.getBack();
                } else if (moveResult == 1) {
                    if (checker.color == 1 && newY == 7) checker.isDamka = true;
                    if (checker.color == 0 && newY == 0) checker.isDamka = true;
                    try {
                        checker.setCrown();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    checker.go(newX, newY);
                    board[nowX][nowY].setChecker(null);
                    board[newX][newY].setChecker(checker);
                    previousMoveColor = checker.color;

                } else if (moveResult == 2) {

                    checker.go(newX, newY);
                    board[nowX][nowY].setChecker(null);
                    board[newX][newY].setChecker(checker);

                    if (!checker.isDamka) {
                        int evilX = (newX + nowX) / 2;
                        int evilY = (newY + nowY) / 2;

                        Checker evil = board[evilX][evilY].getChecker();
                        board[evilX][evilY].setChecker(null);
                        checkers.getChildren().remove(evil);
                    } else {
                        int lx = (newX - nowX) / Math.abs(newX - nowX);
                        int ly = (newY - nowY) / Math.abs(newY - nowY);
                        int xx = nowX + lx;
                        int yy = nowY + ly;
                        while (!board[xx][yy].hasChecker()) {
                            xx += lx;
                            yy += ly;
                        }
                        Checker evil = board[xx][yy].getChecker();
                        board[xx][yy].setChecker(null);
                        checkers.getChildren().remove(evil);
                    }
                    if (!canByte(board[newX][newY])) previousMoveColor = checker.color;

                    if (checker.color == 1 && newY == 7) checker.isDamka = true;
                    if (checker.color == 0 && newY == 0) checker.isDamka = true;
                    try {
                        checker.setCrown();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
                checker.moveType = moveResult;

                if (gameover().equals("White won") || gameover().equals("Black won")) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("We have winner!");
                    alert.setHeaderText(null);
                    alert.setContentText(gameover());
                    alert.showAndWait();

                    try {
                        refresh();
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        return checker;
    }

    public void refresh() throws FileNotFoundException {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker()) checkers.getChildren().remove(board[i][j].getChecker());
                board[i][j].setChecker(null);
                if (j < 3 && board[i][j].color == 1) {
                    board[i][j].setChecker(newChecker(i, j, 1, 0, false));
                }
                if (j > 4 && board[i][j].color == 1) {
                    board[i][j].setChecker(newChecker(i, j, 0, 0, false));
                }
                if (board[i][j].hasChecker()) {
                    checkers.getChildren().add(board[i][j].getChecker());
                }
            }
        }
        previousMoveColor = 1;
    }

    public String gameover() {
        int black = 0;
        int white = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == 1) black += 1;
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == 0) white += 1;
            }
        }
        if (black == 0) {
            return "White won";
        }
        if (white == 0) {
            return "Black won";
        }
        return "";
    }
    
    public boolean cellExist(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    public boolean canByte(Cell cell) {
        if (cell.hasChecker()) {
            int i = cell.x;
            int j = cell.y;
            if (!cell.getChecker().isDamka) {
                if (cellExist(i + 2, j + 2) && board[i + 1][j + 1].hasChecker() && !board[i + 2][j + 2].hasChecker()
                        && board[i + 1][j + 1].getChecker().color != cell.getChecker().color) return true;
                if (cellExist(i + 2, j - 2) && board[i + 1][j - 1].hasChecker() && !board[i + 2][j - 2].hasChecker()
                        && board[i + 1][j - 1].getChecker().color != cell.getChecker().color) return true;
                if (cellExist(i - 2, j + 2) && board[i - 1][j + 1].hasChecker() && !board[i - 2][j + 2].hasChecker()
                        && board[i - 1][j + 1].getChecker().color != cell.getChecker().color) return true;
                if (cellExist(i - 2, j - 2) && board[i - 1][j - 1].hasChecker() && !board[i - 2][j - 2].hasChecker()
                        && board[i - 1][j - 1].getChecker().color != cell.getChecker().color) return true;
            } else {
                int nowX = i;
                int nowY = j;
                while (nowX != 0 && nowY != 0) {
                    nowX--;
                    nowY--;
                    if (cellExist(nowX, nowY)) {
                        if (board[nowX][nowY].hasChecker()) {
                            if (board[nowX][nowY].getChecker().color == cell.getChecker().color) break;
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color && cellExist(nowX - 1, nowY - 1) &&
                                    !board[nowX - 1][nowY - 1].hasChecker()) return true;
                        }
                    }
                }

                nowX = i;
                nowY = j;
                while (nowX != 7 && nowY != 0) {
                    nowX++;
                    nowY--;
                    if (cellExist(nowX, nowY)) {
                        if (board[nowX][nowY].hasChecker()) {
                            if (board[nowX][nowY].getChecker().color == cell.getChecker().color) break;
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color && cellExist(nowX + 1, nowY - 1) &&
                                    !board[nowX + 1][nowY - 1].hasChecker()) return true;
                        }
                    }
                }

                nowX = i;
                nowY = j;
                while (nowX != 7 && nowY != 7) {
                    nowX++;
                    nowY++;
                    if (cellExist(nowX, nowY)) {
                        if (board[nowX][nowY].hasChecker()) {
                            if (board[nowX][nowY].getChecker().color == cell.getChecker().color) break;
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color && cellExist(nowX + 1, nowY + 1) &&
                                    !board[nowX + 1][nowY + 1].hasChecker()) return true;
                        }
                    }
                }

                nowX = i;
                nowY = j;
                while (nowX != 0 && nowY != 7) {
                    nowX--;
                    nowY++;
                    if (cellExist(nowX, nowY)) {
                        if (board[nowX][nowY].hasChecker()) {
                            if (board[nowX][nowY].getChecker().color == cell.getChecker().color) break;
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color && cellExist(nowX - 1, nowY + 1) &&
                                    !board[nowX - 1][nowY + 1].hasChecker()) return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean needtobyteforWhite() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == 0 && canByte(board[i][j])) return true;
            }
        }
        return false;
    }

    public boolean needtobyteforBlack() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == 1 && canByte(board[i][j])) return true;
            }
        }
        return false;
    }

}
