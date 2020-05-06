package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Checker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CheckerModel extends StackPane {
    int x;
    int y;
    Checker checker;
    boolean isDamka;

    public CheckerModel(int x, int y, Checker checker, boolean isDamka) throws FileNotFoundException {
        this.x = x;
        this.y = y;
        this.checker = checker;
        this.isDamka = isDamka;

        Circle circle = new Circle();
        circle.setRadius(40);
        if (this.checker.color == 1)
            circle.setFill(Color.BLACK);
        else circle.setFill(Color.WHITESMOKE);
        relocate((x + 0.1) * 100, (y + 0.1) * 100);
        setOnMouseDragged(e -> {
            relocate((Math.floor(e.getSceneX() / 100) + 0.1) * 100, (Math.floor(e.getSceneY() / 100) + 0.1) * 100);
        });
        getChildren().add(circle);

        if (isDamka) {
            Image image = new Image(new FileInputStream(new File("src\\main\\resources\\crown.png").getAbsolutePath()));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(40);
            imageView.setFitHeight(40);
            getChildren().add(imageView);
        }
    }

}
