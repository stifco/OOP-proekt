import java.util.ArrayList;
import java.util.List;

public class GrammarParser {

    public Grammar parse(String id, List<String> lines) {
        Symbol startSymbol = null;
        List<Rule> tempRules = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            if (startSymbol == null) {
                startSymbol = new Symbol(line.charAt(0));
            } else {
                Rule r = parseRule(line);
                tempRules.add(r);
            }
        }

        Grammar g = new Grammar(id, startSymbol);
        for (int i = 0; i < tempRules.size(); i++) {
            g.addRule(tempRules.get(i));
        }
        return g;
    }

    public Rule parseRule(String line) {
        int arrowIdx = line.indexOf("->");
        if (arrowIdx == -1) {
            throw new IllegalArgumentException("Invalid rule: " + line);
        }

        String lhsStr = line.substring(0, arrowIdx).trim();
        String rhsStr = line.substring(arrowIdx + 2).trim();

        Symbol lsd = new Symbol(lhsStr.charAt(0));
        List<Symbol> rsd = new ArrayList<>();
        for (int i = 0; i < rhsStr.length(); i++) {
            char c = rhsStr.charAt(i);
            if (c == ' ') continue;
            rsd.add(new Symbol(c));
        }
        return new Rule(lsd, rsd);
    }
}