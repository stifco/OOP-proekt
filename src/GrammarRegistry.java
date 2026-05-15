import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarRegistry {
    private Map<String, Grammar> grammars;
    private int nextId;

    public GrammarRegistry() {
        this.grammars = new HashMap<>();
        this.nextId = 1;
    }

    public String generateId() {
        String id = "G" + nextId;
        nextId++;
        return id;
    }

    public void add(Grammar g) {
        grammars.put(g.getId(), g);
    }

    public Grammar get(String id) {
        return grammars.get(id);
    }

    public void remove(String id) {
        grammars.remove(id);
    }

    public boolean contains(String id) {
        return grammars.containsKey(id);
    }

    public List<String> listIds() {
        return new ArrayList<>(grammars.keySet());
    }

    public int size() {
        return grammars.size();
    }
}