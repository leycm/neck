package org.leycm.neck.lang.adapters;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leycm.neck.lang.Text;
import org.leycm.neck.lang.Style;

/**
 * Generic text adapters, e.g., for console output using ASCII/ANSI colors.
 */
public interface GenericTextAdapter {

    /**
     * Adapter to convert {@link Text} to ANSI-colored strings for console output.
     */
    Text.Adapter<String> ASCI_CONSOL = new Text.Adapter<>() {

        private static final String RESET = "\u001B[0m";
        private static final String BOLD = "\u001B[1m";
        private static final String ITALIC = "\u001B[3m";
        private static final String UNDERLINE = "\u001B[4m";
        private static final String STRIKETHROUGH = "\u001B[9m";
        private static final String MAGIC = "\u001B[8m";

        @Override
        public @NotNull String to(@NotNull Text text) {
            StringBuilder sb = new StringBuilder();
            for (Text.Part part : text.getParts()) {
                Style style = part.getStyles();
                if (style != null) {
                    sb.append(applyStyle(style));
                }
                sb.append(part.toPlainString());
                if (style != null) sb.append(RESET);
            }
            return sb.toString();
        }

        @Contract(pure = true)
        @Override
        public @Nullable Text from(String object) {
            return null;
        }

        /**
         * Builds ANSI codes for a given style.
         */
        private static @NotNull String applyStyle(@NotNull Style style) {
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
}
