package org.leycm.neck.lang;

import org.leycm.neck.lang.adapters.ConsoleTextAdapter;
import org.leycm.neck.lang.adapters.MinecraftTextAdapters;


public class LangTest {

    public static void main(String[] args) {
        TextProvider.createDefault(null, MinecraftTextAdapters.MINI_MESSAGE);

        Label label = Label.of("message.error", "du.message");

        Text text = label.in(null);

        System.out.println(text.toJson());

        System.out.println(text.toString(ConsoleTextAdapter.ASCI));
        System.out.println(text.toString(MinecraftTextAdapters.MINI_MESSAGE));


    }

}
