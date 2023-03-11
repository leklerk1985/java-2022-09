package homework;

import java.util.HashMap;
import java.util.Map;

public class Runner {
    public static void main(String[] args) {
        Map<Banknote, Integer> sumBanknotes;
        Map<Banknote, Integer> amounts = new HashMap<>();
        amounts.put(Banknote.rubles5000, 1);
        amounts.put(Banknote.rubles1000, 2);
        amounts.put(Banknote.rubles500, 0);
        amounts.put(Banknote.rubles200, 1);
        amounts.put(Banknote.rubles100, 1);

        Atm AtmObj = new Atm();
        AtmObj.addBanknotes(amounts);

        try {
            sumBanknotes = AtmObj.getBanknotesForSum(12600);
        } catch (RuntimeException e) {
            System.err.printf(e.getMessage());
        }
    }
}
