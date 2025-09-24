package org.leycm.lang.adapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leycm.lang.text.Text;
import org.leycm.lang.text.TextAdapter;
import org.leycm.lang.text.TextComponent;
import org.leycm.lang.text.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Standard text adapters: Console (ASCII/ANSI) and HTML.
 */
public interface BasicTextAdapter {

    /**
     * Adapter to convert {@link Text} to ANSI-colored strings for console output.
     */
    TextAdapter<String> ASCI = new TextAdapter<>() {

        private static final String RESET = "\u001B[0m";
        private static final String BOLD = "\u001B[1m";
        private static final String ITALIC = "\u001B[3m";
        private static final String UNDERLINE = "\u001B[4m";
        private static final String STRIKETHROUGH = "\u001B[9m";
        private static final String MAGIC = "\u001B[8m";

        private static final Pattern RGB_PATTERN = Pattern.compile("\u001B\\[38;2;(\\d+);(\\d+);(\\d+)m");

        @Override
        public @NotNull String to(@NotNull Text to) {
            StringBuilder sb = new StringBuilder();
            for (TextComponent part : to.components()) {
                TextStyle style = part.style();
                sb.append(applyStyle(style));
                sb.append(part.content());
                sb.append(RESET);
            }
            return sb.toString();
        }

        @Override
        public @Nullable Text from(String object) {
            if (object == null || object.isEmpty()) return null;

            List<TextComponent> components = new ArrayList<>();
            String[] parts = object.split(Pattern.quote(RESET));
            for (String part : parts) {
                if (part.isEmpty()) continue;
                TextStyle style = getStyle(part);

                String content = part.replaceAll("\\u001B\\[[\\d;]+m", "");
                components.add(new TextComponent(content, style));
            }

            return Text.of(components);
        }

        private @NotNull TextStyle getStyle(@NotNull String part) {
            TextStyle style = new TextStyle();

            if (part.contains(BOLD)) style.bold();
            if (part.contains(ITALIC)) style.italic();
            if (part.contains(UNDERLINE)) style.underlined();
            if (part.contains(STRIKETHROUGH)) style.strikethrough();
            if (part.contains(MAGIC)) style.censored();

            Matcher m = RGB_PATTERN.matcher(part);
            if (m.find()) {
                int r = Integer.parseInt(m.group(1));
                int g = Integer.parseInt(m.group(2));
                int b = Integer.parseInt(m.group(3));
                style.rgb(String.format("%02X%02X%02X", r, g, b));
            }
            return style;
        }


        @Contract(pure = true)
        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Contract(pure = true)
        @Override
        public Class<String> getFrom() {
            return String.class;
        }

        private @NotNull TextStyle getTextStyle(@NotNull String part) {
            return getStyle(part);
        }

        private static @NotNull String applyStyle(@NotNull TextStyle style) {
            StringBuilder sb = new StringBuilder();

            if (style.isBold()) sb.append(BOLD);
            if (style.isItalic()) sb.append(ITALIC);
            if (style.isUnderlined()) sb.append(UNDERLINE);
            if (style.isStrikethrough()) sb.append(STRIKETHROUGH);
            if (style.isCensored()) sb.append(MAGIC);

            if (style.getRgb() != null) {
                String rgb = style.getRgb();
                int r = Integer.parseInt(rgb.substring(0, 2), 16);
                int g = Integer.parseInt(rgb.substring(2, 4), 16);
                int b = Integer.parseInt(rgb.substring(4, 6), 16);
                sb.append(String.format("\u001B[38;2;%d;%d;%dm", r, g, b));
            }

            return sb.toString();
        }
    };

    /**
     * Adapter to convert {@link Text} to HTML.
     */
    TextAdapter<String> HTML = new TextAdapter<>() {

        @Override
        public @NotNull String to(@NotNull Text to) {
            StringBuilder sb = new StringBuilder();
            for (TextComponent part : to.components()) {
                TextStyle style = part.style();
                sb.append("<span style='");
                if (style.getRgb() != null) sb.append("color:#").append(style.getRgb()).append(";");
                if (style.isBold()) sb.append("font-weight:bold;");
                if (style.isItalic()) sb.append("font-style:italic;");
                if (style.isUnderlined()) sb.append("text-decoration:underline;");
                if (style.isStrikethrough()) sb.append("text-decoration:line-through;");
                if (style.isReplacement()) sb.append("replacement:").append(style.getReplacementKey());
                sb.append("'>");
                sb.append(part.content());
                sb.append("</span>");
            }
            return sb.toString();
        }

        @Contract(pure = true)
        @Override
        public @Nullable Text from(String object) {
            if (object == null || object.isEmpty()) return null;

            List<TextComponent> components = new ArrayList<>();
            Pattern spanPattern = Pattern.compile("<span style='([^']*)'>(.*?)</span>");
            Matcher matcher = spanPattern.matcher(object);
            while (matcher.find()) {
                String styleStr = matcher.group(1);
                String content = matcher.group(2);
                TextStyle style = new TextStyle();

                if (styleStr.contains("font-weight:bold")) style.bold();
                if (styleStr.contains("font-style:italic")) style.italic();
                if (styleStr.contains("text-decoration:underline")) style.underlined();
                if (styleStr.contains("text-decoration:line-through")) style.strikethrough();

                Matcher colorMatcher = Pattern.compile("color:#([0-9A-Fa-f]{6})").matcher(styleStr);
                if (colorMatcher.find()) {
                    style.rgb(colorMatcher.group(1).toUpperCase());
                }

                Matcher replacementMatcher = Pattern.compile("replacement:([^;]+)").matcher(styleStr);
                if (replacementMatcher.find()) {;
                    style.replacement(replacementMatcher.group(1));
                }

                components.add(new TextComponent(content, style));
            }

            return Text.of(components);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Contract(pure = true)
        @Override
        public Class<String> getFrom() {
            return String.class;
        }
    };
}
