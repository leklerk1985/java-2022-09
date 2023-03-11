package homework;

public enum Banknote {
    rubles5000(5000),  rubles1000(1000), rubles500(500), rubles200(200), rubles100(100);

    private int value;
    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
