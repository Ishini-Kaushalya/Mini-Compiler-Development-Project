import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Read the MiniLang input code
            String code = Files.readString(Path.of("input.minilang"));

            // Tokenize using your lexer
            List<MiniLangLexer.Token> tokens = MiniLangLexer.tokenize(code);
            System.out.println("Tokens:");
            for (MiniLangLexer.Token token : tokens) {
                System.out.println(token);
            }

            // Parse using your parser
            MiniLangParser parser = new MiniLangParser(tokens);
            parser.parseProgram();
            System.out.println("\n✅ Parsing successful! The program is syntactically correct.");

        } catch (RuntimeException e) {
            System.err.println("\n❌ Syntax Error: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("\n❌ File Error: input.minilang not found.");
        }
    }
}
