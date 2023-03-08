package homework;

import java.util.HashMap;
import java.util.Map;

public class Testing {
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) throws Throwable {
        testAddBanknotes_Atm();
        testGetBanknotesForSum_Atm();
        testGetBalance_Atm();
        testAddAmount_AtmCell();
        testGetBalance_AtmCell();
    }

    public static void testAddBanknotes_Atm() {
        String scenario = "Добавление банкнот в АТМ.";
        Map<Banknote, Integer> amounts = new HashMap<>();
        amounts.put(Banknote.rubles5000, 10);
        amounts.put(Banknote.rubles1000, 20);
        amounts.put(Banknote.rubles500, 30);
        amounts.put(Banknote.rubles200, 40);
        amounts.put(Banknote.rubles100, 50);

        Atm AtmObj = new Atm();

        try {
            AtmObj.addBanknotes(amounts);
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            System.err.printf(errMessage);
            return;
        }

        for (var entry: AtmObj.getCells().entrySet()) {
            if (entry.getValue().getAmount() != amounts.get(entry.getKey())) {
                String errMessage1 = "Для банкноты " + entry.getKey() + " не удалось корректно внести деньги в банкомат!";
                String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
                System.err.printf(errMessage2);
                return;
            }
        }

        System.out.printf(ANSI_GREEN + "\"%s\" passed %n" + ANSI_RESET, scenario);
    }

    public static void testGetBanknotesForSum_Atm() throws Throwable {
        String scenario = "Получение банкнот из АТМ.";

        try {
            testGetBanknotesForSum_Atm1(scenario);
            testGetBanknotesForSum_Atm2(scenario);
        } catch (Throwable e) {
            System.err.printf(e.getMessage());
            return;
        }

        System.out.printf(ANSI_GREEN + "\"%s\" passed %n" + ANSI_RESET, scenario);
    }

    public static void testGetBanknotesForSum_Atm1(String scenario) throws Throwable {
        Map<Banknote, Integer> amounts = new HashMap<>();
        amounts.put(Banknote.rubles5000, 1);
        amounts.put(Banknote.rubles1000, 1);
        amounts.put(Banknote.rubles500, 0);
        amounts.put(Banknote.rubles200, 1);
        amounts.put(Banknote.rubles100, 1);
        Map<Banknote, Integer> sumBanknotes;
        Atm AtmObj = new Atm();
        AtmObj.addBanknotes(amounts);

        try {
            sumBanknotes = AtmObj.getBanknotesForSum(12600);
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            throw new Throwable(errMessage);
        }

        boolean condition = sumBanknotes.get(Banknote.rubles5000) == 2 && sumBanknotes.get(Banknote.rubles1000) == 2
                && sumBanknotes.get(Banknote.rubles500) == 0 && sumBanknotes.get(Banknote.rubles200) == 3
                && (sumBanknotes.get(Banknote.rubles100) == null || sumBanknotes.get(Banknote.rubles100) == 0);
        if (!condition) {
            String errMessage1 = "Для суммы 12600 получено некорректное разложение по банкнотам (без 500).";
            String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
            throw new Throwable(errMessage2);
        }
    }

    public static void testGetBanknotesForSum_Atm2(String scenario) throws Throwable {
        Map<Banknote, Integer> amounts = new HashMap<>();
        amounts.put(Banknote.rubles5000, 10);
        amounts.put(Banknote.rubles1000, 10);
        amounts.put(Banknote.rubles500, 10);
        amounts.put(Banknote.rubles200, 10);
        amounts.put(Banknote.rubles100, 10);
        Map<Banknote, Integer> sumBanknotes;
        Atm AtmObj = new Atm();
        AtmObj.addBanknotes(amounts);

        try {
            sumBanknotes = AtmObj.getBanknotesForSum(12600);
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            throw new Throwable(errMessage);
        }

        boolean condition = sumBanknotes.get(Banknote.rubles5000) == 2
                && sumBanknotes.get(Banknote.rubles1000) == 2
                && sumBanknotes.get(Banknote.rubles500) == 1
                && sumBanknotes.get(Banknote.rubles100) == 1
                && (sumBanknotes.get(Banknote.rubles200) == 0 || sumBanknotes.get(Banknote.rubles200) == null);
        if (!condition) {
            String errMessage1 = "Для суммы 12600 получено некорректное разложение по банкнотам (c 500).";
            String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
            throw new Throwable(errMessage2);
        }
    }

    public static void testGetBalance_Atm() {
        String scenario = "Получение баланса из АТМ.";
        int balance = 0;
        Atm AtmObj = new Atm();

        Map<Banknote, Integer> amounts = new HashMap<>();
        amounts.put(Banknote.rubles5000, 2);
        amounts.put(Banknote.rubles1000, 10);
        amounts.put(Banknote.rubles500, 4);
        amounts.put(Banknote.rubles200, 5);
        amounts.put(Banknote.rubles100, 3);
        AtmObj.addBanknotes(amounts);

        try {
            balance = AtmObj.getBalance();
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            System.err.printf(errMessage);
            return;
        }

        if (balance == 23300) {
            System.out.printf(ANSI_GREEN + "\"%s\" passed %n" + ANSI_RESET, scenario);
        } else {
            String errMessage1 = "Баланс не равен требуемому значению 23300.";
            String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
            System.err.printf(errMessage2);
            return;
        }
    }

    public static void testAddAmount_AtmCell() {
        String scenario = "Добавление количества в ячейку АТМ.";
        AtmCell Atmcell = new AtmCell(Banknote.rubles100);

        try {
            Atmcell.addAmount(4);
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            System.err.printf(errMessage);
            return;
        }

        if (Atmcell.getAmount() == 4) {
            System.out.printf(ANSI_GREEN + "\"%s\" passed %n" + ANSI_RESET, scenario);
        } else {
            String errMessage1 = "Для банкноты " + Banknote.rubles100 + " не удалось корректно добавить количество в ячейку АТМ.";
            String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
            System.err.printf(errMessage2);
            return;
        }
    }

    public static void testGetBalance_AtmCell() {
        String scenario = "Получение баланса из ячейки АТМ.";
        int balance = 0;
        AtmCell Atmcell = new AtmCell(Banknote.rubles100);
        Atmcell.addAmount(14);

        try {
            balance = Atmcell.getBalance();
        } catch (Throwable e) {
            String errMessage = String.format("\"%s\" fails with message \"%s\" %n", scenario, e.getMessage());
            System.err.printf(errMessage);
            return;
        }

        if (balance == 1400) {
            System.out.printf(ANSI_GREEN + "\"%s\" passed %n" + ANSI_RESET, scenario);
        } else {
            String errMessage1 = "Для банкноты " + Banknote.rubles100 + " не удалось корректно посчитать остаток в ячейке АТМ.";
            String errMessage2 = String.format("\"%s\" fails with message \"%s\" %n", scenario, errMessage1);
            System.err.printf(errMessage2);
            return;
        }
    }
}