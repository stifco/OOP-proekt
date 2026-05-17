import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Регистър за всички заредени и създадени граматики.
 * Държи ги в Map по техния уникален идентификатор и сам генерира
 * нови ID-та във вида G1, G2, G3, ... При операциите union, concat
 * и iter новите граматики също получават идентификатор оттук.
 */
public class GrammarRegistry {
    private Map<String, Grammar> grammars;
    private int nextId;

    /** Създава празен регистър с брояч, започващ от 1. */
    public GrammarRegistry() {
        this.grammars = new HashMap<>();
        this.nextId = 1;
    }

    /**
     * Връща следващия свободен идентификатор и увеличава брояча.
     * Веднъж дадено, ID никога не се връща повторно.
     *
     * @return нов идентификатор, например "G7"
     */
    public String generateId() {
        String id = "G" + nextId;
        nextId++;
        return id;
    }

    /** Добавя граматика в регистъра под нейния идентификатор. */
    public void add(Grammar g) {
        grammars.put(g.getId(), g);
    }

    /**
     * Намира граматика по идентификатор.
     *
     * @param id идентификаторът на търсената граматика
     * @return граматиката, или null ако такава няма
     */
    public Grammar get(String id) {
        return grammars.get(id);
    }

    /** Премахва граматика по идентификатор. */
    public void remove(String id) {
        grammars.remove(id);
    }

    /** True ако в регистъра има граматика с такъв идентификатор. */
    public boolean contains(String id) {
        return grammars.containsKey(id);
    }

    /** Връща списък с всички идентификатори в регистъра. */
    public List<String> listIds() {
        return new ArrayList<>(grammars.keySet());
    }

    /** Колко граматики има в момента. */
    public int size() {
        return grammars.size();
    }
}