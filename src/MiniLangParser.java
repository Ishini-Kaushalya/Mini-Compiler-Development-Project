import java.util.List;

public class MiniLangParser {
    private List<MiniLangLexer.Token> tokens;
    private int index = 0;

    public MiniLangParser(List<MiniLangLexer.Token> tokens) {
        this.tokens = tokens;
    }

    private MiniLangLexer.Token lookahead() {
        return index < tokens.size() ? tokens.get(index) : null;
    }

    private MiniLangLexer.Token match(MiniLangLexer.TokenType expected) {
        MiniLangLexer.Token token = lookahead();
        if (token != null && token.type == expected) {
            index++;
            return token;
        } else {
            throw new RuntimeException("Syntax error at token: " + token);
        }
    }

    public void parseProgram() {
        parseStmtList();
    }

    private void parseStmtList() {
        while (lookahead() != null && lookahead().type != MiniLangLexer.TokenType.RBRACE) {
            parseStmt();
        }
    }

    private void parseStmt() {
        switch (lookahead().type) {
            case KEYWORD:
                String kw = lookahead().value;
                switch (kw) {
                    case "int": parseDecl(); break;
                    case "if": parseIf(); break;
                    case "while": parseWhile(); break;
                    case "print": parsePrint(); break;
                    default: throw new RuntimeException("Unknown keyword: " + kw);
                }
                break;
            case ID: parseAssign(); break;
            default: throw new RuntimeException("Invalid statement");
        }
    }

    private void parseDecl() {
        match(MiniLangLexer.TokenType.KEYWORD); // int
        match(MiniLangLexer.TokenType.ID);
        match(MiniLangLexer.TokenType.SEMI);
    }

    private void parseAssign() {
        match(MiniLangLexer.TokenType.ID);
        match(MiniLangLexer.TokenType.ASSIGN);
        parseExpr();
        match(MiniLangLexer.TokenType.SEMI);
    }

    private void parseIf() {
        match(MiniLangLexer.TokenType.KEYWORD); // if
        match(MiniLangLexer.TokenType.LPAREN);
        parseCond();
        match(MiniLangLexer.TokenType.RPAREN);
        match(MiniLangLexer.TokenType.LBRACE);
        parseStmtList();
        match(MiniLangLexer.TokenType.RBRACE);
        if (lookahead() != null && lookahead().value.equals("else")) {
            match(MiniLangLexer.TokenType.KEYWORD);
            match(MiniLangLexer.TokenType.LBRACE);
            parseStmtList();
            match(MiniLangLexer.TokenType.RBRACE);
        }
    }

    private void parseWhile() {
        match(MiniLangLexer.TokenType.KEYWORD); // while
        match(MiniLangLexer.TokenType.LPAREN);
        parseCond();
        match(MiniLangLexer.TokenType.RPAREN);
        match(MiniLangLexer.TokenType.LBRACE);
        parseStmtList();
        match(MiniLangLexer.TokenType.RBRACE);
    }

    private void parsePrint() {
        match(MiniLangLexer.TokenType.KEYWORD); // print
        match(MiniLangLexer.TokenType.LPAREN);
        match(MiniLangLexer.TokenType.ID);
        match(MiniLangLexer.TokenType.RPAREN);
        match(MiniLangLexer.TokenType.SEMI);
    }

    private void parseCond() {
        parseExpr();
        match(MiniLangLexer.TokenType.COMPARE);
        parseExpr();
    }

    private void parseExpr() {
        parseTerm();
        while (lookahead() != null && (lookahead().value.equals("+") || lookahead().value.equals("-"))) {
            match(lookahead().type); // + or -
            parseTerm();
        }
    }

    private void parseTerm() {
        parseFactor();
        while (lookahead() != null && (lookahead().value.equals("*") || lookahead().value.equals("/"))) {
            match(lookahead().type);
            parseFactor();
        }
    }

    private void parseFactor() {
        if (lookahead().type == MiniLangLexer.TokenType.NUMBER || lookahead().type == MiniLangLexer.TokenType.ID) {
            match(lookahead().type);
        } else if (lookahead().type == MiniLangLexer.TokenType.LPAREN) {
            match(MiniLangLexer.TokenType.LPAREN);
            parseExpr();
            match(MiniLangLexer.TokenType.RPAREN);
        } else {
            throw new RuntimeException("Invalid expression");
        }
    }
}
