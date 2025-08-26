package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Style {
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

    public Style() {
    }

    public Style rgb(String rgb) {
        if (rgb != null && !rgb.isEmpty()) {
            this.rgb = rgb.toUpperCase();
            this.argb = "FF" + this.rgb;
        }
        return this;
    }

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

    public Style bold(boolean value) {
        this.bold = value;
        return this;
    }

    public Style italic(boolean value) {
        this.italic = value;
        return this;
    }

    public Style underlined(boolean value) {
        this.underlined = value;
        return this;
    }

    public Style strikethrough(boolean value) {
        this.strikethrough = value;
        return this;
    }

    public Style censored(boolean value) {
        this.censored = value;
        return this;
    }

    public Style insertText(String insertText) {
        this.insertText = insertText;
        return this;
    }

    public Style shadowColor(String shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public Style shadowAlpha(Float shadowAlpha) {
        this.shadowAlpha = shadowAlpha;
        return this;
    }

    public Style hoverAction(String action, String value) {
        this.hoverAction = action;
        this.hoverValue = value;
        return this;
    }

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
    public String getRgb() { return rgb; }
    public String getArgb() { return argb; }
    public boolean isBold() { return bold; }
    public boolean isItalic() { return italic; }
    public boolean isUnderlined() { return underlined; }
    public boolean isStrikethrough() { return strikethrough; }
    public boolean isCensored() { return censored; }
    public String getInsertText() { return insertText; }
    public String getShadowColor() { return shadowColor; }
    public Float getShadowAlpha() { return shadowAlpha; }
    public String getHoverAction() { return hoverAction; }
    public String getHoverValue() { return hoverValue; }
    public String getClickAction() { return clickAction; }
    public String getClickValue() { return clickValue; }

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

    @Override
    public int hashCode() {
        return Objects.hash(rgb, argb, bold, italic, underlined, strikethrough, censored, insertText, shadowColor, shadowAlpha, hoverAction, hoverValue, clickAction, clickValue);
    }

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
