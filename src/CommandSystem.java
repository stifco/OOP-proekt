import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandSystem {
    private GrammarRegistry registry;
    private GrammarParser parser;
    private GrammarSerializer serializer;
    private GrammarOperations ops;
    private GrammarAnalysis analysis;
    private String currentFilePath;

    public CommandSystem() {
        this.registry = new GrammarRegistry();
        this.parser = new GrammarParser();
        this.serializer = new GrammarSerializer();
        this.ops = new GrammarOperations();
        this.analysis = new GrammarAnalysis();
        this.currentFilePath = null;
    }

    public void run() {
        System.out.println("CFG Manager — type 'help' for available commands.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) continue;
            boolean shouldExit = handleCommand(line);
            if (shouldExit) break;
        }
        scanner.close();
    }

    public boolean handleCommand(String line) {
        List<String> tokens = tokenize(line);
        if (tokens.isEmpty()) return false;

        String cmd = tokens.get(0).toLowerCase();
        try {
            switch (cmd) {
                case "open":       cmdOpen(tokens); break;
                case "close":      cmdClose(); break;
                case "save":
                    if (tokens.size() >= 2 && tokens.get(1).equalsIgnoreCase("as")) {
                        cmdSaveAs(tokens);
                    } else {
                        cmdSave();
                    }
                    break;
                case "help":       cmdHelp(); break;
                case "exit":       return true;
                case "list":       cmdList(); break;
                case "print":      cmdPrint(tokens); break;
                case "addrule":    cmdAddRule(tokens); break;
                case "removerule": cmdRemoveRule(tokens); break;
                case "union":      cmdUnion(tokens); break;
                case "concat":     cmdConcat(tokens); break;
                case "iter":       cmdIter(tokens); break;
                case "empty":      cmdEmpty(tokens); break;
                case "chomsky":    cmdChomsky(tokens); break;
                case "cyk":        break;
                case "chomskify":  break;
                default:
                    System.out.println("Unknown command: '" + cmd +
                            "'. Type 'help' for available commands.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    private List<String> tokenize(String line) {
        List<String> tokens = new ArrayList<>();
        String str = "";
        boolean inQuote = false;
        char[] ca = line.toCharArray();
        for (int i = 0; i < ca.length; i++) {
            char c = ca[i];
            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ' ' && !inQuote) {
                if (str.length() > 0) {
                    tokens.add(str);
                    str = "";
                }
            } else {
                str += c;
            }
        }
        if (str.length() > 0) tokens.add(str);
        return tokens;
    }

    private void cmdOpen(List<String> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Usage: open <file>");
            return;
        }
        String path = joinPath(tokens, 1);
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("Error: file doesn't exist - " + file.getAbsolutePath());
            return;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            String id = registry.generateId();
            Grammar g = parser.parse(id, lines);
            registry.add(g);
            this.currentFilePath = path;
            System.out.println("Successfully opened: " + file.getName());
            System.out.println("Grammar id: " + id);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    private void cmdList() {
        List<String> ids = registry.listIds();
        if (ids.isEmpty()) {
            System.out.println("No grammars loaded.");
            return;
        }
        for (int i = 0; i < ids.size(); i++) {
            System.out.println(ids.get(i));
        }
    }

    private void cmdPrint(List<String> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Usage: print <id>");
            return;
        }
        String id = tokens.get(1);
        Grammar g = registry.get(id);
        if (g == null) {
            System.out.println("No grammar with id: " + id);
            return;
        }
        System.out.print(g.toString());
    }

    private void cmdClose() {
        List<String> ids = registry.listIds();
        for (int i = 0; i < ids.size(); i++) {
            registry.remove(ids.get(i));
        }
        this.currentFilePath = null;
        System.out.println("Closed all grammars.");
    }

    private void cmdSave() {
        if (currentFilePath == null) {
            System.out.println("No file is currently open.");
            return;
        }
        saveAllToFile(currentFilePath);
    }

    private void cmdSaveAs(List<String> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Usage: save as <path>");
            return;
        }
        String path = joinPath(tokens, 2);
        saveAllToFile(path);
    }

    private void saveAllToFile(String path) {
        try {
            String content = "";
            List<String> ids = registry.listIds();
            for (int i = 0; i < ids.size(); i++) {
                Grammar g = registry.get(ids.get(i));
                content = content + serializer.serializeToString(g);
                content = content + "\n";
            }
            Files.writeString(new File(path).toPath(), content);
            System.out.println("Successfully saved: " + new File(path).getName());
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private void cmdAddRule(List<String> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Usage: addRule <id> <rule>");
            return;
        }
        String id = tokens.get(1);
        Grammar g = registry.get(id);
        if (g == null) {
            System.out.println("No grammar with id: " + id);
            return;
        }
        String ruleStr = joinPath(tokens, 2);
        Rule r = parser.parseRule(ruleStr);
        g.addRule(r);
        System.out.println("Rule added to " + id);
    }

    private void cmdRemoveRule(List<String> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Usage: removeRule <id> <n>");
            return;
        }
        String id = tokens.get(1);
        Grammar g = registry.get(id);
        if (g == null) {
            System.out.println("No grammar with id: " + id);
            return;
        }
        int n = Integer.parseInt(tokens.get(2));
        g.removeRule(n);
        System.out.println("Rule " + n + " removed from " + id);
    }

    private void cmdUnion(List<String> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Usage: union <id1> <id2>");
            return;
        }
        Grammar g1 = registry.get(tokens.get(1));
        Grammar g2 = registry.get(tokens.get(2));
        if (g1 == null || g2 == null) {
            System.out.println("Grammar not found");
            return;
        }
        String newId = registry.generateId();
        Grammar result = ops.union(g1, g2, newId);
        registry.add(result);
        System.out.println("New grammar id: " + newId);
    }

    private void cmdConcat(List<String> tokens) {
        if (tokens.size() < 3) {
            System.out.println("Usage: concat <id1> <id2>");
            return;
        }
        Grammar g1 = registry.get(tokens.get(1));
        Grammar g2 = registry.get(tokens.get(2));
        if (g1 == null || g2 == null) {
            System.out.println("Grammar not found");
            return;
        }
        String newId = registry.generateId();
        Grammar result = ops.concat(g1, g2, newId);
        registry.add(result);
        System.out.println("New grammar id: " + newId);
    }

    private void cmdIter(List<String> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Usage: iter <id>");
            return;
        }
        Grammar g = registry.get(tokens.get(1));
        if (g == null) {
            System.out.println("Grammar not found");
            return;
        }
        String newId = registry.generateId();
        Grammar result = ops.iter(g, newId);
        registry.add(result);
        System.out.println("New grammar id: " + newId);
    }

    private void cmdEmpty(List<String> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Usage: empty <id>");
            return;
        }
        Grammar g = registry.get(tokens.get(1));
        if (g == null) {
            System.out.println("Grammar not found");
            return;
        }
        boolean empty = analysis.isEmpty(g);
        if (empty) {
            System.out.println("Language is empty");
        } else {
            System.out.println("Language is not empty");
        }
    }

    private void cmdChomsky(List<String> tokens) {
        if (tokens.size() < 2) {
            System.out.println("Usage: chomsky <id>");
            return;
        }
        Grammar g = registry.get(tokens.get(1));
        if (g == null) {
            System.out.println("Grammar not found");
            return;
        }
        boolean cnf = analysis.isChomsky(g);
        if (cnf) {
            System.out.println("Grammar is in Chomsky normal form");
        } else {
            System.out.println("Grammar is NOT in Chomsky normal form");
        }
    }

    private void cmdHelp() {
        System.out.println("Available commands:");
        System.out.println("open <file>            - load a grammar from file");
        System.out.println("close                  - close all loaded grammars");
        System.out.println("save                   - save to currently open file");
        System.out.println("save as <path>         - save to a new file");
        System.out.println("list                   - list all grammar ids");
        System.out.println("print <id>             - print a grammar");
        System.out.println("addRule <id> <rule>    - add a rule to a grammar");
        System.out.println("removeRule <id> <n>    - remove rule by number");
        System.out.println("union <id1> <id2>      - union of two grammars");
        System.out.println("concat <id1> <id2>     - concatenation");
        System.out.println("iter <id>              - Kleene star iteration");
        System.out.println("empty <id>             - check if language is empty");
        System.out.println("chomsky <id>           - check if grammar is in CNF");
        System.out.println("cyk <id>               - run CYK algorithm");
        System.out.println("chomskify <id>         - convert to CNF");
        System.out.println("help                   - show this help");
        System.out.println("exit                   - exit the program");
    }

    private String joinPath(List<String> tokens, int from) {
        return String.join(" ", tokens.subList(from, tokens.size()));
    }
}