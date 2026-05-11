import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Rule {
    private final Symbol lhs;
    private final List<Symbol> rhs;

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

    public Symbol getLhs() {
        return lhs;
    }

    public List<Symbol> getRhs() {
        return rhs;
    }

    public boolean isEpsilon() {
        return rhs.isEmpty();
    }

    public int length() {
        return rhs.size();
    }

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