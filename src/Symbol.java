import java.util.Objects;

/**
 * Един символ от азбуката на граматиката.
 * При нас главните латински букви са нетерминали (променливи),
 * а малките букви и цифрите — терминали.
 * Класът е имутабилен — щом го създадеш, не може да се промени.
 */
public class Symbol {
    private final String name;
    private final boolean terminal;

    /**
     * Създава символ с дадено име и тип.
     * Използва се основно от вътрешните алгоритми, когато трябва да
     * създаваме нетерминали с по-дълги имена (например X1, X2 при
     * преобразуване в нормална форма на Чомски).
     *
     * @param name име на символа
     * @param terminal true ако е терминал
     * @throws IllegalArgumentException при празно или null име
     */
    public Symbol(String name, boolean terminal) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Symbol name cannot be empty");
        }
        this.name = name;
        this.terminal = terminal;
    }

    /**
     * Удобен конструктор от един знак.
     * Сам разпознава дали знакът е терминал или нетерминал
     * според това дали е главна или малка буква.
     *
     * @param c знакът на символа
     * @throws IllegalArgumentException ако не е буква или цифра
     */
    public Symbol(char c) {
        this(String.valueOf(c), !Character.isUpperCase(c));
        if (!Character.isLetterOrDigit(c)) {
            throw new IllegalArgumentException("Invalid symbol: '" + c + "'");
        }
    }

    /** Връща името на символа. */
    public String getName() { return name; }

    /** Връща true ако символът е терминал. */
    public boolean isTerminal() { return terminal; }

    /** Връща true ако символът е нетерминал. */
    public boolean isNonTerminal() { return !terminal; }

    /** Два символа са равни когато имат еднакво име и еднакъв тип. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Symbol)) return false;
        Symbol other = (Symbol) o;
        return terminal == other.terminal && name.equals(other.name);
    }

    /** Хеш по име и тип — необходимо за работа в HashSet и HashMap. */
    @Override
    public int hashCode() {
        return Objects.hash(name, terminal);
    }

    /** Връща просто името на символа. */
    @Override
    public String toString() {
        return name;
    }
}