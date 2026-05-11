import java.util.Objects;

public class Symbol {
    private final String name;
    private final boolean terminal;

    public Symbol(String name, boolean terminal) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Symbol name cannot be empty");
        }
        this.name = name;
        this.terminal = terminal;
    }


    public Symbol(char c) {
        this(String.valueOf(c), !Character.isUpperCase(c));
        if (!Character.isLetterOrDigit(c)) {
            throw new IllegalArgumentException("Invalid symbol: '" + c + "'");
        }
    }

    public String getName() { return name; }
    public boolean isTerminal() { return terminal; }
    public boolean isNonTerminal() { return !terminal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol other = (Symbol) o;
        return terminal == other.terminal && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, terminal);
    }

    @Override
    public String toString() {
        return name;
    }
}