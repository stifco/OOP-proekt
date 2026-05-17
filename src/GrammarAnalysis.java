import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Прости анализатори на граматики — без да модифицират входа,
 * връщат булева оценка на дадено свойство. Тук са командите
 * empty и chomsky от заданието.
 */
public class GrammarAnalysis {

    /**
     * Проверка дали езикът на граматиката е празен.
     * Идеята: намираме множеството "продуктивни" нетерминали — тези,
     * от които може да се изведе низ само от терминали. Започваме с
     * празно множество и многократно добавяме нови, докато не настъпят
     * промени. Езикът е празен само ако стартовият символ не е
     * продуктивен.
     *
     * @return true ако езикът е празен
     */
    public boolean isEmpty(Grammar g) {
        Set<Symbol> productive = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < g.getRules().size(); i++) {
                Rule r = g.getRules().get(i);
                if (productive.contains(r.getLhs())) continue;
                if (isProductiveRhs(r.getRhs(), productive)) {
                    productive.add(r.getLhs());
                    changed = true;
                }
            }
        }
        return !productive.contains(g.getStartSymbol());
    }

    /** Дясната страна е "продуктивна" ако всичките ѝ нетерминали са продуктивни. */
    private boolean isProductiveRhs(List<Symbol> rhs, Set<Symbol> productive) {
        for (int i = 0; i < rhs.size(); i++) {
            Symbol s = rhs.get(i);
            if (s.isTerminal()) continue;
            if (!productive.contains(s)) return false;
        }
        return true;
    }

    /**
     * Проверка дали граматиката е в нормална форма на Чомски.
     * Позволени са само три типа правила: A → BC (два нетерминала),
     * A → a (един терминал), и S → ε (само за началния символ).
     *
     * @return true ако граматиката отговаря на формата
     */
    public boolean isChomsky(Grammar g) {
        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            if (!isChomskyRule(r, g.getStartSymbol())) {
                return false;
            }
        }
        return true;
    }

    /** Проверка дали едно конкретно правило е в позволената форма. */
    private boolean isChomskyRule(Rule r, Symbol startSymbol) {
        List<Symbol> rhs = r.getRhs();
        if (rhs.isEmpty()) {
            return r.getLhs().equals(startSymbol);
        }
        if (rhs.size() == 1) {
            return rhs.get(0).isTerminal();
        }
        if (rhs.size() == 2) {
            return rhs.get(0).isNonTerminal() && rhs.get(1).isNonTerminal();
        }
        return false;
    }
}