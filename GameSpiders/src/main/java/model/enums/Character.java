package model.enums;

public enum Character {
    Empty, Wall, Spider, Player, Patron, Exit;

    public boolean isEmptyOrPlayer() {
        return (this == Empty || this == Player);
    }
}
