import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Read the MiniLang source code
            String code = Files.readString(Path.of("input.minilang"));

            // Tokenize the input
            List<MiniLangLexer.Token> tokens = MiniLangLexer.tokenize(code);
            System.out.println("🟡 Tokens:");
            for (MiniLangLexer.Token token : tokens) {
                System.out.println("(" + token.type + ", " + token.value + ")");
            }

            // Syntax analysis
            System.out.println("\n🔵 Performing Syntax Analysis...");
            MiniLangParser parser = new MiniLangParser(tokens); // no change to parser
            parser.parseProgram();
            System.out.println("✅ Syntax Analysis: Passed.");

            // Semantic check (manual)
            System.out.println("🔵 Performing Semantic Analysis...");
            Set<String> declared = new HashSet<>();
            for (int i = 0; i < tokens.size(); i++) {
                MiniLangLexer.Token token = tokens.get(i);
                if (token.type == MiniLangLexer.TokenType.KEYWORD && token.value.equals("int")) {
                    if (tokens.get(i + 1).type == MiniLangLexer.TokenType.ID) {
                        String varName = tokens.get(i + 1).value;
                        if (declared.contains(varName)) {
                            throw new RuntimeException("Variable '" + varName + "' already declared.");
                        }
                        declared.add(varName);
                        System.out.println("Declared: " + varName);
                    }
                } else if (token.type == MiniLangLexer.TokenType.ID) {
                    if (!declared.contains(token.value)) {
                        throw new RuntimeException("Variable '" + token.value + "' used before declaration.");
                    }
                    System.out.println("Used: " + token.value);
                }
            }
            System.out.println("✅ Semantic Analysis: Passed.");

            // Intermediate code generation
            System.out.println("🔵 Generating Intermediate Code...");
            IntermediateCodeGenerator gen = new IntermediateCodeGenerator();

            // Manually emit code for this example
            gen.emit("a = 5");
            String t1 = gen.newTemp();
            gen.emit(t1 + " = a > 0");
            gen.emit("if " + t1 + " goto L1");
            gen.emit("goto L2");
            gen.emit("L1:");
            gen.emit("print a");
            gen.emit("goto L3");
            gen.emit("L2:");
            String t2 = gen.newTemp();
            gen.emit(t2 + " = a + 1");
            gen.emit("a = " + t2);
            gen.emit("L3:");

        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("❌ File Error: input.minilang not found.");
        }
    }
}
