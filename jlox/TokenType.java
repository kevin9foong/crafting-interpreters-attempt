package jlox;

import java.util.HashMap;
import java.util.Map;

public enum TokenType {
    // single character tokens
    LEFT_BRACE, RIGHT_BRACE, // for blocks
    LEFT_PAREN, RIGHT_PAREN, // for operator precedence
    DOT, MINUS, PLUS, SLASH, STAR, // for arithmetic
    COMMA, SEMICOLON, // for separating expressions/statements

    // one or two character tokens
    BANG, BANG_EQUAL, // for negation BANG is !
    EQUAL, EQUAL_EQUAL, // for equality
    GREATER, GREATER_EQUAL, // for comparison
    LESS, LESS_EQUAL, // for comparison

    // literals
    IDENTIFIER, STRING, NUMBER,

    // keywords
    AND, OR,
    IF, ELSE,
    FOR, WHILE,
    NIL,
    TRUE, FALSE,
    CLASS, THIS, SUPER,
    VAR, FUN,
    RETURN,
    PRINT,

    EOF // end of file
}
