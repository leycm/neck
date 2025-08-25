package org.leycm.neck.lang.adapters;

import org.jetbrains.annotations.NotNull;
import org.leycm.neck.lang.Text;
import org.leycm.neck.lang.TextProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minecraft-related text adapters (MiniMessage, Legacy, etc.).
 */
public interface MinecraftTextAdapters {

    /**
     * Mapping of standard Minecraft color names to their hex codes.
     */
    Map<String, String> NAMED_COLORS = new HashMap<>() {{
        put("black", "000000");
        put("dark_blue", "0000AA");
        put("dark_green", "00AA00");
        put("dark_aqua", "00AAAA");
        put("dark_red", "AA0000");
        put("dark_purple", "AA00AA");
        put("gold", "FFAA00");
        put("gray", "AAAAAA");
        put("dark_gray", "555555");
        put("blue", "5555FF");
        put("green", "55FF55");
        put("aqua", "55FFFF");
        put("red", "FF5555");
        put("light_purple", "FF55FF");
        put("yellow", "FFFF55");
        put("white", "FFFFFF");
    }};

    /**
     * Adapter to convert {@link Text} to and from MiniMessage format.
     */
    Text.Adapter<String> MINI_MESSAGE = new Text.Adapter<>() {

        @Override
        public @NotNull String to(@NotNull Text text) {
            StringBuilder sb = new StringBuilder();
            for (Text.Part part : text.getParts()) {
                Text.Style s = part.getStyles();
                if (s != null) {
                    sb.append(applyStyleToMiniMessage(s));
                }
                sb.append(part.toPlainString());
                if (s != null) {
                    sb.append(closeStyleMiniMessage(s));
                }
            }
            return sb.toString();
        }

        @Override
        public @NotNull Text from(String input) {
            Text text = new Text(TextProvider.getDefault());
            Pattern pattern = Pattern.compile("<(/?)(#[0-9a-fA-F]{6}|[a-zA-Z_]+)>");
            Matcher matcher = pattern.matcher(input);

            int lastIndex = 0;
            Text.Style current = new Text.Style();

            while (matcher.find()) {
                if (matcher.start() > lastIndex)
                    text.append(input.substring(lastIndex, matcher.start()), current);

                String tag = matcher.group(2);
                boolean closing = matcher.group(1).equals("/");

                if (!closing) {
                    current = applyMiniMessageTag(current, tag);
                } else {
                    current = removeMiniMessageTag(current, tag);
                }

                lastIndex = matcher.end();
            }

            if (lastIndex < input.length()) {
                text.append(input.substring(lastIndex), current);
            }

            return text;
        }

        /**
         * Applies a style to MiniMessage opening tags.
         */
        private @NotNull String applyStyleToMiniMessage(Text.@NotNull Style style) {
            StringBuilder sb = new StringBuilder();
            if (style.getRgb() != null) sb.append("<#").append(style.getRgb()).append(">");
            if (style.isBold()) sb.append("<bold>");
            if (style.isItalic()) sb.append("<italic>");
            if (style.isUnderlined()) sb.append("<strikethrough>");
            if (style.isCensored()) sb.append("<magic>");
            return sb.toString();
        }

        /**
         * Closes MiniMessage style tags in reverse order.
         */
        private @NotNull String closeStyleMiniMessage(Text.@NotNull Style style) {
            StringBuilder sb = new StringBuilder();
            if (style.isCensored()) sb.append("</magic>");
            if (style.isUnderlined()) sb.append("</strikethrough>");
            if (style.isItalic()) sb.append("</italic>");
            if (style.isBold()) sb.append("</bold>");
            if (style.getRgb() != null) sb.append("</#").append(style.getRgb()).append(">");
            return sb.toString();
        }

        /**
         * Converts a MiniMessage tag into a {@link Text.Style}.
         */
        private Text.Style applyMiniMessageTag(Text.Style current, @NotNull String tag) {
            String lower = tag.toLowerCase();
            switch (lower) {
                case "bold" -> current.bold(true);
                case "italic" -> current.italic(true);
                case "strikethrough" -> current.underlined(true);
                case "magic" -> current.censored(true);
                default -> {
                    String color = NAMED_COLORS.getOrDefault(lower, null);
                    if (color != null) current = new Text.Style().rgb(color);
                    else if (tag.startsWith("#")) current = new Text.Style().rgb(tag.substring(1));
                }
            }
            return current;
        }

        /**
         * Removes/turns off a MiniMessage tag from a style.
         */
        private Text.Style removeMiniMessageTag(Text.Style current, @NotNull String tag) {
            return switch (tag.toLowerCase()) {
                case "bold" -> current.bold(false);
                case "italic" -> current.italic(false);
                case "strikethrough" -> current.underlined(false);
                case "magic" -> current.censored(false);
                default -> new Text.Style(); // Reset unknown tag
            };
        }
    };

