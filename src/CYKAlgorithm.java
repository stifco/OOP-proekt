import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Алгоритъм CYK (Cocke–Younger–Kasami) за проверка дали дадена дума
 * принадлежи на езика на граматика в нормална форма на Чомски.
 * Използва се динамично програмиране — попълва се таблица, в която
 * клетката [i][j] съдържа множеството нетерминали, от които може да
 * се изведе поднизът от позиция i до позиция j. Сложност O(n³·|G|).
 */
public class CYKAlgorithm {

    /**
     * Проверява дали word е в езика на g.
     * Граматиката трябва да е в CNF — иначе резултатът не е надежден.
     *
     * @param g граматика (в нормална форма на Чомски)
     * @param word дума за проверка
     * @return true ако думата принадлежи на езика
     */
    public boolean accepts(Grammar g, String word) {
        if (word.isEmpty()) {
            for (int i = 0; i < g.getRules().size(); i++) {
                Rule r = g.getRules().get(i);
                if (r.getLhs().equals(g.getStartSymbol()) && r.getRhs().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        int n = word.length();
        List<List<Set<Symbol>>> table = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Set<Symbol>> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(new HashSet<>());
            }
            table.add(row);
        }

        // Първа стъпка — поднизове с дължина 1: търсим правила A -> a
        for (int i = 0; i < n; i++) {
            String c = String.valueOf(word.charAt(i));
            for (int k = 0; k < g.getRules().size(); k++) {
                Rule r = g.getRules().get(k);
                List<Symbol> rhs = r.getRhs();
                if (rhs.size() == 1 && rhs.get(0).isTerminal()
                        && rhs.get(0).getName().equals(c)) {
                    table.get(i).get(i).add(r.getLhs());
                }
            }
        }

        // Втора стъпка — поднизове с нарастваща дължина: търсим правила A -> BC
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                for (int k = i; k < j; k++) {
                    for (int m = 0; m < g.getRules().size(); m++) {
                        Rule r = g.getRules().get(m);
                        List<Symbol> rhs = r.getRhs();
                        if (rhs.size() == 2) {
                            Symbol B = rhs.get(0);
                            Symbol C = rhs.get(1);
                            if (table.get(i).get(k).contains(B)
                                    && table.get(k + 1).get(j).contains(C)) {
                                table.get(i).get(j).add(r.getLhs());
                            }
                        }
                    }
                }
            }
        }

        return table.get(0).get(n - 1).contains(g.getStartSymbol());
    }
}