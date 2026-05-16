import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrammarAnalysis {

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

    private boolean isProductiveRhs(List<Symbol> rhs, Set<Symbol> productive) {
        for (int i = 0; i < rhs.size(); i++) {
            Symbol s = rhs.get(i);
            if (s.isTerminal()) continue;
            if (!productive.contains(s)) return false;
        }
        return true;
    }

    public boolean isChomsky(Grammar g) {
        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            if (!isChomskyRule(r, g.getStartSymbol())) {
                return false;
            }
        }
        return true;
    }

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