package core;

import model.enums.Character;
import model.enums.MovingDirection;

public class PlayField {
    private final TCell[][] array;

    public PlayField(TCell[][] array) {
        this.array = array;
    }

    public TCell[] getSubarray(int index) {
        return array[index];
    }

    public synchronized Character getCharacter(int firstIndex, int secondIndex) {
        return array[firstIndex][secondIndex].getCharacter();
    }

    public synchronized void setCharacter(Character character, int firstIndex, int secondIndex) {
        array[firstIndex][secondIndex].setCharacter(character);
        if (character == Character.Empty || character == Character.Wall) {
            setMovingDirection(null, firstIndex, secondIndex);
        }
    }

    public synchronized void setMovingDirection(MovingDirection movingDirection, int firstIndex, int secondIndex) {
        array[firstIndex][secondIndex].setMovingDirection(movingDirection);
    }

    public synchronized void setCharacterKilled(int firstIndex, int secondIndex) {
        array[firstIndex][secondIndex].setCharacterIsKilled();
    }
}
