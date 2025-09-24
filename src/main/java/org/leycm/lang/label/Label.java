package org.leycm.lang.label;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.leycm.lang.text.Text;

import java.util.Locale;
import java.util.function.Function;

public class Label {
    protected final Function<Locale, Text> label;
    protected final String path;
    protected final String key;

    @Contract("null -> fail")
    public static @NotNull Label parse(String serialized) {
        if (serialized == null || serialized.isEmpty()) {
            throw new IllegalArgumentException("Invalid string for Label deserialization");
        }

        if (serialized.startsWith("<text:") && serialized.endsWith(">")) {
            String cleaned = serialized.substring(6, serialized.length() - 1);
            return PreLabel.of(Text.fromJson(cleaned));
        }

        if (serialized.startsWith("<lang:") && serialized.endsWith(">")) {
            String cleaned = serialized.substring(6, serialized.length() - 1);
            String[] parts = cleaned.split("/", 2);
            if (parts.length != 2) {
                throw new IllegalArgumentException("String format should be 'key/path'");
            }
            return new Label(parts[1], parts[0], TranslationProvider.getInstance());
        }

        throw new IllegalArgumentException("This is not a Label");
    }

    @Contract("_ -> new")
    public static @NotNull Label text(String text) {
        return PreLabel.of(Text.of(text));
    }

    @Contract("_ -> new")
    public static @NotNull Label text(Text text) {
        return PreLabel.of(text);
    }

    @Contract("_, _ -> new")
    public static @NotNull Label of(String path, String key) {
        return new Label(path, key, TranslationProvider.getInstance());
    }

    Label(String path, String key, TranslationProvider provider) {
        this.label = lang -> provider.translationOf(path, key, lang);
        this.path = path;
        this.key = key;
    }

    public Text inDefaultLang() {
        return in(null);
    }

    public Text in(Locale lang) {
        return label.apply(lang);
    }

    public String getPath() {
        return path;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "<lang:" + key + "/" + path + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Label l)) return false;
        return this.path.equalsIgnoreCase(l.path) || this.key.equalsIgnoreCase(l.key);
    }
}
