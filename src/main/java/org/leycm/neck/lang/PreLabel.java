package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * Represents a label that wraps a {@link Text} object and always returns it regardless of locale.
 */
public class PreLabel extends Label {
    /**
     * The wrapped text.
     */
    protected Text text;

    /**
     * Creates a new {@code PreLabel} instance with the given text.
     *
     * @param text the text to wrap
     * @return a new {@code PreLabel} instance
     */
    @Contract(value = "_, -> new", pure = true)
    public static @NotNull Label of(Text text) {
        return new PreLabel(text);
    }

    /**
     * Constructs a {@code PreLabel} with the specified text.
     *
     * @param text the text to wrap
     */
    private PreLabel(Text text) {
        super(null, null, TextProcessor.getDefault());
        this.text = text;
    }

    /**
     * Returns the wrapped text, ignoring the locale.
     *
     * @param lang the locale (ignored)
     * @return the wrapped {@code Text}
     */
    @Override
    public Text in(Locale lang) {
        return text;
    }

    /**
     * Returns a string representation of this {@code PreLabel}.
     *
     * @return a string in the format {@code <text:...>}
     */
    @Override
    public String toString() {
        return "<text:" + text.toJson() + ">";
    }

    /**
     * Checks if this {@code PreLabel} is equal to another object.
     * Two {@code PreLabel} instances are equal if their wrapped texts are equal by plain string.
     *
     * @param obj the object to compare
     * @return {@code true} if equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PreLabel l)) return false;
        return this.text.toPlainString().equals(l.text.toPlainString());
    }
}
