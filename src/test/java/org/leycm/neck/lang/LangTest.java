package org.leycm.neck.lang;

import org.leycm.neck.lang.adapters.ConsoleTextAdapter;
import org.leycm.neck.lang.adapters.MinecraftTextAdapters;

import java.util.Locale;


public class LangTest {

    public static void main(String[] args) {
        TextProcessor.createDefault(".lang", MinecraftTextAdapters.MINI_MESSAGE);

        Label label = Label.of("command.error", "eco.no_currency");
        Text text = label.in(Locale.US);

        System.out.println(text.toJson());

        System.out.println(text.toString(ConsoleTextAdapter.ASCI));
        System.out.println(text.toString(MinecraftTextAdapters.LEGACY));
    }

}
