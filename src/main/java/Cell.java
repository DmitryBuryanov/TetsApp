import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    public int x;
    public int y;
    public int color;
    public Checker checker;

    public Cell(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;

        setHeight(100);
        setWidth(100);
        setFill(color());

        relocate(x * 100, y * 100);
    }

    public Paint color() {
        if (this.color % 2 == 1) return Color.BROWN;
        else return Color.BEIGE;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public Checker getChecker() {
        return checker;
    }

    public boolean hasChecker() {
        return getChecker() != null;
    }
}