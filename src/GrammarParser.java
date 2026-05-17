import java.util.ArrayList;
import java.util.List;

/**
 * Чете контекстно-свободна граматика от текст.
 * Форматът, който използваме, е възможно най-прост:
 * първият ред е началният символ, а всеки следващ ред е правило
 * във вида "A -> α" (празна дясна страна значи ε-правило).
 */
public class GrammarParser {

    /**
     * Парсва граматика от списък редове (обикновено четени от файл).
     * Празните редове се пропускат, така че форматът е снизходителен
     * към допълнителни празни редове между правилата.
     *
     * @param id идентификатор за новата граматика
     * @param lines редовете от файла
     * @return готовата граматика
     */
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

    /**
     * Парсва един ред като правило.
     * Намира стрелката "->", лявата страна става нетерминал,
     * а дясната — последователност от символи (интервалите се пропускат).
     *
     * @param line ред във формат "A -> α"
     * @return новото правило
     * @throws IllegalArgumentException ако стрелката липсва
     */
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