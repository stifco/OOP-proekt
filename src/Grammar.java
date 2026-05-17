import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Контекстно-свободна граматика G = (V, Σ, R, S).
 * Пази уникален идентификатор, начален символ, списък правила и
 * двете множества от символи — нетерминали (V) и терминали (Σ).
 * При добавяне на ново правило, неговите символи автоматично
 * се класифицират и добавят към съответните множества.
 */
public class Grammar {
    private String id;
    private Symbol startSymbol;
    private List<Rule> rules;
    private Set<Symbol> nonTerminals;
    private Set<Symbol> terminals;

    /**
     * Създава празна граматика с даден идентификатор и начален символ.
     * Самият начален символ автоматично се добавя в множеството на
     * нетерминалите.
     *
     * @param id уникален идентификатор (раздава се от GrammarRegistry)
     * @param startSymbol началният нетерминал
     */
    public Grammar(String id, Symbol startSymbol) {
        this.id = id;
        this.startSymbol = startSymbol;
        this.rules = new ArrayList<>();
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.nonTerminals.add(startSymbol);
    }

    /** Връща идентификатора (например "G1"). */
    public String getId() { return id; }

    /** Връща началния символ. */
    public Symbol getStartSymbol() { return startSymbol; }

    /** Връща списък с всички правила (в реда им на добавяне). */
    public List<Rule> getRules() { return rules; }

    /** Връща множеството нетерминали. */
    public Set<Symbol> getNonTerminals() { return nonTerminals; }

    /** Връща множеството терминали. */
    public Set<Symbol> getTerminals() { return terminals; }

    /**
     * Добавя ново правило към граматиката.
     * Едновременно с това разпознава всички символи в правилото
     * и ги слага в правилното множество — нетерминали или терминали.
     *
     * @param rule правилото за добавяне
     */
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

    /**
     * Премахва правило по неговия пореден номер.
     * Номерирането е от 1 (както го извежда командата print).
     *
     * @param n пореден номер на правилото за премахване
     */
    public void removeRule(int n) {
        rules.remove(n - 1);
    }

    /** Връща правилото с пореден номер n (от 1). */
    public Rule getRule(int n) {
        return rules.get(n - 1);
    }

    /** Колко правила има в граматиката. */
    public int ruleCount() {
        return rules.size();
    }

    /**
     * Извежда граматиката в четим вид — заглавие, начален символ
     * и номериран списък с правила. Точно това, което показва print.
     */
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