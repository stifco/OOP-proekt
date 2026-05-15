import java.util.ArrayList;
import java.util.List;

public class GrammarSerializer {

    public List<String> serialize(Grammar g) {
        List<String> lines = new ArrayList<>();
        lines.add(g.getStartSymbol().toString());

        for (int i = 0; i < g.getRules().size(); i++) {
            Rule r = g.getRules().get(i);
            String line = r.getLsd().toString() + " -> ";
            for (int j = 0; j < r.getRsd().size(); j++) {
                line = line + r.getRsd().get(j).toString();
            }
            lines.add(line);
        }

        return lines;
    }

    public String serializeToString(Grammar g) {
        List<String> lines = serialize(g);
        String result = "";
        for (int i = 0; i < lines.size(); i++) {
            result = result + lines.get(i) + "\n";
        }
        return result;
    }
}