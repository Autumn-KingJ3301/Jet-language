package com.jet.lang;

/**
 * Represents a token produced by the Jet language lexer.
 * Each token has a type, the original lexeme, an optional literal value, and the line number where it appears.
 */
public class Token {
    final TokenType type;    // The type of token (e.g., IDENTIFIER, NUMBER, etc.)
    final String lexeme;     // The actual string from the source code
    final Object literal;    // The literal value, if applicable (e.g., number or string value)
    final int line;          // The line number where the token appears

    /**
     * Constructs a new Token instance.
     *
     * @param type    The type of the token.
     * @param lexeme  The string as it appears in the source code.
     * @param literal The literal value (if any) associated with the token.
     * @param line    The line number where the token was found.
     */
    Token(TokenType type, String lexeme, Object literal, int line){
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    /**
     * Returns a string representation of the token for debugging purposes.
     *
     * @return A string containing the token type, lexeme, and literal value.
     */
    public String toString(){
        return type + " " + lexeme + " " + literal;
    }
}
