import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CYKAlgorithm {

    public boolean accepts(Grammar g, String word) {
        if (word.isEmpty()) {
            for (int i = 0; i < g.getRules().size(); i++) {
                Rule r = g.getRules().get(i);
                if (r.getLhs().equals(g.getStartSymbol()) && r.getRhs().isEmpty()) {
                    return true;
                }
            }
            return false;
        }

        int n = word.length();
        List<List<Set<Symbol>>> table = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Set<Symbol>> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(new HashSet<>());
            }
            table.add(row);
        }

        // Length-1 substrings: A -> a
        for (int i = 0; i < n; i++) {
            String c = String.valueOf(word.charAt(i));
            for (int k = 0; k < g.getRules().size(); k++) {
                Rule r = g.getRules().get(k);
                List<Symbol> rhs = r.getRhs();
                if (rhs.size() == 1 && rhs.get(0).isTerminal()
                        && rhs.get(0).getName().equals(c)) {
                    table.get(i).get(i).add(r.getLhs());
                }
            }
        }

        // Longer substrings: A -> BC
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                for (int k = i; k < j; k++) {
                    for (int m = 0; m < g.getRules().size(); m++) {
                        Rule r = g.getRules().get(m);
                        List<Symbol> rhs = r.getRhs();
                        if (rhs.size() == 2) {
                            Symbol B = rhs.get(0);
                            Symbol C = rhs.get(1);
                            if (table.get(i).get(k).contains(B)
                                    && table.get(k + 1).get(j).contains(C)) {
                                table.get(i).get(j).add(r.getLhs());
                            }
                        }
                    }
                }
            }
        }

        return table.get(0).get(n - 1).contains(g.getStartSymbol());
    }
}