package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a text style with various formatting options such as color, bold, italic, underline, strikethrough, and actions.
 * Provides a fluent API for setting style properties.
 */
public class Style {
    /**
     * The RGB color code (6 hex digits, e.g., "FF00FF").
     */
    private String rgb;

    /**
     * The ARGB color code (8 hex digits, e.g., "FFFF00FF").
     */
    private String argb;

    /**
     * Whether the text is bold.
     */
    private boolean bold;

    /**
     * Whether the text is italic.
     */
    private boolean italic;

    /**
     * Whether the text is underlined.
     */
    private boolean underlined;

    /**
     * Whether the text is strikethrough.
     */
    private boolean strikethrough;

    /**
     * Whether the text is censored.
     */
    private boolean censored;

    /**
     * Text to insert on click.
     */
    private String insertText;

    /**
     * The color of the text shadow.
     */
    private String shadowColor;

    /**
     * The alpha (opacity) of the text shadow.
     */
    private Float shadowAlpha;

    /**
     * The hover action type (e.g., "show_text").
     */
    private String hoverAction;

    /**
     * The value for the hover action.
     */
    private String hoverValue;
    /**
     * The click action type (e.g., "open_url").
     */
    private String clickAction;

    /**
     * The value for the click action.
     */
    private String clickValue;

    /**
     * Constructs a new, empty Style.
     */
    public Style() {
    }

    /**
     * Sets the RGB color code for this style.
     * Also sets the ARGB code with alpha "FF".
     * @param rgb the RGB color code (6 hex digits)
     * @return this Style instance
     */
    public Style rgb(String rgb) {
        if (rgb != null && !rgb.isEmpty()) {
            this.rgb = rgb.toUpperCase();
            this.argb = "FF" + this.rgb;
        }
        return this;
    }

    /**
     * Sets the ARGB color code for this style.
     * If the code is 8+ digits, sets ARGB and extracts RGB.
     * If 6+ digits, sets RGB and ARGB with alpha "FF".
     * @param argb the ARGB color code (6 or 8 hex digits)
     * @return this Style instance
     */
    @Contract("_ -> this")
    public Style argb(@NotNull String argb) {
        if (argb.length() >= 8) {
            this.argb = argb.toUpperCase();
            this.rgb = argb.substring(2);
        } else if (argb.length() >= 6) {
            this.rgb = argb.toUpperCase();
            this.argb = "FF" + this.rgb;
        }
        return this;
    }

    /**
     * Sets whether the text is bold.
     * @param value true for bold, false otherwise
     * @return this Style instance
     */
    public Style bold(boolean value) {
        this.bold = value;
        return this;
    }

    /**
     * Sets whether the text is italic.
     * @param value true for italic, false otherwise
     * @return this Style instance
     */
    public Style italic(boolean value) {
        this.italic = value;
        return this;
    }

    /**
     * Sets whether the text is underlined.
     * @param value true for underlined, false otherwise
     * @return this Style instance
     */
    public Style underlined(boolean value) {
        this.underlined = value;
        return this;
    }

    /**
     * Sets whether the text is strikethrough.
     * @param value true for strikethrough, false otherwise
     * @return this Style instance
     */
    public Style strikethrough(boolean value) {
        this.strikethrough = value;
        return this;
    }

    /**
     * Sets whether the text is censored.
     * @param value true for censored, false otherwise
     * @return this Style instance
     */
    public Style censored(boolean value) {
        this.censored = value;
        return this;
    }

    /**
     * Sets the text to insert on click.
     * @param insertText the text to insert
     * @return this Style instance
     */
    public Style insertText(String insertText) {
        this.insertText = insertText;
        return this;
    }

    /**
     * Sets the color of the text shadow.
     * @param shadowColor the shadow color code
     * @return this Style instance
     */
    public Style shadowColor(String shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    /**
     * Sets the alpha (opacity) of the text shadow.
     * @param shadowAlpha the shadow alpha value
     * @return this Style instance
     */
    public Style shadowAlpha(Float shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
        return this;
    }

    /**
     * Sets the hover action and its value.
     * @param action the hover action type
     * @param value the hover action value
     * @return this Style instance
     */
    public Style hoverAction(String action, String value) {
        this.hoverAction = action;
        this.hoverValue = value;
        return this;
    }

    /**
     * Sets the click action and its value.
     * @param action the click action type
     * @param value the click action value
     * @return this Style instance
     */
    public Style clickAction(String action, String value) {
        this.clickAction = action;
        this.clickValue = value;
        return this;
    }

    /**
     * Creates a copy of this style with the same properties.
     *
     * @return a new Style instance with identical properties
     */
    public @NotNull Style copy() {
        Style copy = new Style();
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
        return copy;
    }

    // Getters

    /** @return the RGB color code */
    public String getRgb() { return rgb; }
    /** @return the ARGB color code */
    public String getArgb() { return argb; }
    /** @return true if bold, false otherwise */
    public boolean isBold() { return bold; }
    /** @return true if italic, false otherwise */
    public boolean isItalic() { return italic; }
    /** @return true if underlined, false otherwise */
    public boolean isUnderlined() { return underlined; }
    /** @return true if strikethrough, false otherwise */
    public boolean isStrikethrough() { return strikethrough; }
    /** @return true if censored, false otherwise */
    public boolean isCensored() { return censored; }
    /** @return the insert text */
    public String getInsertText() { return insertText; }
    /** @return the shadow color */
    public String getShadowColor() { return shadowColor; }
    /** @return the shadow alpha */
    public Float getShadowAlpha() { return shadowAlpha; }
    /** @return the hover action type */
    public String getHoverAction() { return hoverAction; }
    /** @return the hover action value */
    public String getHoverValue() { return hoverValue; }
    /** @return the click action type */
    public String getClickAction() { return clickAction; }
    /** @return the click action value */
    public String getClickValue() { return clickValue; }

    /**
     * Checks if this style is equal to another object.
     * @param o the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Style style = (Style) o;
        return bold == style.bold &&
                italic == style.italic &&
                underlined == style.underlined &&
                strikethrough == style.strikethrough &&
                censored == style.censored &&
                Objects.equals(rgb, style.rgb) &&
                Objects.equals(argb, style.argb) &&
                Objects.equals(insertText, style.insertText) &&
                Objects.equals(shadowColor, style.shadowColor) &&
                Objects.equals(shadowAlpha, style.shadowAlpha) &&
                Objects.equals(hoverAction, style.hoverAction) &&
                Objects.equals(hoverValue, style.hoverValue) &&
                Objects.equals(clickAction, style.clickAction) &&
                Objects.equals(clickValue, style.clickValue);
    }

    /**
     * Returns the hash code for this style.
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(rgb, argb, bold, italic, underlined, strikethrough, censored, insertText, shadowColor, shadowAlpha, hoverAction, hoverValue, clickAction, clickValue);
    }

    /**
     * Returns a string representation of this style.
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Style{" +
                "rgb='" + rgb + '\'' +
                ", argb='" + argb + '\'' +
                ", bold=" + bold +
                ", italic=" + italic +
                ", underlined=" + underlined +
                ", strikethrough=" + strikethrough +
                ", censored=" + censored +
                ", insertText='" + insertText + '\'' +
                ", shadowColor='" + shadowColor + '\'' +
                ", shadowAlpha=" + shadowAlpha +
                ", hoverAction='" + hoverAction + '\'' +
                ", hoverValue='" + hoverValue + '\'' +
                ", clickAction='" + clickAction + '\'' +
                ", clickValue='" + clickValue + '\'' +
                '}';
    }
}
