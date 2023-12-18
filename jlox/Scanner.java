package jlox;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import static jlox.TokenType.*;

public class Scanner {
    private final String sourceCode;
    private final List<Token> tokens;

    private int start = 0; // points to index of first char in lexeme being scanned
    private int current = 0; // points to index of char currently being considered
    private int line = 1; // tracks line number char for index current is on
    private boolean isMultiLineComment = false;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and",    AND);
        keywords.put("class",  CLASS);
        keywords.put("else",   ELSE);
        keywords.put("false",  FALSE);
        keywords.put("for",    FOR);
        keywords.put("fun",    FUN);
        keywords.put("if",     IF);
        keywords.put("nil",    NIL);
        keywords.put("or",     OR);
        keywords.put("print",  PRINT);
        keywords.put("return", RETURN);
        keywords.put("super",  SUPER);
        keywords.put("this",   THIS);
        keywords.put("true",   TRUE);
        keywords.put("var",    VAR);
        keywords.put("while",  WHILE);
    }

    public Scanner(String sourceCode) {
        this.sourceCode = sourceCode;
        this.tokens = new ArrayList<>();
    }

    public List<Token> scanTokens() {

        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            // We are going to scan it.
            start = current;
            scanToken();
        }

        // EOF token means no more tokens to scan, making parser cleaner.
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    /**
     * Checks if we have consumed all the source code characters.
     */
    private boolean isAtEnd() {
        return current >= sourceCode.length();
    }

    private void scanToken() {
        char c = advanceChar(); // consume the next character
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            // deal with potentially 2 char operators
            case '/':
                if (match('/')) {
                    // consume the rest of the line since it is single line comment
                    while (peek() != '\n' && !isAtEnd()) advanceChar();
                } else if (match('*')) {
                    // multiline comment
                    while (peek() != '*' && peekNext() != '/' && !isAtEnd()) {
                        if (peek() == '\n') line++; // increment line number for multiline comments
                        advanceChar();
                    }

                    if (peek() == '*' && peekNext() == '/') {
                        // consume the */
                        advanceChar();
                        advanceChar();
                    } else {
                        // multiline comment was not closed
                        Lox.error(line, "Unterminated multiline comment.");
                    }
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            // literals
            case '"':
                string();
                break;
            // skip over these characters
            case ' ': // space
            case '\r': // carriage return
            case '\t': // tab
                break;
            case '\n': // newline
                line++; // increment line number
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) { // identifiers cannot start with non alpha character
                    identifier();
                    break;
                } else {
                    // erroneous character should still be consumed to avoid infinite loop
                    // keep scanning to find all errors in source code in a single run
                    Lox.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
               (c >= 'A' && c <= 'Z') ||
                c == '_';
      }

    private boolean isAlphaNumeric(char c) {
    return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advanceChar();
        // ends on non alphanumeric character, hence next lexeme

        // check if the identifier is a reserved word
        String text = sourceCode.substring(start, current);
        TokenType type = keywords.get(text);

        // if not a reserved word, it is an identifier
        // for identifiers, we have the lexeme to get the name
        if (type == null) type = TokenType.IDENTIFIER;

        addToken(type);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        // consume all digits
        while (isDigit(peek())) advanceChar();
        // look for fractional part
        if (peek() == '.' && isDigit(peekNext())) {
            // consume the .
            advanceChar();
            // consume the digits after .
            while (isDigit(peek())) advanceChar();
        }
        // ends at the character after the last digit, hence next lexeme

        // convert the string representation of number into double
        addToken(TokenType.NUMBER, Double.parseDouble(sourceCode.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++; // increment line number for multiline strings
            advanceChar();
        }
        if (isAtEnd()) {
            // if reach end without finding the closing quote
            Lox.error(line, "Unterminated string.");
            return;
        }
        // consume the closing "
        advanceChar();
        // do required processing to convert string source code into literal value.
        // trim the surrounding quotes
        String value = sourceCode.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char advanceChar() {
        char c = sourceCode.charAt(current);
        current++; // increment current to point to next char for next scan
        return c;
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (sourceCode.charAt(current) != expected) return false;
        current++; // 2 char operator found, consume the next char
        // and hence increment for next scan
        return true;
    }

    /*
     * Returns the character at the current position without consuming it.
     * aka Lookahead
     * If there is no character at current due to end of source code, return null character.
     */
    private char peek() {
        if (isAtEnd()) return '\0'; // null character
        return sourceCode.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= sourceCode.length()) return '\0';
        return sourceCode.charAt(current + 1);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String lexeme = sourceCode.substring(start, current);
        tokens.add(new Token(type, lexeme, literal, line));
    }
}
