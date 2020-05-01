import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

public class Tests {

    @Test
    public void test() throws FileNotFoundException {

        MainApp mainApp = new MainApp();

        mainApp.getBoard();

        //тестирование правильного заполнения доски в начале игры
        assertTrue(
                //белые шашки
                mainApp.board[0][7].hasChecker() && mainApp.board[2][7].hasChecker() &&
                mainApp.board[4][7].hasChecker() && mainApp.board[6][7].hasChecker() &&
                mainApp.board[1][6].hasChecker() && mainApp.board[3][6].hasChecker() &&
                mainApp.board[5][6].hasChecker() && mainApp.board[7][6].hasChecker() &&
                mainApp.board[0][5].hasChecker() && mainApp.board[2][5].hasChecker() &&
                mainApp.board[4][5].hasChecker() && mainApp.board[6][5].hasChecker() &&

                //черные шашки
                mainApp.board[1][0].hasChecker() && mainApp.board[3][0].hasChecker() &&
                mainApp.board[5][0].hasChecker() && mainApp.board[7][0].hasChecker() &&
                mainApp.board[0][1].hasChecker() && mainApp.board[2][1].hasChecker() &&
                mainApp.board[4][1].hasChecker() && mainApp.board[6][1].hasChecker() &&
                mainApp.board[1][2].hasChecker() && mainApp.board[3][2].hasChecker() &&
                mainApp.board[5][2].hasChecker() && mainApp.board[7][2].hasChecker());

        //тестирование хождения на белую клетку
        mainApp.makeMove(1, 5, mainApp.board[0][5].getChecker());
        assertTrue(!mainApp.board[1][5].hasChecker() && mainApp.board[0][5].hasChecker());

        //тестирование хода белой шашки
        mainApp.makeMove(1, 4, mainApp.board[0][5].getChecker());
        assertTrue(mainApp.board[1][4].hasChecker());

        //тестирование хода шашки одного цвета 2 раза подряд
        mainApp.makeMove(2, 3, mainApp.board[1][4].getChecker());
        assertTrue(!mainApp.board[2][3].hasChecker() && mainApp.board[1][4].hasChecker());

        // тестирование хода черной шашки
        mainApp.makeMove(2, 3, mainApp.board[1][2].getChecker());
        assertTrue(mainApp.board[2][3].hasChecker());

        //ход белой шашки
        mainApp.makeMove(3, 4, mainApp.board[2][5].getChecker());

        //тестирование того, как черная шашка побила белую
        mainApp.makeMove(0, 5, mainApp.board[2][3].getChecker());
        assertTrue(mainApp.board[0][5].hasChecker() && !mainApp.board[2][3].hasChecker() && !mainApp.board[1][4].hasChecker());

        //поочередные ходы белых и черных шашек
        mainApp.makeMove(4, 3, mainApp.board[3][4].getChecker());
        mainApp.makeMove(3, 4, mainApp.board[5][2].getChecker());
        mainApp.makeMove(2, 3, mainApp.board[4][5].getChecker());
        mainApp.makeMove(1, 4, mainApp.board[3][2].getChecker());
        mainApp.makeMove(4, 5, mainApp.board[3][6].getChecker());
        mainApp.makeMove(6, 3, mainApp.board[7][2].getChecker());
        mainApp.makeMove(2, 5, mainApp.board[1][6].getChecker());

        //тестирование взятия одной черной шашкой двух белых подряд
        mainApp.makeMove(3, 6, mainApp.board[1][4].getChecker());
        mainApp.makeMove(5, 4, mainApp.board[3][6].getChecker());
        assertTrue(!mainApp.board[2][5].hasChecker() && !mainApp.board[4][5].hasChecker() && mainApp.board[5][4].hasChecker()
        && !mainApp.board[1][4].hasChecker());

        //поочередные ходы белых и черных шашек
        mainApp.makeMove(4, 3, mainApp.board[6][5].getChecker());
        mainApp.makeMove(3, 2, mainApp.board[2][1].getChecker());
        mainApp.makeMove(2, 1, mainApp.board[4][3].getChecker());
        mainApp.makeMove(3, 2, mainApp.board[1][0].getChecker());
        mainApp.makeMove(1, 6, mainApp.board[0][7].getChecker());
        mainApp.makeMove(1, 2, mainApp.board[0][1].getChecker());
        mainApp.makeMove(2, 5, mainApp.board[1][6].getChecker());
        mainApp.makeMove(7, 4, mainApp.board[6][3].getChecker());
        mainApp.makeMove(3, 4, mainApp.board[2][5].getChecker());
        mainApp.makeMove(0, 3, mainApp.board[1][2].getChecker());
        mainApp.makeMove(3, 6, mainApp.board[2][7].getChecker());
        mainApp.makeMove(1, 6, mainApp.board[0][5].getChecker());
        mainApp.makeMove(4, 5, mainApp.board[3][6].getChecker());

        //тестирование на становление дамкой
        mainApp.makeMove(0, 7, mainApp.board[1][6].getChecker());
        assertTrue(mainApp.board[0][7].hasChecker() && mainApp.board[0][7].getChecker().isDamka);

        mainApp.makeMove(3, 6, mainApp.board[4][7].getChecker());

        //тестирование взятия дамкой
        mainApp.makeMove(5, 2, mainApp.board[0][7].getChecker());
        assertTrue(!mainApp.board[0][7].hasChecker() && mainApp.board[5][2].hasChecker() && !mainApp.board[3][4].hasChecker());

        mainApp.makeMove(5, 4, mainApp.board[4][5].getChecker());

        //тестирование хождения дамкой
        mainApp.makeMove(1, 6, mainApp.board[5][2].getChecker());
        assertTrue(!mainApp.board[5][2].hasChecker() && mainApp.board[1][6].hasChecker());

        mainApp.refresh();

        //тетсирование правильного обновления доски после окончния партии
        assertTrue(
                //белые шашки
                mainApp.board[0][7].hasChecker() && mainApp.board[2][7].hasChecker() &&
                        mainApp.board[4][7].hasChecker() && mainApp.board[6][7].hasChecker() &&
                        mainApp.board[1][6].hasChecker() && mainApp.board[3][6].hasChecker() &&
                        mainApp.board[5][6].hasChecker() && mainApp.board[7][6].hasChecker() &&
                        mainApp.board[0][5].hasChecker() && mainApp.board[2][5].hasChecker() &&
                        mainApp.board[4][5].hasChecker() && mainApp.board[6][5].hasChecker() &&

                        //черные шашки
                        mainApp.board[1][0].hasChecker() && mainApp.board[3][0].hasChecker() &&
                        mainApp.board[5][0].hasChecker() && mainApp.board[7][0].hasChecker() &&
                        mainApp.board[0][1].hasChecker() && mainApp.board[2][1].hasChecker() &&
                        mainApp.board[4][1].hasChecker() && mainApp.board[6][1].hasChecker() &&
                        mainApp.board[1][2].hasChecker() && mainApp.board[3][2].hasChecker() &&
                        mainApp.board[5][2].hasChecker() && mainApp.board[7][2].hasChecker());
    }
}