    /**
     * Adapter to convert {@link Text} to and from legacy Minecraft color codes.
     */
    Text.Adapter<String> LEGACY = new Text.Adapter<>() {

        @Override
        public @NotNull String to(@NotNull Text text) {
            StringBuilder sb = new StringBuilder();
            for (Text.Part part : text.getParts()) {
                Text.Style s = part.getStyles();
                if (s != null) {
                    if (s.isBold()) sb.append("&l");
                    if (s.isItalic()) sb.append("&o");
                    if (s.isUnderlined()) sb.append("&n");
                    if (s.isCensored()) sb.append("&k");
                    if (s.getRgb() != null) sb.append("&#").append(s.getRgb());
                }
                sb.append(part.toPlainString());
            }
            return sb.toString();
        }

        @Override
        public @NotNull Text from(String input) {
            Text text = new Text(TextProvider.getDefault());
            Pattern pattern = Pattern.compile("&(#?[0-9a-fA-F]{1,6}|[0-9a-fklmnor])");
            Matcher matcher = pattern.matcher(input);

            int lastIndex = 0;
            Text.Style current = new Text.Style();

            while (matcher.find()) {
                if (matcher.start() > lastIndex)
                    text.append(input.substring(lastIndex, matcher.start()), current);

                String code = matcher.group(1);
                current = applyLegacyCode(current, code);

                lastIndex = matcher.end();
            }

            if (lastIndex < input.length()) text.append(input.substring(lastIndex), current);

            return text;
        }

        /**
         * Applies a legacy color or formatting code to a style.
         */
        private Text.Style applyLegacyCode(Text.Style style, @NotNull String code) {
            return switch (code.toLowerCase()) {
                case "l" -> style.bold(true);
                case "o" -> style.italic(true);
                case "n" -> style.underlined(true);
                case "m" -> style.strikethrough(true);
                case "k" -> style.censored(true);
                default -> {
                    if (code.startsWith("#")) yield new Text.Style().rgb(code.substring(1));
                    else yield mapLegacyColorCode(style, code.charAt(0));
                }
            };
        }

        /**
         * Maps single-character legacy color codes to hex colors.
         */
        private Text.Style mapLegacyColorCode(Text.Style style, char code) {
            return switch (code) {
                case '0' -> new Text.Style().rgb("000000");
                case '1' -> new Text.Style().rgb("0000AA");
                case '2' -> new Text.Style().rgb("00AA00");
                case '3' -> new Text.Style().rgb("00AAAA");
                case '4' -> new Text.Style().rgb("AA0000");
                case '5' -> new Text.Style().rgb("AA00AA");
                case '6' -> new Text.Style().rgb("FFAA00");
                case '7' -> new Text.Style().rgb("AAAAAA");
                case '8' -> new Text.Style().rgb("555555");
                case '9' -> new Text.Style().rgb("5555FF");
                case 'a' -> new Text.Style().rgb("55FF55");
                case 'b' -> new Text.Style().rgb("55FFFF");
                case 'c' -> new Text.Style().rgb("FF5555");
                case 'd' -> new Text.Style().rgb("FF55FF");
                case 'e' -> new Text.Style().rgb("FFFF55");
                case 'f' -> new Text.Style().rgb("FFFFFF");
                default -> style;
            };
        }
    };
}
