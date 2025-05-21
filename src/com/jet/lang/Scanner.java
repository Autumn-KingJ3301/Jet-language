package com.jet.lang;

import java.util.ArrayList;
import java.util.List;

import static com.jet.lang.TokenType.*;

/**
 * The Scanner class is responsible for converting Jet source code into a list
 * of tokens.
 * It performs lexical analysis, breaking the input string into meaningful
 * symbols for parsing.
 */
class Scanner {
    private final String source; // The source code to scan
    private final List<Token> tokens = new ArrayList<>(); // List to hold generated tokens
    private int start = 0; // Start index of the current lexeme
    private int current = start; // Current index in the source
    private int line = 1; // Current line number in the source

    /**
     * Constructs a Scanner for the given source code.
     *
     * @param source The Jet source code to scan.
     */
    Scanner(String source) {
        this.source = source;
    }

    /**
     * Scans the entire source code and returns a list of tokens.
     *
     * @return List of tokens found in the source code.
     */
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", NIL, line)); // Add EOF token at the end
        return tokens;
    }

    /**
     * Checks if the scanner has reached the end of the source code.
     *
     * @return true if at end, false otherwise.
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * Scans a single token from the source code and adds it to the token list.
     */
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREM);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('0') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    // Skip comment
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else {
                    Jet.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    /**
     * Scans a number literal from the source code.
     * Handles both integer and floating-point numbers.
     */
    private void number() {
        while (isDigit(peek())) advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // Consume the '.'
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Returns the character after the current one without consuming it.
     *
     * @return The next character, or '\0' if at end.
     */
    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /**
     * Checks if a character is a digit (0-9).
     *
     * @param i The character to check.
     * @return true if the character is a digit, false otherwise.
     */
    private boolean isDigit(char i) {
        return i >= '0' && i <= '9';
    }

    /**
     * Handles string literals in the source code.
     * Scans until the closing quote or end of file, reporting errors for
     * unterminated strings.
     */
    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }
        if (isAtEnd()) {
            Jet.error(line, "Unterminated String!");
        }

        // For the closing "
        advance();

        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /**
     * Returns the next character in the source without consuming it.
     *
     * @return The next character, or '\0' if at end.
     */
    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    /**
     * Consumes the next character if it matches the expected character.
     *
     * @param expected The character to match.
     * @return true if matched and consumed, false otherwise.
     */
    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;
        current++;
        return true;
    }

    /**
     * Advances the scanner to the next character and returns it.
     *
     * @return The next character in the source.
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Adds a token of the given type with no literal value.
     *
     * @param type The type of token to add.
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Adds a token of the given type and literal value.
     *
     * @param type    The type of token to add.
     * @param literal The literal value for the token.
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

}
