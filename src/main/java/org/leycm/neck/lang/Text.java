package org.leycm.neck.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.NotNull;
import org.leycm.neck.lang.adapters.JsonTextAdapter;

import java.util.*;

public class Text {

    private final List<Part> parts = new ArrayList<>();
    private final TextProvider provider;

    public Text(TextProvider provider) {
        this.provider = provider;
    }

    public static @NotNull Text of(String value) {
        Text text = new Text(TextProvider.getDefault());
        text.parts.add(new Part(value, null));
        return text;
    }

    public Text append(String value) {
        parts.add(new Part(value, null));
        return this;
    }

    public Text append(String value, Style style) {
        parts.add(new Part(value, style));
        return this;
    }

    public Text replace(Map<String, String> placeholders) {
        for (Part part : parts) {
            part.replace(placeholders);
        }
        return this;
    }

    public Text replace(String placeholder, String value) {
        for (Part part : parts) {
            part.replace(placeholder, value);
        }
        return this;
    }

    public String toPlainString() {
        StringBuilder sb = new StringBuilder();
        for (Part part : parts) {
            sb.append(part.toPlainString());
        }
        return sb.toString();
    }

    public String toJson() {
        return toString(JsonTextAdapter.EXTENDET);
    }

    public <T> T to(Class<T> type) {
        Text.Adapter<T> adapter = provider.getTextAdapter(type);
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter for " + type.getName());
        }
        return adapter.to(this);
    }

    public String toString(@NotNull Text.Adapter<String> adapter) {
        return adapter.to(this);
    }

    public static Text fromString(String s, @NotNull Text.Adapter<String> adapter) {
        return adapter.from(s);
    }

    public static Text fromJson(String s) {
        return fromString(s, JsonTextAdapter.EXTENDET);
    }

    public static <T> Text from(@NotNull T object) {
        return from(object, TextProvider.getDefault());
    }

    public static <T> Text from(@NotNull T object, @NotNull TextProvider provider) {
        //noinspection unchecked
        Text.Adapter<T> adapter = provider.getTextAdapter((Class<T>) object.getClass());
        if (adapter == null) {
            throw new IllegalArgumentException("No adapter for " + object.getClass().getName());
        }
        return adapter.from(object);
    }

    public List<Part> getParts() {
        return Collections.unmodifiableList(parts);
    }

    public static TextProvider getProvider() {
        return TextProvider.getDefault();
    }

    // === Records ===
    public static class Part {
        String string;
        Style styles;

        public Part(String string, Style styles) {
            this.string = string;
            this.styles = styles;
        }

        public String toPlainString() {
            return string;
        }

        public Style getStyles() {
            return styles;
        }

        public void replace(String placeholder, String value) {
            string = string.replaceAll("%" + placeholder + "%", value);
        }

        public void replace(@NotNull Map<String, String> placeholders) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
                string = string.replaceAll("%" + placeholder.getKey() + "%", placeholder.getValue());
            }
        }
    }

    public interface Adapter<T> {
        T to(Text text);
        Text from(T object);
    }
}