package org.leycm.lang.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Represents a single component of text with its own style
 */
public record TextComponent(String content, TextStyle style) {

    public TextComponent(@NotNull String content, @Nullable TextStyle style) {
        this.content = content;
        this.style = style != null ? style : new TextStyle();
    }

    @Override
    @NotNull
    public String content() {
        return content;
    }

    @Override
    @NotNull
    public TextStyle style() {
        return style;
    }

    /**
     * Creates a deep copy of this component
     */
    @Contract(" -> new")
    public @NotNull TextComponent copy() {
        return new TextComponent(content, style.copy());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextComponent(String content1, TextStyle style1))) return false;
        return Objects.equals(content, content1) &&
                Objects.equals(style, style1);
    }

    @Override
    public @NotNull String toString() {
        return "TextComponent{" +
                "content='" + content + '\'' +
                ", style=" + style +
                '}';
    }
}

