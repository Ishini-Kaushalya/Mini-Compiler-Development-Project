import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            String code = Files.readString(Path.of("input.minilang"));

            // Lexical Analysis
            List<MiniLangLexer.Token> tokens = MiniLangLexer.tokenize(code);
            System.out.println("🟡 Tokens:");
            for (MiniLangLexer.Token token : tokens) {
                String label = switch (token.type) {
                    case ASSIGN -> "ASSIGN_OP";
                    case ID -> "IDENTIFIER";
                    case NUMBER -> "NUMBER";
                    case OP -> "OPERATOR";
                    case COMPARE -> "COMPARATOR";
                    case SEMI -> "SEMICOLON";
                    case KEYWORD -> "KEYWORD";
                    case LPAREN -> "LEFT_PAREN";
                    case RPAREN -> "RIGHT_PAREN";
                    case LBRACE -> "LEFT_BRACE";
                    case RBRACE -> "RIGHT_BRACE";
                };
                System.out.printf("(%s, %s)%n", label, token.value);
            }

            // Syntax Analysis
            System.out.println("\n🔵 Performing Syntax Analysis...");
            MiniLangParser parser = new MiniLangParser(tokens);
            parser.parseProgram();
            System.out.println("✅ Syntax Analysis: Passed.");

            // Semantic Analysis (post-parse token scan)
            System.out.println("🔵 Performing Semantic Analysis...");
            performSemanticCheck(tokens);
            System.out.println("✅ Semantic Analysis: Passed.");

        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ File Error: input.minilang not found.");
        }
    }

    public static void performSemanticCheck(List<MiniLangLexer.Token> tokens) {
        Set<String> declared = new HashSet<>();
        boolean nextIsDeclaration = false;
        boolean insidePrint = false;

        for (int i = 0; i < tokens.size(); i++) {
            MiniLangLexer.Token token = tokens.get(i);

            // Detect int declaration
            if (token.type == MiniLangLexer.TokenType.KEYWORD && token.value.equals("int")) {
                nextIsDeclaration = true;
                continue;
            }

            // Declaration
            if (nextIsDeclaration && token.type == MiniLangLexer.TokenType.ID) {
                if (declared.contains(token.value)) {
                    throw new RuntimeException("Semantic Error: Duplicate declaration of variable '" + token.value + "'");
                }
                declared.add(token.value);
                System.out.println("Declared: " + token.value);
                nextIsDeclaration = false;
            }

            // Check for usage: assignment or print
            if (token.type == MiniLangLexer.TokenType.KEYWORD && token.value.equals("print")) {
                insidePrint = true;
                continue;
            }

            if (token.type == MiniLangLexer.TokenType.ID) {
                if (!declared.contains(token.value)) {
                    throw new RuntimeException("Semantic Error: Variable '" + token.value + "' used before declaration");
                }
                System.out.println("Used: " + token.value);
            }

            if (insidePrint && token.type == MiniLangLexer.TokenType.RPAREN) {
                insidePrint = false;
            }
        }
    }
}
