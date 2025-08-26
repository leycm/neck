package org.leycm.neck.lang.adapters;

import org.jetbrains.annotations.NotNull;
import org.leycm.neck.lang.Text;
import org.leycm.neck.lang.TextProvider;
import org.leycm.neck.lang.Style;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Enhanced Minecraft text adapters with full MiniMessage support including
 * rainbow, gradient, transition, font, insertion, shadow, hover, and click features.
 */
public interface MinecraftTextAdapters {

    Map<String, String> NAME_TO_HEX = new HashMap<>() {{
        put("black", "000000");
        put("dark_blue", "0000AA");
        put("dark_green", "00AA00");
        put("dark_aqua", "00AAAA");
        put("dark_red", "AA0000");
        put("dark_purple", "AA00AA");
        put("gold", "FFAA00");
        put("gray", "AAAAAA");
        put("grey", "AAAAAA");
        put("dark_gray", "555555");
        put("dark_grey", "555555");
        put("blue", "5555FF");
        put("green", "55FF55");
        put("aqua", "55FFFF");
        put("red", "FF5555");
        put("light_purple", "FF55FF");
        put("yellow", "FFFF55");
        put("white", "FFFFFF");
    }};

    Map<String, String> HEX_TO_NAME = mirror(NAME_TO_HEX);

    Map<String, String> CODE_TO_HEX = new HashMap<>() {{
        put("0", "000000");
        put("1", "0000AA");
        put("2", "00AA00");
        put("3", "00AAAA");
        put("4", "AA0000");
        put("5", "AA00AA");
        put("6", "FFAA00");
        put("7", "AAAAAA");
        put("8", "555555");
        put("9", "5555FF");
        put("a", "55FF55");
        put("b", "55FFFF");
        put("c", "FF5555");
        put("d", "FF55FF");
        put("e", "FFFF55");
        put("f", "FFFFFF");
    }};

    Map<String, String> HEX_TO_CODE = mirror(CODE_TO_HEX);

    /**
     * Enhanced MiniMessage adapter with full feature support
     */
    Text.Adapter<String> MINI_MESSAGE = new Text.Adapter<>() {

        private static final Pattern MINI_MESSAGE_PATTERN = Pattern.compile(
                "<(/?)(#[0-9a-fA-F]{6}|[a-zA-Z_][a-zA-Z0-9_]*(?::[^>]*)?|!)>"
        );

        @Override
        public @NotNull String to(@NotNull Text text) {
            StringBuilder result = new StringBuilder();

            for (Text.Part part : text.getParts()) {
                Style style = part.getStyles();
                if (style != null) {
                    result.append(buildOpeningTags(style));
                }
                result.append(part.toPlainString());
                if (style != null) {
                    result.append(buildClosingTags(style));
                }
            }

            return result.toString();
        }

        @Override
        public @NotNull Text from(@NotNull String input) {
            Text text = new Text(TextProvider.getDefault());

            // Handle special cases first: rainbow, gradient, transition
            input = processSpecialTags(input, text);

            Matcher matcher = MINI_MESSAGE_PATTERN.matcher(input);
            int lastIndex = 0;
            Stack<Style> styleStack = new Stack<>();
            Style currentStyle = new Style();

            while (matcher.find()) {
                if (matcher.start() > lastIndex) {
                    String textPart = input.substring(lastIndex, matcher.start());
                    if (!textPart.isEmpty()) {
                        text.append(textPart, currentStyle.copy());
                    }
                }

                String tag = matcher.group(2);
                boolean isClosingTag = "/".equals(matcher.group(1));

                if (isClosingTag) {
                    if (!styleStack.isEmpty()) {
                        styleStack.pop();
                        currentStyle = styleStack.isEmpty() ? new Style() : styleStack.peek().copy();
                    }
                } else {
                    if (isResetTag(tag)) {
                        styleStack.clear();
                        currentStyle = new Style();
                    } else if (isNewlineTag(tag)) {
                        text.append("\n", currentStyle.copy());
                    } else {
                        Style newStyle = applyStyleFromTag(currentStyle, tag);
                        styleStack.push(newStyle);
                        currentStyle = newStyle;
                    }
                }

                lastIndex = matcher.end();
            }

            if (lastIndex < input.length()) {
                String remaining = input.substring(lastIndex);
                if (!remaining.isEmpty()) {
                    text.append(remaining, currentStyle);
                }
            }

            return text;
        }

        private String processSpecialTags(String input, Text text) {
            // Process rainbow tags
            input = processRainbowTags(input, text);

            // Process gradient tags
            input = processGradientTags(input, text);

            // Process transition tags
            input = processTransitionTags(input, text);

            return input;
        }

        private String processRainbowTags(String input, Text text) {
            Pattern rainbowPattern = Pattern.compile("<rainbow(?::([^>]*))?>([^<]*)</rainbow>");
            Matcher matcher = rainbowPattern.matcher(input);
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                String params = matcher.group(1);
                String content = matcher.group(2);

                double phase = 0.0;
                boolean reverse = false;

                if (params != null) {
                    String[] parts = params.split(":");
                    for (String part : parts) {
                        if ("!".equals(part.trim())) {
                            reverse = true;
                        } else {
                            try {
                                phase = Double.parseDouble(part.trim());
                            } catch (NumberFormatException ignored) {}
                        }
                    }
                }

                String rainbowText = applyRainbow(content, phase, reverse);
                matcher.appendReplacement(result, rainbowText);
            }

            matcher.appendTail(result);
            return result.toString();
        }

