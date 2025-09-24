package org.leycm.lang.adapter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.leycm.lang.text.Text;
import org.leycm.lang.text.TextComponent;
import org.leycm.lang.text.TextStyle;
import org.leycm.lang.text.TextAdapter;

import java.util.List;
import java.util.Objects;

public interface AdventureTextAdapter {

    public static final TextAdapter<String> ANDLEGACY = new TextAdapter<>() {

        private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();

        @Override
        public @NotNull String to(@NotNull Text text) {
            Component component = COMPONENT.to(text);
            return legacySerializer.serialize(component);
        }

        @Override
        public @NotNull Text from(@NotNull String legacyText) {
            Component component = legacySerializer.deserialize(legacyText);
            return COMPONENT.from(component);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Override
        public Class<String> getFrom() {
            return String.class;
        }
    };

    public static final TextAdapter<String> LEGACY = new TextAdapter<>() {

        private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacySection();

        @Override
        public @NotNull String to(@NotNull Text text) {
            Component component = COMPONENT.to(text);
            return legacySerializer.serialize(component);
        }

        @Override
        public @NotNull Text from(@NotNull String legacyText) {
            Component component = legacySerializer.deserialize(legacyText);
            return COMPONENT.from(component);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Override
        public Class<String> getFrom() {
            return String.class;
        }
    };

    public static final TextAdapter<String> MINIMESSAGE = new TextAdapter<>() {

        private final MiniMessage miniMessage = MiniMessage.miniMessage();

        @Override
        public @NotNull String to(@NotNull Text text) {
            Component component = COMPONENT.to(text);
            return miniMessage.serialize(component);
        }

        @Override
        public @NotNull Text from(@NotNull String miniMessageText) {
            Component component = miniMessage.deserialize(miniMessageText);
            return COMPONENT.from(component);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Override
        public Class<String> getFrom() {
            return String.class;
        }
    };

    public static final TextAdapter<Component> COMPONENT = new TextAdapter<>() {

        @Override
        public @NotNull Component to(@NotNull Text text) {
            Component root = Component.empty();
            List<TextComponent> components = text.components();

            for (TextComponent part : components) {
                TextStyle style = part.style();
                Component comp = Component.text(part.content());

                if (style.getArgb() != null) {
                    comp = comp.color(TextColor.fromHexString("#" + style.getArgb().substring(2)));
                }

                if (style.isBold()) comp = comp.decorate(TextDecoration.BOLD);
                if (style.isItalic()) comp = comp.decorate(TextDecoration.ITALIC);
                if (style.isUnderlined()) comp = comp.decorate(TextDecoration.UNDERLINED);
                if (style.isStrikethrough()) comp = comp.decorate(TextDecoration.STRIKETHROUGH);
                if (style.isCensored()) comp = comp.decorate(TextDecoration.OBFUSCATED);

                if (style.getHoverAction() != null && style.getHoverValue() != null) {
                    comp = comp.hoverEvent(Component.text(style.getHoverValue()));
                }

                if (style.getClickAction() != null && style.getClickValue() != null) {
                    switch (style.getClickAction().toLowerCase()) {
                        case "open_url" -> comp = comp.clickEvent(net.kyori.adventure.text.event.ClickEvent.openUrl(style.getClickValue()));
                        case "run_command" -> comp = comp.clickEvent(net.kyori.adventure.text.event.ClickEvent.runCommand(style.getClickValue()));
                        case "suggest_command" -> comp = comp.clickEvent(net.kyori.adventure.text.event.ClickEvent.suggestCommand(style.getClickValue()));
                        case "copy_to_clipboard" -> comp = comp.clickEvent(net.kyori.adventure.text.event.ClickEvent.copyToClipboard(style.getClickValue()));
                    }
                }

                if (style.getInsertText() != null) {
                    comp = comp.insertion(style.getInsertText());
                }

                root = root.append(comp);
            }

            return root;
        }

        @Override
        public @NotNull Text from(@NotNull Component component) {
            return appendComponent(Text.empty(), component);
        }

        @Override
        public Class<Text> getTo() {
            return Text.class;
        }

        @Override
        public Class<Component> getFrom() {
            return Component.class;
        }

        private @NotNull Text appendComponent(@NotNull Text text, @NotNull Component comp) {
            Text result = text;
            if (comp instanceof net.kyori.adventure.text.TextComponent tc) {
                String content = tc.content();
                TextStyle style = extractStyle(tc);
                result = result.append(content, style);
            }

            for (Component child : comp.children()) {
                result = appendComponent(result, child);
            }

            return result;
        }

        private @NotNull TextStyle extractStyle(@NotNull Component comp) {
            TextStyle style = new TextStyle();

            if (comp.color() != null) {
                style.argb(String.format("%08X", Objects.requireNonNull(comp.color()).value()));
            }

            for (TextDecoration deco : TextDecoration.values()) {
                if (comp.decoration(deco) == TextDecoration.State.TRUE) {
                    switch (deco) {
                        case BOLD -> style.bold(true);
                        case ITALIC -> style.italic(true);
                        case UNDERLINED -> style.underlined(true);
                        case STRIKETHROUGH -> style.strikethrough(true);
                        case OBFUSCATED -> style.censored(true);
                    }
                }
            }

            if (comp.insertion() != null) style.insertText(comp.insertion());
            if (comp.hoverEvent() != null) {
                Objects.requireNonNull(comp.hoverEvent());
                style.hoverAction("show_text", Objects.requireNonNull(comp.hoverEvent()).value().toString());
            }

            if (comp.clickEvent() != null) {
                var ev = comp.clickEvent();
                assert ev != null;
                style.clickAction(ev.action().toString().toLowerCase(), ev.value());
            }

            return style;
        }
    };
}