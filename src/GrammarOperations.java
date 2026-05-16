import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GrammarOperations {

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