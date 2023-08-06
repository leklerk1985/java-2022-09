package model.classes;

import core.Coordinates;
import core.PlayField;
import core.TCell;
import javafx.scene.control.TableView;
import model.enums.Character;
import model.enums.MovingDirection;
import javax.swing.*;
import java.util.HashMap;
import static utils.UtilGeneral.moveCharacter;
import static core.Coordinates.fillCoordinatesOfNextCell;

public class Player {
    private Coordinates coordinates;
    private MovingDirection movingDirection = MovingDirection.Right;
    private boolean isKilled = false;
    private boolean isWon = false;
    private final PlayField playField;
    private final TableView<TCell[]> table;

    public Player(Coordinates coordinates, PlayField playField, TableView<TCell[]> table) {
        this.coordinates = coordinates;
        this.playField = playField;
        this.table = table;

        setPlayerInPlayField();
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public MovingDirection getMovingDirection() {
        return movingDirection;
    }

    public void setIsKilled() {
        isKilled = true;
        JOptionPane.showMessageDialog(null, "Вы проиграли!", "Уведомление", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setIsWon() {
        isWon = true;
        JOptionPane.showMessageDialog(null, "Вы победили!", "Поздравление", JOptionPane.INFORMATION_MESSAGE);
    }

    public boolean isKilled() {
        return isKilled;
    }

    public boolean isWon() {
        return isWon;
    }

    public void movePlayer(MovingDirection newMovingDirection) {
        if (newMovingDirection != movingDirection) {
            movingDirection = newMovingDirection;
            playField.setMovingDirection(movingDirection, coordinates.get(1), coordinates.get(2));
            table.refresh();
            return;
        }

        Coordinates newCoordinates = new Coordinates();
        fillCoordinatesOfNextCell(newCoordinates, coordinates, movingDirection);

        var characterObjects = new HashMap<String, Object>();
        characterObjects.put("Player", this);

        boolean success = moveCharacter(Character.Player, movingDirection, coordinates, newCoordinates, playField, table, characterObjects);
        if (success) {
            setCoordinates(newCoordinates);
        }
    }

    private void setPlayerInPlayField() {
        playField.setCharacter(Character.Player, coordinates.get(1), coordinates.get(2));
        playField.setMovingDirection(movingDirection, coordinates.get(1), coordinates.get(2));
    }
}
