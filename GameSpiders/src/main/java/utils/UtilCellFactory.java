package utils;

import core.TCell;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import java.util.Map;

public class UtilCellFactory {

    public static class Cell extends TableCell<TCell[], TCell> {

        private final ImageView imageView = new ImageView();

        private final Map<String, Image> images;

        {
            imageView.setFitWidth(33.4);
            imageView.setFitHeight(33.4);
            setGraphic(imageView);
        }

        public Cell(Map<String, Image> images) {
            this.images = images;
        }

        @Override
        protected void updateItem(TCell item, boolean empty) {
            if (empty || item == null) {
                imageView.setImage(null);
            } else {
                switch (item.getCharacter()) {
                    case Empty -> imageView.setImage(null);
                    case Wall -> setStyle("-fx-background-color: burlywood;");
                    case Spider -> imageView.setImage(images.get("imageSpider"));
                    case Player -> {
                        switch (item.getMovingDirection()) {
                            case Right -> imageView.setImage(images.get("imagePlayer"));
                            case Left -> imageView.setImage(images.get("imagePlayerLeft"));
                            case Up -> imageView.setImage(images.get("imagePlayerUp"));
                            case Down -> imageView.setImage(images.get("imagePlayerDown"));
                        }

                        if (item.characterIsKilled()) {
                            setStyle("-fx-background-color: red;");
                        }
                    }
                    case Patron -> {
                        switch (item.getMovingDirection()) {
                            case Right -> imageView.setImage(images.get("imagePatron"));
                            case Left -> imageView.setImage(images.get("imagePatronLeft"));
                            case Up -> imageView.setImage(images.get("imagePatronUp"));
                            case Down -> imageView.setImage(images.get("imagePatronDown"));
                        }
                    }
                    case Exit -> {
                        setStyle("-fx-background-color: lightskyblue;");
                        setText("EXIT");
                        setFont(new Font("Arial", 14));
                    }
                }
            }
        }
    }

}