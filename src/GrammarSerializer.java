import java.util.ArrayList;
import java.util.List;

/**
 * Превръща граматика обратно в текстов формат — обратната операция
 * на GrammarParser. Форматът е същият, който парсерът разбира,
 * така че запазена граматика може отново да бъде заредена.
 */
public class GrammarSerializer {

    /**
     * Връща граматиката като списък от редове.
     * Първи ред — началният символ. Следващите редове — правилата.
     */
    public List<String> serialize(Grammar g) {
        List<String> lines = new ArrayList<>();
        lines.add(g.getStartSymbol().toString());

        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            String line = r.getLhs().toString() + " -> ";
            for (int j = 0; j < r.getRhs().size(); j++) {
                line = line + r.getRhs().get(j).toString();
            }
            lines.add(line);
        }

        return lines;
    }

    /** Връща цялата граматика като един низ с нови редове. */
    public String serializeToString(Grammar g) {
        List<String> lines = serialize(g);
        String result = "";
        for (int i = 0; i < lines.size(); i++) {
            result = result + lines.get(i) + "\n";
        }
        return result;
    }
}