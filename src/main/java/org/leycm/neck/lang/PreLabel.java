package org.leycm.neck.lang;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PreLabel extends Label {
    protected Text text;

    @Contract(value = "_, -> new", pure = true)
    public static @NotNull Label of(Text text) {
        return new PreLabel(text);
    }

    private PreLabel(Text text) {
        super(null, null, TextProvider.getDefault());
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
