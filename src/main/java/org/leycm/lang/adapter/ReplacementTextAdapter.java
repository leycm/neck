package org.leycm.lang.adapter;

import org.leycm.lang.text.Text;
import org.leycm.lang.text.TextAdapter;
import org.leycm.lang.text.TextComponent;
import org.leycm.lang.text.TextStyle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ReplacementTextAdapter {

    TextAdapter<Text> REPLACE = new TextAdapter<>() {

        private static final Pattern PERCENT_SIMPLE = Pattern.compile("%[a-zA-Z] ");
        private static final Pattern PERCENT_COMPLEX = Pattern.compile("%[a-zA-Z]+%");
        private static final Pattern DOLLAR = Pattern.compile("\\$\\{([^}]+)}");

        @Override
        public @NotNull Text from(@NotNull Text input) {
            List<TextComponent> resultComponents = new ArrayList<>();
            int counter = 0;

            for (TextComponent comp : input.components()) {
                String text = comp.content();
                int lastIndex = 0;

                List<Matcher> matchers = List.of(
                        PERCENT_SIMPLE.matcher(text),
                        PERCENT_COMPLEX.matcher(text),
                        DOLLAR.matcher(text)
                );

                List<MatchData> matches = new ArrayList<>();
                for (Matcher m : matchers) {
                    while (m.find()) {
                        matches.add(new MatchData(m.start(), m.end(), m.group(), m.pattern()));
                    }
                }

                matches.sort(Comparator.comparingInt(md -> md.start));

                for (MatchData match : matches) {
                    if (match.start > lastIndex) {
                        resultComponents.add(new TextComponent(
                                text.substring(lastIndex, match.start),
                                comp.style().copy()
                        ));
                    }

                    String replacementKey;
                    if (match.pattern.pattern().equals(PERCENT_SIMPLE.pattern())) {
                        replacementKey = "string." + counter;
                        counter++;
                    } else if (match.pattern.pattern().equals(PERCENT_COMPLEX.pattern())) {
                        replacementKey = match.group.substring(1, match.group.length() - 1);
                    } else {
                        replacementKey = match.group.substring(2, match.group.length() - 1);
                    }

                    TextStyle replacementStyle = comp.style().copy().replacement(replacementKey);
                    resultComponents.add(new TextComponent(match.group, replacementStyle));

                    lastIndex = match.end;
                }

                if (lastIndex < text.length()) {
                    resultComponents.add(new TextComponent(
                            text.substring(lastIndex),
                            comp.style().copy()
                    ));
                }
            }

            return new Text(resultComponents);
        }

        @Contract("_ -> new")
        @Override
        public @NotNull Text to(@NotNull Text from) {
            return from(from);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Override
        public Class<Text> getFrom() {
            return Text.class;
        }

        private record MatchData(int start, int end, String group, Pattern pattern) {}
    };
}