        private String processGradientTags(String input, Text text) {
            Pattern gradientPattern = Pattern.compile("<gradient(?::([^>]*))?>([^<]*)</gradient>");
            Matcher matcher = gradientPattern.matcher(input);
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                String params = matcher.group(1);
                String content = matcher.group(2);

                List<String> colors = new ArrayList<>();
                double phase = 0.0;

                if (params != null) {
                    String[] parts = params.split(":");
                    for (String part : parts) {
                        part = part.trim();
                        try {
                            phase = Double.parseDouble(part);
                        } catch (NumberFormatException e) {
                            if (part.startsWith("#") || NAME_TO_HEX.containsKey(part.toLowerCase())) {
                                colors.add(part);
                            }
                        }
                    }
                }

                if (colors.isEmpty()) {
                    colors.add("red");
                    colors.add("blue");
                }

                String gradientText = applyGradient(content, colors, phase);
                matcher.appendReplacement(result, gradientText);
            }

            matcher.appendTail(result);
            return result.toString();
        }

        private String processTransitionTags(String input, Text text) {
            Pattern transitionPattern = Pattern.compile("<transition(?::([^>]*))?>([^<]*)</transition>");
            Matcher matcher = transitionPattern.matcher(input);
            StringBuffer result = new StringBuffer();

            while (matcher.find()) {
                String params = matcher.group(1);
                String content = matcher.group(2);

                List<String> colors = new ArrayList<>();
                double phase = 0.0;

                if (params != null) {
                    String[] parts = params.split(":");
                    for (String part : parts) {
                        part = part.trim();
                        try {
                            phase = Double.parseDouble(part);
                        } catch (NumberFormatException e) {
                            if (part.startsWith("#") || NAME_TO_HEX.containsKey(part.toLowerCase())) {
                                colors.add(part);
                            }
                        }
                    }
                }

                if (colors.isEmpty()) {
                    colors.add("red");
                    colors.add("blue");
                }

                String transitionText = applyTransition(content, colors, phase);
                matcher.appendReplacement(result, transitionText);
            }

            matcher.appendTail(result);
            return result.toString();
        }

        private String applyRainbow(String text, double phase, boolean reverse) {
            StringBuilder result = new StringBuilder();
            double hueStep = 360.0 / text.length();

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c != ' ') {
                    double hue = (reverse ? 360 - (i * hueStep) : i * hueStep) + (phase * 360);
                    hue = hue % 360;
                    String hexColor = hsvToHex((float) hue, 1.0f, 1.0f);
                    result.append("<#").append(hexColor).append(">").append(c).append("</#").append(hexColor).append(">");
                } else {
                    result.append(c);
                }
            }

            return result.toString();
        }

        private String applyGradient(String text, List<String> colors, double phase) {
            if (text.isEmpty()) return text;

            StringBuilder result = new StringBuilder();
            int textLength = text.replaceAll(" ", "").length();

            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (c != ' ') {
                    double position = (double) i / Math.max(1, textLength - 1);
                    position = (position + phase) % 1.0;
                    if (position < 0) position += 1.0;

                    String hexColor = interpolateColors(colors, position);
                    result.append("<#").append(hexColor).append(">").append(c).append("</#").append(hexColor).append(">");
                } else {
                    result.append(c);
                }
            }

            return result.toString();
        }

        private String applyTransition(String text, List<String> colors, double phase) {
            if (text.isEmpty() || colors.isEmpty()) return text;

            phase = phase % 1.0;
            if (phase < 0) phase += 1.0;

            String hexColor = interpolateColors(colors, phase);
            return "<#" + hexColor + ">" + text + "</#" + hexColor + ">";
        }

        private String interpolateColors(List<String> colors, double position) {
            if (colors.size() == 1) {
                String color = colors.get(0);
                return color.startsWith("#") ? color.substring(1) : NAME_TO_HEX.getOrDefault(color.toLowerCase(), "FFFFFF");
            }

            double scaledPosition = position * (colors.size() - 1);
            int index = (int) Math.floor(scaledPosition);
            double t = scaledPosition - index;

            if (index >= colors.size() - 1) {
                String color = colors.get(colors.size() - 1);
                return color.startsWith("#") ? color.substring(1) : NAME_TO_HEX.getOrDefault(color.toLowerCase(), "FFFFFF");
            }

            String color1 = colors.get(index);
            String color2 = colors.get(index + 1);

            String hex1 = color1.startsWith("#") ? color1.substring(1) : NAME_TO_HEX.getOrDefault(color1.toLowerCase(), "FFFFFF");
            String hex2 = color2.startsWith("#") ? color2.substring(1) : NAME_TO_HEX.getOrDefault(color2.toLowerCase(), "FFFFFF");

            return interpolateHexColors(hex1, hex2, t);
        }

        private String interpolateHexColors(String hex1, String hex2, double t) {
            int r1 = Integer.parseInt(hex1.substring(0, 2), 16);
            int g1 = Integer.parseInt(hex1.substring(2, 4), 16);
            int b1 = Integer.parseInt(hex1.substring(4, 6), 16);

            int r2 = Integer.parseInt(hex2.substring(0, 2), 16);
            int g2 = Integer.parseInt(hex2.substring(2, 4), 16);
            int b2 = Integer.parseInt(hex2.substring(4, 6), 16);

            int r = (int) (r1 + t * (r2 - r1));
            int g = (int) (g1 + t * (g2 - g1));
            int b = (int) (b1 + t * (b2 - b1));

            return String.format("%02X%02X%02X", r, g, b);
        }

        private String hsvToHex(float hue, float saturation, float value) {
            int rgb = java.awt.Color.HSBtoRGB(hue / 360f, saturation, value);
            return String.format("%06X", rgb & 0xFFFFFF);
        }

        private boolean isResetTag(String tag) {
            return "reset".equalsIgnoreCase(tag);
        }

        private boolean isNewlineTag(String tag) {
            return "newline".equalsIgnoreCase(tag) || "br".equalsIgnoreCase(tag);
        }

        private @NotNull String buildOpeningTags(@NotNull Style style) {
            StringBuilder tags = new StringBuilder();

            // Color
            if (style.getRgb() != null) {
                String colorName = HEX_TO_NAME.get(style.getRgb().toUpperCase());
                if (colorName != null) {
                    tags.append("<").append(colorName).append(">");
                } else {
                    tags.append("<#").append(style.getRgb()).append(">");
                }
            }

            // Shadow
            if (style.getShadowColor() != null) {
                tags.append("<shadow:").append(style.getShadowColor());
                if (style.getShadowAlpha() != null) {
                    tags.append(":").append(style.getShadowAlpha());
                }
                tags.append(">");
            }

            // Decorations
            if (style.isBold()) tags.append("<bold>");
            if (style.isItalic()) tags.append("<italic>");
            if (style.isUnderlined()) tags.append("<underlined>");
            if (style.isStrikethrough()) tags.append("<strikethrough>");
            if (style.isCensored()) tags.append("<obfuscated>");

            // Interactive elements
            if (style.getInsertText() != null) {
                tags.append("<insertion:").append(style.getInsertText()).append(">");
            }
            if (style.getHoverAction() != null && style.getHoverValue() != null) {
                tags.append("<hover:").append(style.getHoverAction()).append(":").append(style.getHoverValue()).append(">");
            }
            if (style.getClickAction() != null && style.getClickValue() != null) {
                tags.append("<click:").append(style.getClickAction()).append(":").append(style.getClickValue()).append(">");
            }

            return tags.toString();
        }

        private @NotNull String buildClosingTags(@NotNull Style style) {
            StringBuilder tags = new StringBuilder();

            // Close in reverse order
            if (style.getClickAction() != null && style.getClickValue() != null) {
                tags.append("</click>");
            }
            if (style.getHoverAction() != null && style.getHoverValue() != null) {
                tags.append("</hover>");
            }
            if (style.getInsertText() != null) {
                tags.append("</insertion>");
            }

            if (style.isCensored()) tags.append("</obfuscated>");
            if (style.isStrikethrough()) tags.append("</strikethrough>");
            if (style.isUnderlined()) tags.append("</underlined>");
            if (style.isItalic()) tags.append("</italic>");
            if (style.isBold()) tags.append("</bold>");

            if (style.getShadowColor() != null) {
                tags.append("</shadow>");
            }

            if (style.getRgb() != null) {
                String colorName = HEX_TO_NAME.get(style.getRgb().toUpperCase());
                if (colorName != null) {
                    tags.append("</").append(colorName).append(">");
                } else {
                    tags.append("</#").append(style.getRgb()).append(">");
                }
            }

            return tags.toString();
        }

        private @NotNull Style applyStyleFromTag(@NotNull Style currentStyle, @NotNull String tag) {
            String lowerTag = tag.toLowerCase();

            if (lowerTag.contains(":")) {
                String[] parts = tag.split(":", 2);
                String tagName = parts[0].toLowerCase();
                String params = parts.length > 1 ? parts[1] : "";

                return switch (tagName) {
                    case "color", "colour", "c" -> {
                        if (params.startsWith("#")) {
                            yield currentStyle.copy().rgb(params.substring(1));
                        } else {
                            String hexColor = NAME_TO_HEX.get(params.toLowerCase());
                            yield hexColor != null ? currentStyle.copy().rgb(hexColor) : currentStyle;
                        }
                    }

                    case "insertion" -> currentStyle.copy().insertText(params);
                    case "shadow" -> {
                        String[] shadowParts = params.split(":");
                        Style newStyle = currentStyle.copy().shadowColor(shadowParts[0]);
                        if (shadowParts.length > 1) {
                            try {
                                newStyle.shadowAlpha(Float.parseFloat(shadowParts[1]));
                            } catch (NumberFormatException ignored) {}
                        }
                        yield newStyle;
                    }
                    case "hover" -> {
                        String[] hoverParts = params.split(":", 2);
                        yield hoverParts.length > 1 ?
                                currentStyle.copy().hoverAction(hoverParts[0], hoverParts[1]) :
                                currentStyle;
                    }
                    case "click" -> {
                        String[] clickParts = params.split(":", 2);
                        yield clickParts.length > 1 ?
                                currentStyle.copy().clickAction(clickParts[0], clickParts[1]) :
                                currentStyle;
                    }
                    case "key" -> currentStyle;
                    default -> currentStyle;
                };
            }

            return switch (lowerTag) {
                case "bold", "b" -> currentStyle.copy().bold(true);
                case "italic", "i", "em" -> currentStyle.copy().italic(true);
                case "underlined", "u" -> currentStyle.copy().underlined(true);
                case "strikethrough", "st" -> currentStyle.copy().strikethrough(true);
                case "obfuscated", "obf" -> currentStyle.copy().censored(true);
                case "!shadow" -> currentStyle.copy().shadowColor("#00000000");
                default -> {
                    if (tag.startsWith("#")) {
                        yield currentStyle.copy().rgb(tag.substring(1));
                    }
                    String hexColor = NAME_TO_HEX.get(lowerTag);
                    if (hexColor != null) {
                        yield currentStyle.copy().rgb(hexColor);
                    }
                    yield currentStyle;
                }
            };
        }
    };

    /**
     * Enhanced Legacy adapter with support for hex colors
     */
    Text.Adapter<String> LEGACY = new Text.Adapter<>() {

        private static final Pattern LEGACY_PATTERN = Pattern.compile("&(#?[0-9a-fA-F]{1,6}|[0-9a-fklmnor])");

        @Override
        public @NotNull String to(@NotNull Text text) {
            StringBuilder result = new StringBuilder();

            for (Text.Part part : text.getParts()) {
                Style style = part.getStyles();
                if (style != null) {
                    result.append(buildLegacyCodes(style));
                }
                result.append(part.toPlainString());
            }

            return result.toString();
        }

        @Override
        public @NotNull Text from(@NotNull String input) {
            Text text = new Text(TextProvider.getDefault());
            Matcher matcher = LEGACY_PATTERN.matcher(input);

            int lastIndex = 0;
            Style currentStyle = new Style();

            while (matcher.find()) {
                if (matcher.start() > lastIndex) {
                    String textPart = input.substring(lastIndex, matcher.start());
                    if (!textPart.isEmpty()) {
                        text.append(textPart, currentStyle.copy());
                    }
                }

                String code = matcher.group(1);
                currentStyle = applyLegacyCode(currentStyle, code);

                lastIndex = matcher.end();
            }

            if (lastIndex < input.length()) {
                String remaining = input.substring(lastIndex);
                if (!remaining.isEmpty()) {
                    text.append(remaining, currentStyle);
                }
            }

            return text;
        }

        private @NotNull String buildLegacyCodes(@NotNull Style style) {
            StringBuilder codes = new StringBuilder();

            if (style.getRgb() != null) {
                String legacyCode = HEX_TO_CODE.get(style.getRgb().toUpperCase());
                if (legacyCode != null) {
                    codes.append("&").append(legacyCode);
                } else {
                    codes.append("&#").append(style.getRgb());
                }
            }

            if (style.isBold()) codes.append("&l");
            if (style.isItalic()) codes.append("&o");
            if (style.isUnderlined()) codes.append("&n");
            if (style.isStrikethrough()) codes.append("&m");
            if (style.isCensored()) codes.append("&k");

            return codes.toString();
        }

        private @NotNull Style applyLegacyCode(@NotNull Style currentStyle, @NotNull String code) {
            String lowerCode = code.toLowerCase();

            return switch (lowerCode) {
                case "l" -> currentStyle.copy().bold(true);
                case "o" -> currentStyle.copy().italic(true);
                case "n" -> currentStyle.copy().underlined(true);
                case "m" -> currentStyle.copy().strikethrough(true);
                case "k" -> currentStyle.copy().censored(true);
                case "r" -> new Style();
                default -> {
                    if (code.startsWith("#")) {
                        yield new Style().rgb(code.substring(1));
                    }
                    String hexColor = CODE_TO_HEX.get(lowerCode);
                    if (hexColor != null) {
                        yield new Style().rgb(hexColor);
                    }
                    yield currentStyle;
                }
            };
        }
    };

    /**
     * Utility method to create a reverse mapping of a given map
     */
    static <K, V> @NotNull Map<V, K> mirror(@NotNull Map<K, V> original) {
        Map<V, K> mirrored = new HashMap<>();
        for (Map.Entry<K, V> entry : original.entrySet()) {
            mirrored.put(entry.getValue(), entry.getKey());
        }
        return mirrored;
    }

    /**
     * Utility class for color operations
     */
    class ColorUtils {

        /**
         * Converts HSV color values to RGB hex string
         */
        public static String hsvToHex(float hue, float saturation, float value) {
            int rgb = java.awt.Color.HSBtoRGB(hue / 360f, saturation, value);
            return String.format("%06X", rgb & 0xFFFFFF);
        }

        /**
         * Interpolates between two hex colors
         */
        public static String interpolateColors(String hex1, String hex2, double t) {
            if (hex1.length() != 6 || hex2.length() != 6) {
                return hex1; // Return first color if invalid
            }

            try {
                int r1 = Integer.parseInt(hex1.substring(0, 2), 16);
                int g1 = Integer.parseInt(hex1.substring(2, 4), 16);
                int b1 = Integer.parseInt(hex1.substring(4, 6), 16);

                int r2 = Integer.parseInt(hex2.substring(0, 2), 16);
                int g2 = Integer.parseInt(hex2.substring(2, 4), 16);
                int b2 = Integer.parseInt(hex2.substring(4, 6), 16);

                int r = (int) Math.round(r1 + t * (r2 - r1));
                int g = (int) Math.round(g1 + t * (g2 - g1));
                int b = (int) Math.round(b1 + t * (b2 - b1));

                // Clamp values to 0-255 range
                r = Math.max(0, Math.min(255, r));
                g = Math.max(0, Math.min(255, g));
                b = Math.max(0, Math.min(255, b));

                return String.format("%02X%02X%02X", r, g, b);
            } catch (NumberFormatException e) {
                return hex1; // Return first color if parsing fails
            }
        }

        /**
         * Validates if a string is a valid hex color
         */
        public static boolean isValidHexColor(String hex) {
            if (hex == null || (hex.length() != 6 && hex.length() != 8)) {
                return false;
            }
            try {
                Long.parseLong(hex, 16);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }

        /**
         * Normalizes a color string to hex format
         */
        public static String normalizeColor(String color) {
            if (color == null || color.isEmpty()) {
                return "FFFFFF"; // Default to white
            }

            String trimmed = color.trim();

            // Handle hex colors
            if (trimmed.startsWith("#")) {
                String hex = trimmed.substring(1);
                return isValidHexColor(hex) ? hex.toUpperCase() : "FFFFFF";
            }

            // Handle named colors
            String hex = NAME_TO_HEX.get(trimmed.toLowerCase());
            return hex != null ? hex : "FFFFFF";
        }
    }
}