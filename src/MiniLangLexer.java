import java.util.*;
        import java.util.regex.*;
        import java.io.*;
public class MiniLangLexer {
    enum TokenType {
        KEYWORD, ID, NUMBER, ASSIGN, SEMI, OP, COMPARE,
        LPAREN, RPAREN, LBRACE, RBRACE
    }
    static class Token {
        TokenType type;
        String value;
        Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
        public String toString() {
            return "(" + type + ", " + value + ")";
        }
    }
    private static final Map<TokenType, String> TOKEN_REGEX = new LinkedHashMap<>();
    private static final Set<String> KEYWORDS = Set.of("int", "if", "else", "while", "print");
    static {
        TOKEN_REGEX.put(TokenType.KEYWORD, "\\b(int|if|else|while|print)\\b");
        TOKEN_REGEX.put(TokenType.ID, "\\b[a-zA-Z_][a-zA-Z0-9_]*\\b");
        TOKEN_REGEX.put(TokenType.NUMBER, "\\b\\d+\\b");
        TOKEN_REGEX.put(TokenType.ASSIGN, "=");
        TOKEN_REGEX.put(TokenType.SEMI, ";");
        TOKEN_REGEX.put(TokenType.OP, "[+\\-*/]");
        TOKEN_REGEX.put(TokenType.COMPARE, "[<>]");
        TOKEN_REGEX.put(TokenType.LPAREN, "\\(");
        TOKEN_REGEX.put(TokenType.RPAREN, "\\)");
        TOKEN_REGEX.put(TokenType.LBRACE, "\\{");
        TOKEN_REGEX.put(TokenType.RBRACE, "\\}");
    }
    public static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        String regex = TOKEN_REGEX.values().stream().reduce((a, b) -> a + "|" + b).get();
        Pattern masterPattern = Pattern.compile(regex);
        Matcher matcher = masterPattern.matcher(input);
        while (matcher.find()) {
            String value = matcher.group();
            TokenType type = null;
            for (Map.Entry<TokenType, String> entry : TOKEN_REGEX.entrySet()) {
                if (value.matches(entry.getValue())) {
                    type = entry.getKey();
                    break;
                }
            }
            if (type == TokenType.ID && KEYWORDS.contains(value)) {
                type = TokenType.KEYWORD;
            }
            if (type != null) {
                tokens.add(new Token(type, value));
            }
        }
        return tokens;
    }
    public static void main(String[] args) throws IOException {
        StringBuilder code = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("input.minilang"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                code.append(line).append("\n");
            }
        }
        List<Token> tokens = tokenize(code.toString());
        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
