package jlox;

public class Token {
    final TokenType type;
    // whats lexeme vs literal?
    final String lexeme;
    final Object literal;
    final int line; // store location info for error reporting

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
