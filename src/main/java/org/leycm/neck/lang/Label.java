package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Function;

public class Label {
    private final Function<Locale, Text> label;
    private final String path;
    private final String key;

    /**
     * Creates a Label from a string representation
     * @param serialized the string to parse (format: "path:key")
     * @return a new Label instance
     * @throws IllegalArgumentException if the string format is invalid
     */
    @Contract("null -> fail")
    public static @NotNull Label fromString(String serialized) {
        if (serialized == null || serialized.isEmpty()) throw new IllegalArgumentException("Invalid string for Label deserialization");

        String[] parts = serialized.split("/", 2);
        if (parts.length != 2) throw new IllegalArgumentException("String format should be 'path:key'");

        String path = parts[0];
        String key = parts[1];

        if (path.isEmpty() || key.isEmpty()) throw new IllegalArgumentException("Path and key cannot be empty");

        return new Label(path, key, TextProvider.getDefault());
    }

    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull Label of(String path, String key) {
        return new Label(path, key, TextProvider.getDefault());
    }



    private Label(String path, String key, TextProvider provider) {
        this.label = lang -> provider.translationOf(path, key, lang);
        this.path = path;
        this.key = key;
    }

}