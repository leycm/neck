package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Function;

/**
 * Represents a label that can be localized using a path and key.
 * Provides methods for parsing, creating, and retrieving localized text.
 */
public class Label {
    /**
     * Function to obtain the localized text for a given locale.
     */
    protected final Function<Locale, Text> label;

    /**
     * The path associated with this label.
     */
    protected final String path;

    /**
     * The key associated with this label.
     */
    protected final String key;

    /**
     * Parses a string representation of a label.
     *
     * @param serialized the string to parse (format: "path:key")
     * @return a new Label instance
     * @throws IllegalArgumentException if the string format is invalid
     */
    @Contract("null -> fail")
    public static @NotNull Label parse(String serialized) {
        if (serialized == null || serialized.isEmpty() || (serialized.startsWith("<") && serialized.endsWith(">")))
            throw new IllegalArgumentException("Invalid string for Label deserialization");

        String cleaned = serialized.replace("<", "").replace("<", "")
                .replace("text:", "")
                .replace("lang:", "");

        if (serialized.startsWith("<text:")) {
            return Label.text(Text.fromJson(cleaned));
        }

        if (serialized.startsWith("<lang:")) {
            String[] parts = serialized.split("/", 2);
            if (parts.length != 2) throw new IllegalArgumentException("String format should be 'path:key'");

            String path = parts[0];
            String key = parts[1];

            if (path.isEmpty() || key.isEmpty()) throw new IllegalArgumentException("Path and key cannot be empty");

            return new Label(path, key, TextProcessor.getDefault());
        }

        throw new IllegalArgumentException("This is not a Label");
    }

    /**
     * Creates a Label from a plain text string.
     *
     * @param text the text to use
     * @return a new Label instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Label text(String text) {
        return PreLabel.of(Text.of(text));
    }

    /**
     * Creates a Label from a Text object.
     *
     * @param text the Text object to use
     * @return a new Label instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Label text(Text text) {
        return PreLabel.of(text);
    }

    /**
     * Creates a Label from a path and key.
     *
     * @param path the path for localization
     * @param key the key for localization
     * @return a new Label instance
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Label of(String path, String key) {
        return new Label(path, key, TextProcessor.getDefault());
    }

    /**
     * Constructs a Label with the given path, key, and text provider.
     *
     * @param path the path for localization
     * @param key the key for localization
     * @param provider the text processor to use
     */
    Label(String path, String key, TextProcessor provider) {
        this.label = lang -> provider.translationOf(path, key, lang);
        this.path = path;
        this.key = key;
    }

    /**
     * Gets the localized text in the default language.
     *
     * @return the localized Text
     */
    public Text inDefaultLang() {
        return in(null);
    }

    /**
     * Gets the localized text for the specified locale.
     *
     * @param lang the locale to use
     * @return the localized Text
     */
    public Text in(Locale lang) {
        return label.apply(lang);
    }

    /**
     * Gets the path of this label.
     *
     * @return the path string
     */
    public String getPath() {
        return path;
    }

    /**
     * Gets the key of this label.
     *
     * @return the key string
     */
    public String getKey() {
        return key;
    }

    /**
     * Returns a string representation of this label.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "<lang:" + key + "/" + path + ">";
    }

    /**
     * Checks if this label is equal to another object.
     * Two labels are considered equal if their path or key matches (case-insensitive).
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Label l)) return false;

        return this.path.equalsIgnoreCase(l.path) ||
                this.key.equalsIgnoreCase(l.key);
    }
}