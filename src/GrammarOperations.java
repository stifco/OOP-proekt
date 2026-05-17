import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Трите алгебрични операции върху граматики — обединение, конкатенация
 * и итерация (звезда на Клини). И трите работят по един и същ принцип:
 * въвеждат нов начален символ, добавят съответните правила за него
 * и копират правилата на входните граматики.
 */
public class GrammarOperations {

    /**
     * Обединение на две граматики.
     * Въвежда нов начален символ S' с две правила: S' → S1 и S' → S2,
     * след което копира правилата и на двете граматики.
     *
     * @return нова граматика, описваща обединението на езиците
     */
    public Grammar union(Grammar g1, Grammar g2, String newId) {
        Symbol newStart = findUnusedNonTerminal(g1, g2);
        Grammar result = new Grammar(newId, newStart);

        List<Symbol> rhs1 = new ArrayList<>();
        rhs1.add(g1.getStartSymbol());
        result.addRule(new Rule(newStart, rhs1));

        List<Symbol> rhs2 = new ArrayList<>();
        rhs2.add(g2.getStartSymbol());
        result.addRule(new Rule(newStart, rhs2));

        for (int i = 0; i < g1.getRules().size(); i++) {
            result.addRule(g1.getRules().get(i));
        }
        for (int i = 0; i < g2.getRules().size(); i++) {
            result.addRule(g2.getRules().get(i));
        }

        return result;
    }

    /**
     * Конкатенация на две граматики.
     * Новият начален символ има едно правило S' → S1 S2.
     *
     * @return нова граматика за конкатенацията на езиците
     */
    public Grammar concat(Grammar g1, Grammar g2, String newId) {
        Symbol newStart = findUnusedNonTerminal(g1, g2);
        Grammar result = new Grammar(newId, newStart);

        List<Symbol> rhs = new ArrayList<>();
        rhs.add(g1.getStartSymbol());
        rhs.add(g2.getStartSymbol());
        result.addRule(new Rule(newStart, rhs));

        for (int i = 0; i < g1.getRules().size(); i++) {
            result.addRule(g1.getRules().get(i));
        }
        for (int i = 0; i < g2.getRules().size(); i++) {
            result.addRule(g2.getRules().get(i));
        }

        return result;
    }

    /**
     * Итерация (звезда на Клини) върху граматика.
     * Новият начален символ има две правила: S' → S S' (рекурсия)
     * и S' → ε (базов случай). Резултатът описва нула или повече
     * последователности от думи на оригиналния език.
     */
    public Grammar iter(Grammar g, String newId) {
        Symbol newStart = findUnusedNonTerminal(g, null);
        Grammar result = new Grammar(newId, newStart);

        List<Symbol> rhs = new ArrayList<>();
        rhs.add(g.getStartSymbol());
        rhs.add(newStart);
        result.addRule(new Rule(newStart, rhs));

        result.addRule(new Rule(newStart, new ArrayList<>()));

        for (int i = 0; i < g.getRules().size(); i++) {
            result.addRule(g.getRules().get(i));
        }

        return result;
    }

    /**
     * Намира главна буква, която не се използва от подадените граматики.
     * Тя ще стане новият начален символ. Ако всички 26 букви са заети,
     * хвърля изключение — но в практиката това почти не се случва.
     */
    private Symbol findUnusedNonTerminal(Grammar g1, Grammar g2) {
        Set<String> used = new HashSet<>();
        for (Symbol s : g1.getNonTerminals()) {
            used.add(s.getName());
        }
        if (g2 != null) {
            for (Symbol s : g2.getNonTerminals()) {
                used.add(s.getName());
            }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!used.contains(String.valueOf(c))) {
                return new Symbol(String.valueOf(c), false);
            }
        }
        throw new RuntimeException("No unused non-terminal letter available");
    }
}