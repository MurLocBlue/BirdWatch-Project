package com.birdwatch.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.regex.Pattern;

/**
 * Utility class for sanitizing and validating input strings.
 * Provides methods to prevent SQL injection and ensure safe content.
 */
public class InputSanitizer {
    // Pattern to allow only alphanumeric characters, spaces, hyphens, and basic punctuation
    private static final Pattern SAFE_INPUT_PATTERN = Pattern.compile("^[a-zA-Z0-9\\s\\-.,:;()]+$");
    
    // Maximum length for input strings
    private static final int MAX_INPUT_LENGTH = 100;

    private InputSanitizer() {
        // Private constructor to prevent instantiation
    }

    /**
     * Sanitizes input string to prevent SQL injection and ensure safe content.
     *
     * @param input The input string to sanitize
     * @return The sanitized string
     * @throws ResponseStatusException if the input is invalid
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        // Trim whitespace
        input = input.trim();
        
        // Check length
        if (input.length() > MAX_INPUT_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Input exceeds maximum length of " + MAX_INPUT_LENGTH + " characters");
        }
        
        // Check for safe characters
        if (!SAFE_INPUT_PATTERN.matcher(input).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                "Input contains invalid characters. Only letters, numbers, spaces, hyphens, and basic punctuation are allowed");
        }
        
        return input;
    }
} 