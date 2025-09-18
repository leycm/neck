package org.leycm.neck.lang;

import org.jetbrains.annotations.NotNull;
import org.leycm.neck.lang.adapters.JsonTextAdapter;

import java.util.*;
/**
 * Represents a text object composed of multiple parts, each optionally styled.
 * Provides methods for appending, replacing placeholders, and converting to/from various formats.
 */
public class Text {

    private final List<Part> parts = new ArrayList<>();
    private final TextProcessor provider;

    /**
     * Constructs a Text instance with the given TextProcessor.
     * @param provider the text processor to use for adapters
     */
    public Text(TextProcessor provider) {
        this.provider = provider;
    }

    /**
     * Creates a Text instance with a single part containing the given value.
     * @param value the string value
     * @return a new Text instance
     */
    public static @NotNull Text of(String value) {
        Text text = new Text(TextProcessor.getDefault());
        text.parts.add(new Part(value, null));
        return text;
    }

    /**
     * Appends a new part with the given value and no style.
     * @param value the string to append
     * @return this Text instance
     */
    public Text append(String value) {
        parts.add(new Part(value, null));
        return this;
    }

    /**
     * Appends a new part with the given value and style.
     * @param value the string to append
     * @param style the style to apply
     * @return this Text instance
     */
    public Text append(String value, Style style) {
        parts.add(new Part(value, style));
        return this;
    }

    /**
     * Replaces all placeholders in all parts using the provided map.
     * @param placeholders map of placeholder names to values
     * @return this Text instance
     */
    public Text replace(Map<String, String> placeholders) {
        for (Part part : parts) {
            part.replace(placeholders);
        }
        return this;
    }

    /**
     * Replaces a single placeholder in all parts.
     * @param placeholder the placeholder name (without %)
     * @param value the value to replace with
     * @return this Text instance
     */
    public Text replace(String placeholder, String value) {
        for (Part part : parts) {
            part.replace(placeholder, value);
        }
        return this;
    }

    /**
     * Returns the plain string representation of this text (without styles).
     * @return the plain string
     */
    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        for (Part part : parts) {
            sb.append(part.toPlainString());
        }
        return sb.toString();
    }

    /**
     * Converts this text to a JSON string using the default JSON adapter.
     * @return the JSON string
     */
    public String toJson() {
        return toString(JsonTextAdapter.EXTENDET);
    }

    /**
     * Converts this text to the specified type using a registered adapter.
     * @param type the target type class
     * @param <T> the target type
     * @return the converted object
     * @throws IllegalArgumentException if no adapter is found
     */
    public <T> T to(Class<T> type) {
        Text.Adapter<T> adapter = provider.getTextAdapter(type);
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter for " + type.getName());
        }
        return adapter.to(this);
    }

    /**
     * Converts this text to a string using the provided adapter.
     * @param adapter the adapter to use
     * @return the string representation
     */
    public String toString(@NotNull Text.Adapter<String> adapter) {
        return adapter.to(this);
    }

    /**
     * Creates a Text instance from a string using the provided adapter.
     * @param s the string
     * @param adapter the adapter to use
     * @return the Text instance
     */
    public static Text fromString(String s, @NotNull Text.Adapter<String> adapter) {
        return adapter.from(s);
    }

    /**
     * Creates a Text instance from a JSON string using the default JSON adapter.
     * @param s the JSON string
     * @return the Text instance
     */
    public static Text fromJson(String s) {
        return fromString(s, JsonTextAdapter.EXTENDET);
    }

    /**
     * Creates a Text instance from an object using the default provider.
     * @param object the object to convert
     * @param <T> the object type
     * @return the Text instance
     */
    public static <T> Text from(@NotNull T object) {
        return from(object, TextProcessor.getDefault());
    }

    /**
     * Creates a Text instance from an object using the specified provider.
     * @param object the object to convert
     * @param provider the text processor to use
     * @param <T> the object type
     * @return the Text instance
     * @throws IllegalArgumentException if no adapter is found
     */
    public static <T> Text from(@NotNull T object, @NotNull TextProcessor provider) {
        //noinspection unchecked
        Text.Adapter<T> adapter = provider.getTextAdapter((Class<T>) object.getClass());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter for " + object.getClass().getName());
        }
        return adapter.from(object);
    }

    /**
     * Returns an unmodifiable list of the parts of this text.
     * @return the list of parts
     */
    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    /**
     * Returns the default text processor.
     * @return the default TextProcessor
     */
    public static TextProcessor getProvider() {
        return TextProcessor.getDefault();
    }

    /**
     * Represents a part of the text, optionally styled.
     */
    public static class Part {
        String string;
        Style styles;

        /**
         * Constructs a Part with the given string and styles.
         * @param string the string value
         * @param styles the style, or null
         */
        public Part(String string, Style styles) {
            this.string = string;
            this.styles = styles;
        }

        /**
         * Returns the plain string of this part.
         * @return the string
         */
        public String toPlainString() {
            return string;
        }

        /**
         * Returns the style of this part.
         * @return the style, or null
         */
        public Style getStyles() {
            return styles;
        }

        /**
         * Replaces a single placeholder in this part.
         * @param placeholder the placeholder name (without %)
         * @param value the value to replace with
         */
        public void replace(String placeholder, String value) {
            string = string.replaceAll("%" + placeholder + "%", value);
        }

        /**
         * Replaces all placeholders in this part using the provided map.
         * @param placeholders map of placeholder names to values
         */
        public void replace(@NotNull Map<String, String> placeholders) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                string = string.replaceAll("%" + placeholder.getKey() + "%", placeholder.getValue());
            }
        }
    }

    /**
     * Adapter interface for converting between Text and other types.
     * @param <T> the target type
     */
    public interface Adapter<T> {
        /**
         * Converts a Text instance to the target type.
         * @param text the Text instance
         * @return the converted object
         */
        T to(Text text);

        /**
         * Converts an object of the target type to a Text instance.
         * @param object the object to convert
         * @return the Text instance
         */
        Text from(T object);
    }
}