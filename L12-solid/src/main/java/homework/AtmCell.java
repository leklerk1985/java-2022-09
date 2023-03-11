package homework;

public class AtmCell {
    private Banknote banknote;
    private int amount;

    AtmCell(Banknote banknote) {
        this.banknote = banknote;
        amount = 0;
    }

    public int getAmount() {
        return amount;
    }
    public int getBalance() {
        return amount * banknote.getValue();
    }

    public void addAmount(int amount) {
        this.amount = this.amount + amount;
    }
}
