import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Checker extends StackPane {
    public int x;
    public int y;
    public int color;

    public Checker(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;

        Circle circle = new Circle();

        circle.setRadius(40);
        circle.setFill(color());

        relocate((x + 0.1) * 100, (y + 0.1) * 100);

        getChildren().add(circle);
    }

    public Paint color() {
        if (this.color == 1) return Color.BLACK;
        else return Color.WHITESMOKE;
    }
}
