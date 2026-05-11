import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class CommandSystem {
    public  void  run() {
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
                case "open":       cmdOpen(tokens);  break;
                case "close":       break;
                case "save":        break;
                case "help":        break;
                case "exit":        return true;
                case "list":        break;
                case "print":       break;
                case "addrule":     break;
                case "removerule":  break;
                case "union":       break;
                case "concat":      break;
                case "chomsky":     break;
                case "cyk":         break;
                case "iter":        break;
                case "empty":       break;
                case "chomskify":   break;
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
        var ca = line.toCharArray();
        for(int i = 0 ; i<ca.length ; i++){
            char c = ca[i];
            if (c == '"') {
                inQuote = !inQuote;
            } else if (c == ' ' && !inQuote) {
                if (str.length() > 0) {
                    tokens.add(str);

                    str = "";
                }
            } else {
                str +=c;
            }
        }
        if (str.length() > 0) tokens.add(str);
        return tokens;
    }
    private boolean cmdOpen(List<String> tokens) {
        if (tokens.size() < 2) return false;
        String path = joinPath(tokens, 1);
        File file = new File(path);
        for(int i = 0 ; i<tokens.size(); i++){
            System.out.println(tokens.get(i));
        }
        System.out.println("Length bytes: " + file.length());

        if (!file.exists()) {
            System.out.println("Error: file doesn't exist - " + file.getAbsolutePath());
            return false;
        }

        try {
            List<String> lines = Files.readAllLines(file.toPath());
            if (lines.isEmpty()) {
                System.out.println("File is empty.");
            } else {
                for (String line : lines) {
                    System.out.println(line);
                }
            }
            System.out.println("Successfully opened: " + file.getName());
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return false;
    }

    private String joinPath(List<String> tokens, int from) {
        return String.join(" ", tokens.subList(from, tokens.size()));
    }


}


