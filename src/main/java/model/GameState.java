package model;

public class GameState {
    public Cell[][] board = new Cell[8][8];
    public Color previousMoveColor = Color.Black;
    public int moveCount = 0;

    public void getBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Color color;
                if ((i + j) % 2 == 0) color = Color.Beige;
                else color = Color.Brown;
                board[i][j] = new Cell(i, j, color);
                Checker checker = null;
                board[i][j].setChecker(null);
                if (j < 3 && board[i][j].color == Color.Brown) {
                    checker = new Checker(i, j, Color.Black, 0, false);
                }
                if (j > 4 && board[i][j].color == Color.Brown) {
                    checker = new Checker(i, j, Color.White, 0, false);
                }
                if (checker != null) {
                    board[i][j].setChecker(checker);
                }
            }
        }
        moveCount = 0;
    }

    public int canMove(int newX, int newY, Checker checker) {
        if (checker.color == previousMoveColor) return 0;
        if (board[newX][newY].hasChecker() || (newX + newY) % 2 != 1) return 0;
        else {
            int nowX = (int) Math.floor(checker.getOldX() / 100);
            int nowY = (int) Math.floor(checker.getOldY() / 100);
            if (!checker.isDamka) {
                if (checker.color == Color.Black && Math.abs(newX - nowX) == 1 && newY - nowY == 1 ||
                        checker.color == Color.White && Math.abs(newX - nowX) == 1 && newY - nowY == -1) {
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

    public void makeMove(int newX, int newY, Checker checker) {
        int moveResult;
        if (newX < 0 || newY < 0 || newX > 7 || newY > 7) moveResult = 0;
        else moveResult = canMove(newX, newY, checker);

        if ((needtobyteforWhite() && checker.color == Color.White || needtobyteforBlack()
                && checker.color == Color.Black) && moveResult != 2) {
            moveResult = 0;
        }

        int nowX = (int) Math.floor(checker.getOldX() / 100);
        int nowY = (int) Math.floor(checker.getOldY() / 100);

        if (moveResult != 0) {
            checker.go(newX, newY);
            board[nowX][nowY].setChecker(null);
            board[newX][newY].setChecker(checker);
            moveCount++;
            if (moveResult == 1) {
                if (checker.color == Color.Black && newY == 7) checker.isDamka = true;
                if (checker.color == Color.White && newY == 0) checker.isDamka = true;
                previousMoveColor = checker.color;

            } else if (moveResult == 2) {
                if (!checker.isDamka) {
                    int evilX = (newX + nowX) / 2;
                    int evilY = (newY + nowY) / 2;
                    board[evilX][evilY].setChecker(null);

                } else {
                    int lx = (newX - nowX) / Math.abs(newX - nowX);
                    int ly = (newY - nowY) / Math.abs(newY - nowY);
                    int xx = nowX + lx;
                    int yy = nowY + ly;
                    while (!board[xx][yy].hasChecker()) {
                        xx += lx;
                        yy += ly;
                    }
                    board[xx][yy].setChecker(null);
                }
                if (!canByte(board[newX][newY])) previousMoveColor = checker.color;

                if (checker.color == Color.Black && newY == 7) checker.isDamka = true;
                if (checker.color == Color.White && newY == 0) checker.isDamka = true;

                if (gameover().equals("White won") || gameover().equals("Black won")) {
                    getBoard();
                    previousMoveColor = Color.Black;
                }
            }

            if (checker.color == Color.Black && newY == 7 || checker.color == Color.White && newY == 0)
                checker.isDamka = true;

            if (gameover().equals("White won") || gameover().equals("Black won")) {
                getBoard();
                previousMoveColor = Color.Black;
            }
        }
        checker.moveType = moveResult;
    }

    private String gameover() {
        int black = 0;
        int white = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == Color.Black) black += 1;
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == Color.White) white += 1;
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

    private boolean cellExist(int x, int y) {
        return x >= 0 && x <= 7 && y >= 0 && y <= 7;
    }

    private boolean canByte(Cell cell) {
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
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color &&
                                    cellExist(nowX - 1, nowY - 1) && !board[nowX - 1][nowY - 1].hasChecker())
                                return true;
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
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color &&
                                    cellExist(nowX + 1, nowY - 1) && !board[nowX + 1][nowY - 1].hasChecker())
                                return true;
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
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color &&
                                    cellExist(nowX + 1, nowY + 1) && !board[nowX + 1][nowY + 1].hasChecker())
                                return true;
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
                            if (board[nowX][nowY].getChecker().color != cell.getChecker().color &&
                                    cellExist(nowX - 1, nowY + 1) && !board[nowX - 1][nowY + 1].hasChecker())
                                return true;
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
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == Color.White && canByte(board[i][j]))
                    return true;
            }
        }
        return false;
    }

    public boolean needtobyteforBlack() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].hasChecker() && board[i][j].getChecker().color == Color.Black && canByte(board[i][j]))
                    return true;
            }
        }
        return false;
    }
}
