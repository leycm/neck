package org.leycm.lang.label;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.leycm.lang.text.Text;

import java.util.Locale;

public class PreLabel extends Label {
    protected Text text;

    @Contract("_ -> new")
    public static @NotNull Label of(Text text) {
        return new PreLabel(text);
    }

    private PreLabel(Text text) {
        super(null, null, null);
        this.text = text;
    }

    @Override
    public Text in(Locale lang) {
        return text;
    }

    @Override
    public String toString() {
        return "<text:" + text.toJson() + ">";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PreLabel l)) return false;
        return this.text.toPlainString().equals(l.text.toPlainString());
    }
}
