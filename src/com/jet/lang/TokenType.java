package com.jet.lang;

/**
 * Represents the different types of tokens in the Jet language.
 * Used by the lexer and parser to categorize pieces of source code.
 */
enum TokenType {
    // Single character tokens
    LEFT_PAREM,   // '('
    RIGHT_PAREN,  // ')'
    LEFT_BRACE,   // '{'
    RIGHT_BRACE,  // '}'
    COMMA,        // ','
    DOT,          // '.'
    MINUS,        // '-'
    PLUS,         // '+'
    SEMICOLON,    // ';'
    SLASH,        // '/'
    STAR,         // '*'

    // One or two character tokens
    BANG,         // '!'
    BANG_EQUAL,   // '!='
    EQUAL,        // '='
    EQUAL_EQUAL,  // '=='
    GREATER,      // '>'
    GREATER_EQUAL,// '>='
    LESS,         // '<'
    LESS_EQUAL,   // '<='
    
    // Literals
    IDENTIFIER,   // variable/function names
    STRING,       // string literals
    NUMBER,       // numeric literals
    
    // Keywords
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR, PRINT, 
    RETURN, SUPER, THIS, TRUE, VAR, WHILE, 

    EOF           // End of file/input
}
