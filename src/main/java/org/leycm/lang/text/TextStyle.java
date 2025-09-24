package org.leycm.lang.text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leycm.io.ToStringBuilder;

import java.util.Objects;
import java.util.regex.Pattern;

public class TextStyle {
    private static final Pattern HEX_COLOR_PATTERN = Pattern.compile("^[0-9A-Fa-f]{6}$");
    private static final Pattern HEX_ARGB_PATTERN = Pattern.compile("^[0-9A-Fa-f]{8}$");

    private String rgb;
    private String argb;

    private boolean bold;
    private boolean italic;
    private boolean underlined;
    private boolean strikethrough;
    private boolean censored;

    private String insertText;
    private String shadowColor;
    private Float shadowAlpha;
    private String hoverAction;
    private String hoverValue;
    private String clickAction;
    private String clickValue;

    private String replacementKey;

    public TextStyle() {}

    /**
     * Sets RGB color (6-digit hex without #)
     */
    public TextStyle rgb(@Nullable String rgb) {
        if (rgb != null && !rgb.isEmpty()) {
            rgb = rgb.replace("#", "").toUpperCase();
            if (HEX_COLOR_PATTERN.matcher(rgb).matches()) {
                this.rgb = rgb;
                this.argb = "FF" + rgb;
            } else {
                throw new IllegalArgumentException("Invalid RGB color format: " + rgb);
            }
        }
        return this;
    }

    /**
     * Sets ARGB color (8-digit hex without #)
     */
    public TextStyle argb(@NotNull String argb) {
        argb = argb.replace("#", "").toUpperCase();

        if (HEX_ARGB_PATTERN.matcher(argb).matches()) {
            this.argb = argb;
            this.rgb = argb.substring(2);
        } else if (HEX_COLOR_PATTERN.matcher(argb).matches()) {
            this.rgb = argb;
            this.argb = "FF" + argb;
        } else {
            throw new IllegalArgumentException("Invalid ARGB color format: " + argb);
        }
        return this;
    }

    public TextStyle replacement(@Nullable String replacementContent) {
        this.replacementKey = replacementContent;
        return this;
    }

    @Nullable
    public String getReplacementKey() {
        return replacementKey;
    }

    public boolean isReplacement() {
        return replacementKey != null;
    }

    public TextStyle bold(boolean value) {
        this.bold = value;
        return this;
    }

    public TextStyle bold() {
        return bold(true);
    }

    public TextStyle italic(boolean value) {
        this.italic = value;
        return this;
    }

    public TextStyle italic() {
        return italic(true);
    }

    public TextStyle underlined(boolean value) {
        this.underlined = value;
        return this;
    }

    public TextStyle underlined() {
        return underlined(true);
    }

    public TextStyle strikethrough(boolean value) {
        this.strikethrough = value;
        return this;
    }

    public TextStyle strikethrough() {
        return strikethrough(true);
    }

    public TextStyle censored(boolean value) {
        this.censored = value;
        return this;
    }

    public TextStyle censored() {
        return censored(true);
    }

    public TextStyle insertText(@Nullable String insertText) {
        this.insertText = insertText;
        return this;
    }

