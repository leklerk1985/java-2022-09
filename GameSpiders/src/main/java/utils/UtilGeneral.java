package utils;

import core.Coordinates;
import core.PlayField;
import core.TCell;
import javafx.scene.control.TableView;
import model.classes.Patron;
import model.classes.Player;
import model.enums.Character;
import model.enums.MovingDirection;
import java.util.Map;
import static core.Coordinates.coordinatesAreValid;

public class UtilGeneral {

    public static final int PLAYFIELD_WIDTH = 10;
    public static final int PLAYFIELD_HEIGHT = 10;

    public static boolean moveCharacter(Character character, MovingDirection movingDirection, Coordinates oldCoordinates, Coordinates newCoordinates,
                                        PlayField playField, TableView<TCell[]> table, Map<String, Object> characterObjects) {

        if (character == Character.Patron) {
            synchronized (playField) {
                if (!coordinatesAreValid(newCoordinates)) {
                    if (oldCoordinates != null) {
                        playField.setCharacter(Character.Empty, oldCoordinates.get(1), oldCoordinates.get(2));
                    }

                    var patron = (Patron) characterObjects.get("Patron");
                    patron.setEndWasReached();
                } else if (cellIsEmpty(newCoordinates, playField)) {
                    playField.setCharacter(character, newCoordinates.get(1), newCoordinates.get(2));
                    playField.setMovingDirection(movingDirection, newCoordinates.get(1), newCoordinates.get(2));
                    if (oldCoordinates != null) {
                        playField.setCharacter(Character.Empty, oldCoordinates.get(1), oldCoordinates.get(2));
                    }
                } else {
                    if (playField.getCharacter(newCoordinates.get(1), newCoordinates.get(2)) == Character.Wall) {
                        playField.setCharacter(Character.Empty, newCoordinates.get(1), newCoordinates.get(2));
                    }
                    if (oldCoordinates != null) {
                        playField.setCharacter(Character.Empty, oldCoordinates.get(1), oldCoordinates.get(2));
                    }

                    var patron = (Patron) characterObjects.get("Patron");
                    patron.setEndWasReached();
                }
            }
        } else if (character == Character.Player) {
            if (!coordinatesAreValid(newCoordinates)) {
                return false;
            }

            if (!cellIsEmpty(newCoordinates, playField) && !cellIsExit(newCoordinates, playField)) {
                return false;
            }

            synchronized (playField) {
                if (cellIsExit(newCoordinates, playField)) {
                    var player = (Player) characterObjects.get("Player");
                    player.setIsWon();
                } else {
                    playField.setCharacter(character, newCoordinates.get(1), newCoordinates.get(2));
                    playField.setMovingDirection(movingDirection, newCoordinates.get(1), newCoordinates.get(2));
                    if (oldCoordinates != null) {
                        playField.setCharacter(Character.Empty, oldCoordinates.get(1), oldCoordinates.get(2));
                    }
                }
            }
        } else {    // character == Character.Spider
            if (!coordinatesAreValid(newCoordinates) || !cellIsEmptyOrPlayer(newCoordinates, playField)) {
                return false;
            }

            synchronized (playField) {
                if (cellIsEmpty(newCoordinates, playField)) {
                    playField.setCharacter(character, newCoordinates.get(1), newCoordinates.get(2));
                    playField.setMovingDirection(movingDirection, newCoordinates.get(1), newCoordinates.get(2));
                    if (oldCoordinates != null) {
                        playField.setCharacter(Character.Empty, oldCoordinates.get(1), oldCoordinates.get(2));
                    }
                } else {
                    playField.setCharacterKilled(newCoordinates.get(1), newCoordinates.get(2));

                    var player = (Player) characterObjects.get("Player");
                    player.setIsKilled();
                }
            }
        }

        table.refresh();
        sleep(getMillisForCharacter(character));

        return true;
    }

    private static long getMillisForCharacter(Character character) {
        if (character == Character.Player) {
            return 200L;
        } else if (character == Character.Patron) {
            return 100L;
        } else { // character == Character.Spider
            return 750L;
        }
    }

    private static boolean cellIsEmpty(Coordinates coordinates, PlayField playField) {
        return playField.getCharacter(coordinates.get(1), coordinates.get(2)) == Character.Empty;
    }

    private static boolean cellIsEmptyOrPlayer(Coordinates coordinates, PlayField playField) {
        return playField.getCharacter(coordinates.get(1), coordinates.get(2)).isEmptyOrPlayer();
    }

    private static boolean cellIsExit(Coordinates coordinates, PlayField playField) {
        return playField.getCharacter(coordinates.get(1), coordinates.get(2)) == Character.Exit;
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

}




