package app.gamespiders;

import core.Coordinates;
import core.PlayField;
import core.TCell;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.classes.Patron;
import model.enums.Character;
import model.enums.MovingDirection;
import model.classes.Player;
import model.enums.RoutePassing;
import model.classes.Spider;
import java.util.*;
import static utils.UtilCellFactory.Cell;

public class GameSpiders extends Application {

    private final PlayField playField = new PlayField(buildArrayForPlayField());

    private final TableView<TCell[]> table = new TableView<>();

    private final Player player = new Player(new Coordinates(4, 0), playField, table);

    private final Map<String, Image> images = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        hideHeaders();
        initializeImages();
        initializeColumns();
        initializeRows();
        launchSpiders();
        configureControlsAndShowStage(stage);
    }

    private void configureControlsAndShowStage(Stage stage) {
        Scene scene = new Scene(new Group());
        scene.getStylesheets().add("style.css");

        table.setEditable(true);
        table.setFocusTraversable(false);
        ((Group) scene.getRoot()).getChildren().addAll(table);

        scene.setOnKeyPressed(this::onKeyPressed);


        stage.setScene(scene);
        stage.show();
    }

    private void hideHeaders() {
        table.skinProperty().addListener((a, b, newSkin) ->
        {
            Pane header = (Pane) table.lookup("TableHeaderRow");
            header.setMinHeight(0);
            header.setPrefHeight(0);
            header.setMaxHeight(0);
            header.setVisible(false);
        });
    }

    private void initializeImages() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try {
            try (var streamSpider = classloader.getResourceAsStream("spider.jpg");
                 var streamPlayer = classloader.getResourceAsStream("player.png");
                 var streamPlayerLeft = classloader.getResourceAsStream("playerLeft.png");
                 var streamPlayerUp = classloader.getResourceAsStream("playerUp.png");
                 var streamPlayerDown = classloader.getResourceAsStream("playerDown.png");
                 var streamPatron = classloader.getResourceAsStream("patron.png");
                 var streamPatronLeft = classloader.getResourceAsStream("patronLeft.png");
                 var streamPatronUp = classloader.getResourceAsStream("patronUp.png");
                 var streamPatronDown = classloader.getResourceAsStream("patronDown.png");) {

                if (streamSpider != null && streamPlayer != null && streamPlayerLeft != null && streamPlayerUp != null && streamPlayerDown != null
                        && streamPatron != null && streamPatronLeft != null && streamPatronUp != null && streamPatronDown != null) {
                    images.put("imageSpider", new Image(streamSpider));
                    images.put("imagePlayer", new Image(streamPlayer));
                    images.put("imagePlayerLeft", new Image(streamPlayerLeft));
                    images.put("imagePlayerUp", new Image(streamPlayerUp));
                    images.put("imagePlayerDown", new Image(streamPlayerDown));
                    images.put("imagePatron", new Image(streamPatron));
                    images.put("imagePatronLeft", new Image(streamPatronLeft));
                    images.put("imagePatronUp", new Image(streamPatronUp));
                    images.put("imagePatronDown", new Image(streamPatronDown));
                } else {
                    System.out.println("Не удалось получить изображения!");
                }
            }
        } catch (Exception e) {
            System.out.println("Не удалось получить изображения! " + e.getMessage());
        }
    }

    private void initializeColumns() {
        TableColumn<TCell[], TCell> column;

        for (int i = 0; i < 10; i++) {
            column = new TableColumn<>();
            column.setMinWidth(100);
            column.setCellValueFactory(getCallbackLambda(i));
            column.setCellFactory(col -> new Cell(images));
            table.getColumns().add(column);
        }
    }

    private Callback<TableColumn.CellDataFeatures<TCell[], TCell>, ObservableValue<TCell>> getCallbackLambda(int i) {
        return p -> {
            TCell[] x = p.getValue();
            if (x != null && x.length > 0) {
                return new SimpleObjectProperty<>(x[i]);
            } else {
                return new SimpleObjectProperty<>(null);
            }
        };
    }

    private void initializeRows() {
        var data= FXCollections.observableArrayList(
                playField.getSubarray(0),
                playField.getSubarray(1),
                playField.getSubarray(2),
                playField.getSubarray(3),
                playField.getSubarray(4),
                playField.getSubarray(5),
                playField.getSubarray(6),
                playField.getSubarray(7),
                playField.getSubarray(8),
                playField.getSubarray(9));
        table.setItems(data);
    }

    private void launchSpiders() {
        Coordinates[] routeCoord, boundaryCoord;

        routeCoord = new Coordinates[] {new Coordinates(0, 3), new Coordinates(1, 3), new Coordinates(2, 3), new Coordinates(3, 3),
                new Coordinates(4, 3), new Coordinates(5, 3), new Coordinates(6, 3), new Coordinates(6, 4), new Coordinates(5, 4), new Coordinates(4, 4),
                new Coordinates(3, 4), new Coordinates(2, 4), new Coordinates(1, 4), new Coordinates(0, 4)};
        boundaryCoord = new Coordinates[] {new Coordinates(0, 2), new Coordinates(1, 2), new Coordinates(2, 2), new Coordinates(3, 2), new Coordinates(4, 2),
                new Coordinates(5, 2), new Coordinates(6, 2), new Coordinates(7, 3),
                new Coordinates(7, 4), new Coordinates(6, 5), new Coordinates(5, 5),
                new Coordinates(4, 5), new Coordinates(3, 5), new Coordinates(2, 5), new Coordinates(1, 5), new Coordinates(0, 5)};
        launchSpider(routeCoord, boundaryCoord, RoutePassing.InCircles);


        routeCoord = new Coordinates[] {new Coordinates(1, 8), new Coordinates(1, 7), new Coordinates(1, 6), new Coordinates(2, 6),
                new Coordinates(3, 6), new Coordinates(4, 6), new Coordinates(5, 6), new Coordinates(6, 6), new Coordinates(6, 7), new Coordinates(6, 8)};
        boundaryCoord = new Coordinates[] {new Coordinates(1, 5), new Coordinates(2, 5), new Coordinates(3, 5), new Coordinates(4, 5),
                new Coordinates(5, 5), new Coordinates(6, 5),
                new Coordinates(7, 6), new Coordinates(7, 7), new Coordinates(7, 8), new Coordinates(6, 9),
                new Coordinates(5, 8), new Coordinates(5, 7), new Coordinates(4, 7), new Coordinates(3, 7),
                new Coordinates(2, 7), new Coordinates(2, 8),
                new Coordinates(1, 9), new Coordinates(0, 8), new Coordinates(0, 7), new Coordinates(0, 6)};
        launchSpider(routeCoord, boundaryCoord, RoutePassing.ToTheEndAndBack);
    }

    private void launchSpider(Coordinates[] routeCoordinates, Coordinates[] boundaryCoordinates, RoutePassing routePassing) {
        new Spider(routeCoordinates, boundaryCoordinates, routePassing, player, playField, table).launchSpider();
    }

    private void onKeyPressed(KeyEvent event) {
        KeyCode keyCodeInit = event.getCode();

        if (keyCodeInit == KeyCode.ENTER) {
            new Patron(player.getCoordinates(), player.getMovingDirection(), playField, table).launchPatron();
        } else if (keyCodeInit == KeyCode.RIGHT || keyCodeInit == KeyCode.LEFT || keyCodeInit == KeyCode.UP || keyCodeInit == KeyCode.DOWN) {
            String keyCodeString = keyCodeInit.toString().toLowerCase();
            String keyCode = keyCodeString.substring(0, 1).toUpperCase() + keyCodeString.substring(1);

            player.movePlayer(MovingDirection.valueOf(keyCode));
        }
    }

    private TCell[][] buildArrayForPlayField() {
        TCell[][] array = new TCell[12][12];

        array[0] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[1] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty), new TCell(Character.Empty), new TCell(Character.Wall)};


        array[2] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[3] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[4] = new TCell[] {new TCell(Character.Empty), new TCell(Character.Empty), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Exit)};


        array[5] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[6] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty),
                new TCell(Character.Wall), new TCell(Character.Empty), new TCell(Character.Empty), new TCell(Character.Empty), new TCell(Character.Wall)};


        array[7] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall),
                new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[8] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall),
                new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        array[9] = new TCell[] {new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall),
                new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall), new TCell(Character.Wall)};


        return array;
    }

}