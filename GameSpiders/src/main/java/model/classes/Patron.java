package model.classes;

import core.Coordinates;
import core.PlayField;
import core.TCell;
import javafx.scene.control.TableView;
import model.enums.Character;
import model.enums.MovingDirection;
import java.util.HashMap;
import static utils.UtilGeneral.moveCharacter;
import static core.Coordinates.fillCoordinatesOfNextCell;
import static core.Coordinates.calculateCoordinatesOfNextCell;

public class Patron {
    private final Coordinates playerCoordinates;
    private final Coordinates initialCoordinates = new Coordinates();
    private final MovingDirection movingDirection;
    private boolean endWasReached = false;
    private final PlayField playField;
    private final TableView<TCell[]> table;

    public Patron(Coordinates playerCoordinates, MovingDirection movingDirection, PlayField playField, TableView<TCell[]> table) {
        this.playerCoordinates = playerCoordinates;
        this.movingDirection = movingDirection;
        this.playField = playField;
        this.table = table;

        setInitialCoordinatesByPlayerCoordinates();
    }

    public void setEndWasReached() {
        endWasReached = true;
    }

    public void launchPatron() {
        new Thread(this::patronIsFlying).start();
    }

    private void setInitialCoordinatesByPlayerCoordinates() {
        fillCoordinatesOfNextCell(initialCoordinates, playerCoordinates, movingDirection);
    }

    private void patronIsFlying() {
        Coordinates prevCoordinates = null;
        Coordinates currCoordinates = initialCoordinates;

        while (!endWasReached) {
            movePatron(prevCoordinates, currCoordinates);

            prevCoordinates = currCoordinates;
            currCoordinates = calculateCoordinatesOfNextCell(currCoordinates, movingDirection);
        }
    }

    private void movePatron(Coordinates oldCoordinates, Coordinates newCoordinates) {
        var characterObjects = new HashMap<String, Object>();
        characterObjects.put("Patron", this);

        moveCharacter(Character.Patron, movingDirection, oldCoordinates, newCoordinates, playField, table, characterObjects);
    }

}
