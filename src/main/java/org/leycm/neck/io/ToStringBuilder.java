package org.leycm.neck.io;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility for building customizable string representations of objects.
 * Supports multiple formats: SIMPLE, JSON, and URL.
 */
public class ToStringBuilder {

    /**
     * Output formats for the string representation.
     */
    public enum Format {
        /**
         * Example: CachedIdentifier{type=1, size="100x200"}
         */
        SIMPLE,
        /**
         * Example: {"type":1,"size":"100x200"}
         */
        JSON,
        /**
         * Example: type=1&size=100x200
         */
        URL
    }

    private final String className;


    private final Map<String, Object> values = new LinkedHashMap<>();


    private boolean includeNulls = true;


    private Format format = Format.SIMPLE;

    /**
     * Creates a builder for the given class.
     * @param clazz the class whose name will be used in the output
     */
    private ToStringBuilder(@NotNull Class<?> clazz) {
        this.className = clazz.getSimpleName();
    }

    /**
     * Creates a new builder for the given class.
     * @param clazz the class whose name will be used in the output
     * @return a new ToStringBuilder instance
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ToStringBuilder of(Class<?> clazz) {
        return new ToStringBuilder(clazz);
    }

    /**
     * Adds a field and its value.
     * @param fieldName the field name
     * @param value the value (nullable)
     * @return this builder
     */
    public ToStringBuilder add(String fieldName, @Nullable Object value) {
        values.put(fieldName, value);
        return this;
    }

    /**
     * Adds a field with a custom formatter for its value.
     * @param fieldName the field name
     * @param value the value (nullable)
     * @param formatter function to format the value
     * @return this builder
     */
    public ToStringBuilder add(String fieldName, @Nullable Object value, @NotNull Function<Object, String> formatter) {
        values.put(fieldName, new FormattedValue(value, formatter));
        return this;
    }

    /**
     * Adds a field with multiple values joined by a separator.
     * @param fieldName the field name
     * @param separator the separator string
     * @param values the values to join (nullable)
     * @return this builder
     */
    public ToStringBuilder add(String fieldName, @NotNull String separator, @Nullable Object... values) {
        if (values == null || values.length == 0) {
            return this;
        }

        String joinedValue = Stream.of(values)
                .filter(Objects::nonNull)
                .map(Objects::toString)
                .collect(Collectors.joining(separator));

        if (!joinedValue.isEmpty()) {
            this.values.put(fieldName, joinedValue);
        } else if (includeNulls) {
            this.values.put(fieldName, null);
        }

        return this;
    }

    /**
     * Includes null values in the output.
     * @return this builder
     */
    public ToStringBuilder includeNulls() {
        this.includeNulls = true;
        return this;
    }

    /**
     * Sets whether to include null values in the output.
     * @param include true to include nulls, false to skip them
     * @return this builder
     */
    public ToStringBuilder includeNulls(boolean include) {
        this.includeNulls = include;
        return this;
    }

    /**
     * Sets the output format.
     * @param format the desired format
     * @return this builder
     */
    public ToStringBuilder format(@NotNull Format format) {
        this.format = format;
        return this;
    }

    /**
     * Builds the string representation according to the selected format.
     * @return the string representation
     */
    @Override
    public String toString() {
        return switch (format) {
            case SIMPLE -> buildSimpleFormat();
            case JSON -> buildJsonFormat();
            case URL -> buildUrlArgsFormat();
        };
    }

    /**
     * Builds the SIMPLE format string.
     */
    private @NotNull String buildSimpleFormat() {
        StringBuilder sb = new StringBuilder(className).append("{");
        buildContent(sb, "=", ", ", this::formatSimpleValue);
        return sb.append("}").toString();
    }

    /**
     * Builds the JSON format string.
     */
    private @NotNull String buildJsonFormat() {
        StringBuilder sb = new StringBuilder("{");
        buildContent(sb, "\":\"", "\",\"", this::formatJsonValue);
        return sb.append("\"}").toString();
    }

    /**
     * Builds the URL argument format string.
     */
    private @NotNull String buildUrlArgsFormat() {
        StringBuilder sb = new StringBuilder();
        buildContent(sb, "=", "&", this::formatUrlValue);
        return sb.toString();
    }

    /**
     * Appends formatted key-value pairs to the StringBuilder.
     * @param sb the StringBuilder
     * @param keyValueSeparator separator between key and value
     * @param entrySeparator separator between entries
     * @param valueFormatter function to format values
     */
    private void buildContent(StringBuilder sb,
                              String keyValueSeparator,
                              String entrySeparator,
                              Function<Object, String> valueFormatter) {
        boolean first = true;
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            Object value = entry.getValue() instanceof FormattedValue fv ? fv.value : entry.getValue();

            if (!includeNulls && value == null) {
                continue;
            }

            if (!first) {
                sb.append(entrySeparator);
            }
            first = false;

            sb.append(switch (format) {
                case JSON -> "\"" + entry.getKey() + keyValueSeparator;
                case URL -> "?" + entry.getKey() + keyValueSeparator;
                default -> entry.getKey() + keyValueSeparator;
            });

            sb.append(valueFormatter.apply(entry.getValue()));
        }
    }

    /**
     * Formats a value for SIMPLE output.
     */
    private String formatSimpleValue(Object value) {
        if (value instanceof FormattedValue(Object value1, Function<Object, String> formatter)) {
            return formatter.apply(value1);
        }
        if (value instanceof CharSequence) {
            return "\"" + value + "\"";
        }
        return Objects.toString(value);
    }

    /**
     * Formats a value for JSON output.
     */
    private String formatJsonValue(Object value) {
        if (value instanceof FormattedValue(Object value1, Function<Object, String> formatter)) {
            return formatter.apply(value1);
        }
        return Objects.toString(value);
    }

    /**
     * Formats a value for URL output.
     */
    private String formatUrlValue(Object value) {
        if (value instanceof FormattedValue(Object value1, Function<Object, String> formatter)) {
            return formatter.apply(value1);
        }
        return Objects.toString(value);
    }

    /**
     * Holds a value and its formatter.
     * Used for custom formatting of individual fields.
     * @param value the value
     * @param formatter the formatter function
     */
    private record FormattedValue(Object value, Function<Object, String> formatter) {}
}