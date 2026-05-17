import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Едно продукционно правило A → α от граматиката.
 * Лявата страна винаги е един нетерминал, а дясната — низ от символи
 * (може и да е празен — тогава правилото е ε-правило).
 * Имутабилен клас.
 */
public class Rule {
    private final Symbol lhs;
    private final List<Symbol> rhs;

    /**
     * Създава ново правило A → α.
     * Проверката, че лявата страна е нетерминал, гарантира, че
     * правилото е в контекстно-свободна форма.
     *
     * @param lhs лявата страна — задължително нетерминал
     * @param rhs дясната страна — празен списък значи ε-правило
     * @throws IllegalArgumentException ако lhs не е нетерминал
     */
    public Rule(Symbol lhs, List<Symbol> rhs) {
        if (lhs == null) {
            throw new IllegalArgumentException("LHS cannot be null");
        }
        if (!lhs.isNonTerminal()) {
            throw new IllegalArgumentException("LHS must be a non-terminal, got: " + lhs);
        }
        if (rhs == null) {
            throw new IllegalArgumentException("RHS cannot be null (use empty list for epsilon)");
        }
        this.lhs = lhs;
        this.rhs = Collections.unmodifiableList(new ArrayList<>(rhs));
    }

    /** Връща лявата страна на правилото. */
    public Symbol getLhs() { return lhs; }

    /** Връща дясната страна. Списъкът е немодифицируем. */
    public List<Symbol> getRhs() { return rhs; }

    /** True ако дясната страна е празна (т.е. правилото е A → ε). */
    public boolean isEpsilon() { return rhs.isEmpty(); }

    /** Колко символа има в дясната страна. */
    public int length() { return rhs.size(); }

    /** Две правила са равни когато имат еднакви леви и десни страни. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule other = (Rule) o;
        return lhs.equals(other.lhs) && rhs.equals(other.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lhs, rhs);
    }

    /** Извежда правилото в четим вид, например "S -> aSb" или "S -> ε". */
    @Override
    public String toString() {
        if (rhs.isEmpty()) {
            return lhs + " -> ε";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(lhs).append(" -> ");
        for (Symbol s : rhs) {
            sb.append(s);
        }
        return sb.toString();
    }
}