    public TextStyle shadowColor(@Nullable String shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public TextStyle shadowAlpha(@Nullable Float shadowAlpha) {
        if (shadowAlpha != null && (shadowAlpha < 0.0f || shadowAlpha > 1.0f)) {
            throw new IllegalArgumentException("Shadow alpha must be between 0.0 and 1.0");
        }
        this.shadowAlpha = shadowAlpha;
        return this;
    }

    public TextStyle hoverAction(@Nullable String action, @Nullable String value) {
        this.hoverAction = action;
        this.hoverValue = value;
        return this;
    }

    public TextStyle clickAction(@Nullable String action, @Nullable String value) {
        this.clickAction = action;
        this.clickValue = value;
        return this;
    }

    /**
     * Creates a deep copy of this style
     */
    public TextStyle copy() {
        TextStyle copy = new TextStyle();
        copy.rgb = this.rgb;
        copy.argb = this.argb;
        copy.bold = this.bold;
        copy.italic = this.italic;
        copy.underlined = this.underlined;
        copy.strikethrough = this.strikethrough;
        copy.censored = this.censored;
        copy.insertText = this.insertText;
        copy.shadowColor = this.shadowColor;
        copy.shadowAlpha = this.shadowAlpha;
        copy.hoverAction = this.hoverAction;
        copy.hoverValue = this.hoverValue;
        copy.clickAction = this.clickAction;
        copy.clickValue = this.clickValue;
        copy.replacementKey = this.replacementKey;
        return copy;
    }

    /**
     * Merges this style with another, with the other style taking precedence
     */
    public TextStyle merge(@NotNull TextStyle other) {
        TextStyle merged = this.copy();

        if (other.rgb != null) merged.rgb = other.rgb;
        if (other.argb != null) merged.argb = other.argb;
        if (other.bold) merged.bold = true;
        if (other.italic) merged.italic = true;
        if (other.underlined) merged.underlined = true;
        if (other.strikethrough) merged.strikethrough = true;
        if (other.censored) merged.censored = true;
        if (other.insertText != null) merged.insertText = other.insertText;
        if (other.shadowColor != null) merged.shadowColor = other.shadowColor;
        if (other.shadowAlpha != null) merged.shadowAlpha = other.shadowAlpha;
        if (other.hoverAction != null) merged.hoverAction = other.hoverAction;
        if (other.hoverValue != null) merged.hoverValue = other.hoverValue;
        if (other.clickAction != null) merged.clickAction = other.clickAction;
        if (other.clickValue != null) merged.clickValue = other.clickValue;
        if (other.isReplacement()) {
            merged.replacementKey = other.replacementKey;
        }

        return merged;
    }

    /**
     * Checks if this style has no formatting applied
     */
    public boolean isEmpty() {
        return rgb == null && argb == null && !bold && !italic && !underlined
                && !strikethrough && !censored && insertText == null && shadowColor == null
                && shadowAlpha == null && hoverAction == null && hoverValue == null
                && clickAction == null && clickValue == null;
    }

    // Getters
    @Nullable public String getRgb() { return rgb; }
    @Nullable public String getArgb() { return argb; }
    public boolean isBold() { return bold; }
    public boolean isItalic() { return italic; }
    public boolean isUnderlined() { return underlined; }
    public boolean isStrikethrough() { return strikethrough; }
    public boolean isCensored() { return censored; }
    @Nullable public String getInsertText() { return insertText; }
    @Nullable public String getShadowColor() { return shadowColor; }
    @Nullable public Float getShadowAlpha() { return shadowAlpha; }
    @Nullable public String getHoverAction() { return hoverAction; }
    @Nullable public String getHoverValue() { return hoverValue; }
    @Nullable public String getClickAction() { return clickAction; }
    @Nullable public String getClickValue() { return clickValue; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextStyle style)) return false;
        return bold == style.bold && italic == style.italic && underlined == style.underlined &&
                strikethrough == style.strikethrough && censored == style.censored &&
                Objects.equals(rgb, style.rgb) && Objects.equals(argb, style.argb) &&
                Objects.equals(insertText, style.insertText) && Objects.equals(shadowColor, style.shadowColor) &&
                Objects.equals(shadowAlpha, style.shadowAlpha) && Objects.equals(hoverAction, style.hoverAction) &&
                Objects.equals(hoverValue, style.hoverValue) && Objects.equals(clickAction, style.clickAction) &&
                Objects.equals(clickValue, style.clickValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rgb, argb, bold, italic, underlined, strikethrough, censored, insertText,
                shadowColor, shadowAlpha, hoverAction, hoverValue, clickAction, clickValue);
    }

    @Override
    public String toString() {
        return ToStringBuilder.of(TextStyle.class)
                .add("rgb", rgb)
                .add("argb", argb)
                .add("bold", bold)
                .add("italic", italic)
                .add("underlined", underlined)
                .add("strikethrough", strikethrough)
                .add("censored", censored)
                .add("insertText", insertText)
                .add("shadowColor", shadowColor)
                .add("shadowAlpha", shadowAlpha)
                .add("hoverAction", hoverAction)
                .add("hoverValue", hoverValue)
                .add("clickAction", clickAction)
                .add("clickValue", clickValue)
                .add("replacementKey", replacementKey)
                .includeNulls(false)
                .format(ToStringBuilder.Format.SIMPLE)
                .toString();
    }

}