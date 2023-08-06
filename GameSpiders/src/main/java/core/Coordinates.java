package core;

import model.enums.MovingDirection;

import java.util.Arrays;
import java.util.List;
import static utils.UtilGeneral.PLAYFIELD_HEIGHT;
import static utils.UtilGeneral.PLAYFIELD_WIDTH;

public class Coordinates {
    private final int[] coordinates;

    public Coordinates(int firstCoordinate, int secondCoordinate) {
        coordinates = new int[2];
        coordinates[0] = firstCoordinate;
        coordinates[1] = secondCoordinate;
    }

    public Coordinates() {
        coordinates = new int[2];
    }

    public void copyCoordinates(Coordinates coordinates) {
        setCoordinate(coordinates.get(1), 1);
        setCoordinate(coordinates.get(2), 2);
    }

    public void setCoordinate(int value, int coordinateNumber) {
        coordinates[coordinateNumber-1] = value;
    }

    public void setCoordinates(int firstValue, int secondValue) {
        coordinates[0] = firstValue;
        coordinates[1] = secondValue;
    }

    public int get(int coordinateNumber) {
        return coordinates[coordinateNumber-1];
    }

    public static boolean coordinatesAreValid(Coordinates coordinates) {
        if (coordinates == null) {
            return false;
        }

        return coordinates.get(1) >= 0 && coordinates.get(1) < PLAYFIELD_HEIGHT && coordinates.get(2) >= 0 && coordinates.get(2) < PLAYFIELD_WIDTH;
    }

    public static boolean coordinatesArePermitted(Coordinates coordinates, Coordinates currentSpiderCoordinates, List<Coordinates> cyclicPathCells, PlayField playField) {
        if (!coordinatesAreValid(coordinates)) {
            return false;
        }

        if (cyclicPathCells.contains(coordinates)) {
            return false;
        }

        boolean isEmptyOrPlayer = playField.getCharacter(coordinates.get(1), coordinates.get(2)).isEmptyOrPlayer();
        boolean isThisSpider = coordinates.equals(currentSpiderCoordinates);

        return isEmptyOrPlayer || isThisSpider;
    }

    public static void fillCoordinatesOfNextCell(Coordinates newCoordinates, Coordinates oldCoordinates, MovingDirection movingDirection) {
        if (movingDirection == null) {
            newCoordinates.copyCoordinates(oldCoordinates);
            return;
        }

        switch (movingDirection) {
            case Right -> {
                newCoordinates.setCoordinates(oldCoordinates.get(1), oldCoordinates.get(2) + 1);
            }
            case Left -> {
                newCoordinates.setCoordinates(oldCoordinates.get(1), oldCoordinates.get(2) - 1);
            }
            case Up -> {
                newCoordinates.setCoordinates(oldCoordinates.get(1) - 1, oldCoordinates.get(2));
            }
            case Down -> {
                newCoordinates.setCoordinates(oldCoordinates.get(1) + 1, oldCoordinates.get(2));
            }
        }
    }

    public static Coordinates calculateCoordinatesOfNextCell(Coordinates oldCoordinates, MovingDirection movingDirection) {
        if (movingDirection == null) {
            return null;
        }

        Coordinates newCoordinates = new Coordinates();
        fillCoordinatesOfNextCell(newCoordinates, oldCoordinates, movingDirection);
        return newCoordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return (coordinates[0] == that.coordinates[0] && coordinates[1] == that.coordinates[1]);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }
}
