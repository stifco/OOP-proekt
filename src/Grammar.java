import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Grammar {
    private String id;
    private Symbol startSymbol;
    private List<Rule> rules;
    private Set<Symbol> nonTerminals;
    private Set<Symbol> terminals;

    public Grammar(String id, Symbol startSymbol) {
        this.id = id;
        this.startSymbol = startSymbol;
        this.rules = new ArrayList<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.nonTerminals.add(startSymbol);
    }

    public String getId() {
        return id;
    }

    public Symbol getStartSymbol() {
        return startSymbol;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public Set<Symbol> getNonTerminals() {
        return nonTerminals;
    }

    public Set<Symbol> getTerminals() {
        return terminals;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
        nonTerminals.add(rule.getLhs());
        for (int i = 0; i < rule.getRhs().size(); i++) {
            Symbol s = rule.getRhs().get(i);
            if (s.isTerminal()) {
                terminals.add(s);
            } else {
                nonTerminals.add(s);
            }
        }
    }

    public void removeRule(int n) {
        rules.remove(n - 1);
    }

    public Rule getRule(int n) {
        return rules.get(n - 1);
    }

    public int ruleCount() {
        return rules.size();
    }

    @Override
    public String toString() {
        String result = "Grammar " + id + "\n";
        result = result + "Start: " + startSymbol + "\n";
        for (int i = 0; i < rules.size(); i++) {
            result = result + (i + 1) + ". " + rules.get(i) + "\n";
        }
        return result;
    }
}