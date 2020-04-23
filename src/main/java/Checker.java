import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Checker extends StackPane {
    public int color;
    public double oldX;
    public double oldY;
    public int moveType;

    public Checker(int x, int y, int color, int moveType) {
        this.color = color;
        this.moveType = moveType;

        go(x, y);

        Circle circle = new Circle();

        circle.setRadius(40);
        circle.setFill(color());

        getChildren().add(circle);

        setOnMouseDragged(e -> {
            relocate((Math.floor(e.getSceneX() / 100) + 0.1) * 100, (Math.floor(e.getSceneY() / 100) + 0.1) * 100);
        });
    }

    public void go(int newX, int newY) {
        oldX = (newX + 0.1) * 100;
        oldY = (newY + 0.1) * 100;
        relocate(oldX, oldY);
    }

    public Paint color() {
        if (this.color == 1) return Color.BLACK;
        else return Color.WHITESMOKE;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public void getBack() {
        relocate(oldX, oldY);
    }
}
