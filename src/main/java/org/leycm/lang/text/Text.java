package org.leycm.lang.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leycm.adapter.AdapterProvider;
import org.leycm.adapter.ObjectAdapter;
import org.leycm.lang.adapter.ReplacementTextAdapter;

import java.util.*;

/**
 * Represents styled text with support for formatting and serialization
 */
public record Text(List<TextComponent> components) {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @ApiStatus.Internal
    public Text(List<TextComponent> components) {
        this.components = new ArrayList<>(components);
    }

    /**
     * Creates a simple text without styling and text
     */
    @Contract(" -> new")
    public static @NotNull Text empty() {
        return Text.of(List.of(new TextComponent("", new TextStyle())));
    }

    /**
     * Creates a simple text without styling
     */
    @Contract("_ -> new")
    public static @NotNull Text of(@NotNull String content) {
        return Text.of(List.of(new TextComponent(content, new TextStyle())));
    }

    /**
     * Creates text with a specific style
     */
    @Contract("_, _ -> new")
    public static @NotNull Text of(@NotNull String content, @NotNull TextStyle style) {
        return Text.of(List.of(new TextComponent(content, style)));
    }

    /**
     * Creates text from multiple components
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Text of(@NotNull List<TextComponent> components) {
        return ReplacementTextAdapter.REPLACE.from(new Text(components));
    }

    /**
     * Creates text from a string using an adapter
     */
    public static Text fromString(@NotNull String content, @Nullable TextAdapter<String> adapter) {
        if (adapter == null) {
            return of(content);
        }
        return adapter.from(content);
    }

    /**
     * Deserializes text from JSON
     */
    public static @NotNull Text fromJson(@NotNull String json) {
        try {
            TextData data = GSON.fromJson(json, TextData.class);
            if (data.components == null || data.components.isEmpty()) {
                return of(data.content != null ? data.content : "");
            }
            return new Text(data.components);
        } catch (Exception e) {
            return of(json);
        }
    }

    /**
     * Creates a Text instance from an object using the adapter provider.
     * @param object the object to convert
     * @param <T> the object type
     * @return the Text instance
     * @throws IllegalArgumentException if no adapter is found
     */
    public static <T> Text from(@NotNull T object) {
        @SuppressWarnings("unchecked")
        Class<T> sourceClass = (Class<T>) object.getClass();

        Optional<ObjectAdapter<Text, T>> adapter = AdapterProvider.find(Text.class, sourceClass);
        if (adapter.isEmpty()) {
            throw new IllegalArgumentException("No adapter registered for " + sourceClass.getName() + " -> Text");
        }
        return adapter.get().from(object);
    }

    /**
     * Creates a Text instance from a string using the provided adapter.
     * @param content the string
     * @param adapter the adapter to use
     * @return the Text instance
     */
    public static Text fromString(@NotNull String content, @NotNull ObjectAdapter<Text, String> adapter) {
        return adapter.from(content);
    }

    /**
     * Appends another text to this one
     */
    @Contract("_ -> new")
    public @NotNull Text append(@NotNull Text other) {
        List<TextComponent> newComponents = new ArrayList<>(this.components);
        newComponents.addAll(other.components);
        return new Text(newComponents);
    }

    /**
     * Appends styled text
     */
    @Contract("_, _ -> new")
    public @NotNull Text append(@NotNull String content, @NotNull TextStyle style) {
        return append(Text.of(content, style));
    }

    public @NotNull Text replace(@NotNull String key, String value) {
        return replace(Map.of(key, value));
    }

    /**
     * Replaces all occurrences of keys in the map with their corresponding values in all components.
     */
    @Contract("_ -> new")
    public @NotNull Text replace(@NotNull Map<String, String> replacements) {
        if (replacements.isEmpty() || this.isEmpty()) return this;

        List<TextComponent> newComponents = new ArrayList<>();
        for (TextComponent component : components) {
            String newContent = component.content();

            // Priority 1: replace by replacementKey if present
            if (component.style().isReplacement()) {
                String key = component.style().getReplacementKey();
                if (key != null && replacements.containsKey(key)) {
                    newContent = replacements.get(key);
                    // clear replacement flag on the copied style
                    TextStyle copied = component.style().copy().replacement(null);
                    newComponents.add(new TextComponent(newContent, copied));
                    continue;
                }
            }

            // Priority 2: literal in-content replacements
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                newContent = newContent.replace(entry.getKey(), entry.getValue());
            }
            newComponents.add(new TextComponent(newContent, component.style().copy()));
        }
        return new Text(newComponents);
    }

    /**
     * Appends plain text
     */
    @Contract("_ -> new")
    public @NotNull Text append(@NotNull String content) {
        return append(Text.of(content));
    }

    /**
     * Returns the plain text content without formatting
     */
    public String toPlainString() {
        return components.stream()
                .map(TextComponent::content)
                .reduce("", String::concat);
    }

    /**
     * Converts this text to a JSON string using the registered JSON adapter.
     * @return the JSON string
     * @throws IllegalArgumentException if no JSON adapter is registered
     */
    public String toJson() {
        Optional<ObjectAdapter<Text, String>> adapter = AdapterProvider.find(Text.class, String.class);
        if (adapter.isPresent()) {
            return adapter.get().to(this);
        }

        // Fallback to built-in JSON serialization if no adapter is registered
        if (components.size() == 1 && components.get(0).style().isEmpty()) {
            return GSON.toJson(new TextData(toPlainString(), null));
        }
        return GSON.toJson(new TextData(null, components));
    }

    /**
     * Converts this text to the specified type using a registered adapter.
     * @param type the target type class
     * @param <T> the target type
     * @return the converted object
     * @throws IllegalArgumentException if no adapter is found
     */
    public <T> T to(@NotNull Class<T> type) {
        Optional<ObjectAdapter<Text, T>> adapter = AdapterProvider.find(Text.class, type);
        if (adapter.isEmpty()) {
            throw new IllegalArgumentException("No adapter registered for Text -> " + type.getName());
        }
        return adapter.get().to(this);
    }

    /**
     * Converts this text to a string using the provided adapter.
     * @param adapter the adapter to use
     * @return the string representation
     */
    public String toString(@NotNull ObjectAdapter<Text, String> adapter) {
        return adapter.to(this);
    }

    /**
     * Gets all text components
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull List<TextComponent> components() {
        return new ArrayList<>(components);
    }

    /**
     * Checks if text is empty
     */
    public boolean isEmpty() {
        return components.isEmpty() || toPlainString().trim().isEmpty();
    }

    @Override
    public String toString() {
        return toPlainString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Text(List<TextComponent> components1))) return false;
        return Objects.equals(components, components1);
    }

    private static class TextData {
        String content;
        List<TextComponent> components;

        TextData(String content, List<TextComponent> components) {
            this.content = content;
            this.components = components;
        }
    }
}