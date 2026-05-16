import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChomskyTransformer {

    private int counter;

    public Grammar toChomsky(Grammar g, String newId) {
        this.counter = 1;

        Grammar copy = new Grammar(newId, g.getStartSymbol());
        for (int i = 0; i < g.getRules().size(); i++) {
            copy.addRule(g.getRules().get(i));
        }

        Grammar step1 = removeEpsilonRules(copy, newId);
        Grammar step2 = removeUnitRules(step1, newId);
        Grammar step3 = replaceTerminalsInMixed(step2, newId);
        Grammar step4 = breakLongRules(step3, newId);

        return step4;
    }

    private Grammar removeEpsilonRules(Grammar g, String newId) {
        Set<Symbol> nullable = findNullable(g);
        Grammar result = new Grammar(newId, g.getStartSymbol());

        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            if (r.getRhs().isEmpty()) continue;

            List<List<Symbol>> combinations = new ArrayList<>();
            expandHelper(r.getRhs(), nullable, 0, new ArrayList<>(), combinations);

            for (int j = 0; j < combinations.size(); j++) {
                List<Symbol> rhs = combinations.get(j);
                if (!rhs.isEmpty()) {
                    Rule newRule = new Rule(r.getLhs(), rhs);
                    if (!containsRule(result.getRules(), newRule)) {
                        result.addRule(newRule);
                    }
                }
            }
        }
        return result;
    }

    private Set<Symbol> findNullable(Grammar g) {
        Set<Symbol> nullable = new HashSet<>();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < g.getRules().size(); i++) {
                Rule r = g.getRules().get(i);
                if (nullable.contains(r.getLhs())) continue;

                boolean allNullable = true;
                if (r.getRhs().isEmpty()) {
                    allNullable = true;
                } else {
                    for (int j = 0; j < r.getRhs().size(); j++) {
                        Symbol s = r.getRhs().get(j);
                        if (s.isTerminal() || !nullable.contains(s)) {
                            allNullable = false;
                            break;
                        }
                    }
                }
                if (allNullable) {
                    nullable.add(r.getLhs());
                    changed = true;
                }
            }
        }
        return nullable;
    }

    private void expandHelper(List<Symbol> rhs, Set<Symbol> nullable, int idx,
                              List<Symbol> current, List<List<Symbol>> result) {
        if (idx == rhs.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        Symbol s = rhs.get(idx);
        current.add(s);
        expandHelper(rhs, nullable, idx + 1, current, result);
        current.remove(current.size() - 1);
        if (s.isNonTerminal() && nullable.contains(s)) {
            expandHelper(rhs, nullable, idx + 1, current, result);
        }
    }

    private Grammar removeUnitRules(Grammar g, String newId) {
        Grammar result = new Grammar(newId, g.getStartSymbol());

        for (Symbol A : g.getNonTerminals()) {
            Set<Symbol> reachable = unitReachable(g, A);
            for (Symbol B : reachable) {
                for (int i = 0; i < g.getRules().size(); i++) {
                    Rule r = g.getRules().get(i);
                    if (!r.getLhs().equals(B)) continue;
                    if (r.getRhs().size() == 1 && r.getRhs().get(0).isNonTerminal()) continue;
                    Rule newRule = new Rule(A, r.getRhs());
                    if (!containsRule(result.getRules(), newRule)) {
                        result.addRule(newRule);
                    }
                }
            }
        }
        return result;
    }

    private Set<Symbol> unitReachable(Grammar g, Symbol start) {
        Set<Symbol> reachable = new HashSet<>();
        reachable.add(start);
        boolean changed = true;
        while (changed) {
            changed = false;
            for (int i = 0; i < g.getRules().size(); i++) {
                Rule r = g.getRules().get(i);
                if (!reachable.contains(r.getLhs())) continue;
                if (r.getRhs().size() == 1 && r.getRhs().get(0).isNonTerminal()) {
                    if (!reachable.contains(r.getRhs().get(0))) {
                        reachable.add(r.getRhs().get(0));
                        changed = true;
                    }
                }
            }
        }
        return reachable;
    }

    private Grammar replaceTerminalsInMixed(Grammar g, String newId) {
        Grammar result = new Grammar(newId, g.getStartSymbol());
        Map<Symbol, Symbol> terminalToNT = new HashMap<>();

        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            List<Symbol> rhs = r.getRhs();
            if (rhs.size() == 1) {
                result.addRule(r);
                continue;
            }
            List<Symbol> newRhs = new ArrayList<>();
            for (int j = 0; j < rhs.size(); j++) {
                Symbol s = rhs.get(j);
                if (s.isTerminal()) {
                    Symbol nt = terminalToNT.get(s);
                    if (nt == null) {
                        nt = newNonTerminal();
                        terminalToNT.put(s, nt);
                        List<Symbol> single = new ArrayList<>();
                        single.add(s);
                        result.addRule(new Rule(nt, single));
                    }
                    newRhs.add(nt);
                } else {
                    newRhs.add(s);
                }
            }
            result.addRule(new Rule(r.getLhs(), newRhs));
        }
        return result;
    }

    private Grammar breakLongRules(Grammar g, String newId) {
        Grammar result = new Grammar(newId, g.getStartSymbol());

        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            if (r.getRhs().size() <= 2) {
                result.addRule(r);
                continue;
            }
            Symbol current = r.getLhs();
            for (int j = 0; j < r.getRhs().size() - 2; j++) {
                Symbol next = newNonTerminal();
                List<Symbol> rhs = new ArrayList<>();
                rhs.add(r.getRhs().get(j));
                rhs.add(next);
                result.addRule(new Rule(current, rhs));
                current = next;
            }
            List<Symbol> last = new ArrayList<>();
            last.add(r.getRhs().get(r.getRhs().size() - 2));
            last.add(r.getRhs().get(r.getRhs().size() - 1));
            result.addRule(new Rule(current, last));
        }
        return result;
    }

    private Symbol newNonTerminal() {
        String name = "X" + counter;
        counter++;
        return new Symbol(name, false);
    }

    private boolean containsRule(List<Rule> rules, Rule r) {
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).equals(r)) return true;
        }
        return false;
    }
}