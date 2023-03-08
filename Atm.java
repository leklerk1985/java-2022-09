package homework;

import java.util.HashMap;
import java.util.Map;

public class Atm {
    private Map<Banknote, AtmCell> cells;
    private Banknote[] banknotes = Banknote.values();

    Atm () {
        cells = new HashMap<>();
        for (Banknote banknote: banknotes) {
            cells.put(banknote, new AtmCell(banknote));
        }
    }

    public int getBalance() {
        int balance = 0;
        for (var entry: cells.entrySet()) {
            balance += entry.getValue().getBalance();
        }
        return balance;
    }

    public Map<Banknote, AtmCell> getCells() {
        return cells;
    }

    public void addBanknotes(Map<Banknote, Integer> amounts) {
        AtmCell currentCell;
        for (var entry: amounts.entrySet()) {
            currentCell = cells.get(entry.getKey());
            currentCell.addAmount(entry.getValue());
        }
    }

    public Map<Banknote, Integer> getBanknotesForSum(int sum) {
        // Инициализация переменных.
        Map<Banknote, Integer> sumBanknotes = new HashMap<>();

        Map<Banknote, Integer> cellAmounts = new HashMap<>();
        for (Banknote banknote: banknotes) {
            cellAmounts.put(banknote, cells.get(banknote).getAmount());
        }

        int currentSum = 0;
        int currentValue;
        int currentCellAmount;
        int currentAmount;

        // Выполнение действий.
        for (Banknote banknote: banknotes) {
            if (currentSum == sum)
                break;

            currentValue = banknote.getValue();
            currentCellAmount = cellAmounts.get(banknote);
            currentAmount = 0;

            while (true) {
                if (currentCellAmount == 0) {
                    break;
                }

                if (currentSum + currentValue <= sum) {
                    currentSum += currentValue;
                    currentCellAmount--;
                    currentAmount++;
                } else {
                    break;
                }
            }

            sumBanknotes.put(banknote, currentAmount);
        }

        if (currentSum == sum) {
            return sumBanknotes;
        } else {
            throw new RuntimeException("Для выдачи суммы недостаточно банкнот!");
        }
    }
}