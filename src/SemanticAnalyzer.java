import java.util.HashSet;
import java.util.Set;

public class SemanticAnalyzer {
    private Set<String> symbolTable = new HashSet<>();

    public void declareVariable(String name) {
        if (symbolTable.contains(name)) {
            throw new RuntimeException("Variable already declared: " + name);
        }
        symbolTable.add(name);
    }

    public void checkVariable(String name) {
        if (!symbolTable.contains(name)) {
            throw new RuntimeException("Undeclared variable: " + name);
        }
    }
}
