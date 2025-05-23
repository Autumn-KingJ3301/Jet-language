package com.jet.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jet.lang.TokenType.*;

/**
 * The Scanner class is responsible for converting Jet source code into a list
 * of tokens.
 * <p>
 * It performs lexical analysis, breaking the input string into meaningful
 * symbols for parsing.
 * </p>
 */
class Scanner {
    private final String source; // The source code to scan
    private final List<Token> tokens = new ArrayList<>(); // List to hold generated tokens
    private int start = 0; // Start index of the current lexeme
    private int current = start; // Current index in the source
    private int line = 1; // Current line number in the source
    private static final Map<String, TokenType> keywords; // Map of reserverd words for the language.

    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

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
                } else if (match('*')) {
                    multilineComment();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace characters (space, carriage return, tab)
                break;
            case '\n':
                // Newline encountered, increment line counter
                line++;
                break;
            case '"':
                // Start of a string literal
                string();
                break;
            default:
                // Handle numbers, identifiers, or report unexpected characters
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Jet.error(line, "Unexpected character.");
                    break;
                }
        }
    }

    private void multilineComment() {
        while (!(peek() == '*' && peekNext() == '/') && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }
        if (!isAtEnd()) {
            advance();
            advance();
        }else{
            Jet.error(line, "Unterminated multi line comment");
        }

    }

    /**
     * Scans an identifier or keyword from the source code.
     * If the scanned text matches a reserved keyword, its token type is used.
     * Otherwise, it is treated as a generic identifier.
     */
    private void identifier() {
        while (isAlphaNumeric(peek()))
            advance();
        String text = source.substring(start, current);
        TokenType type = keywords.get(text); // Check with the hashmap to find a match, if exists we add that or it is
                                             // recognized as a keyword
        if (type == null)
            type = IDENTIFIER;
        addToken(type);
    }

    /**
     * Checks if a character is an alphabetic letter (a-z, A-Z) or underscore.
     *
     * @param c The character to check.
     * @return true if the character is a letter or underscore, false otherwise.
     */
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    /**
     * Checks if a character is alphanumeric (letter, digit, or underscore).
     *
     * @param c The character to check.
     * @return true if the character is alphanumeric, false otherwise.
     */
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Scans a number literal from the source code.
     * Handles both integer and floating-point numbers.
     */
    private void number() {
        while (isDigit(peek()))
            advance();

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            advance(); // Consume the '.'
            while (isDigit(peek()))
                advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    /**
     * Returns the character after the current one without consuming it.
     *
     * @return The next character, or '\0' if at end.
     *         Our Scanner looks ahead mostly two characters.
     */
    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
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
