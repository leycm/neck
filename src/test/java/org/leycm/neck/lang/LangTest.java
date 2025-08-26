package org.leycm.neck.lang;

import org.leycm.neck.lang.adapters.GenericTextAdapter;
import org.leycm.neck.lang.adapters.MinecraftTextAdapters;


public class LangTest {

    public static void main(String[] args) {
        TextProvider.createDefault(null, MinecraftTextAdapters.LEGACY);

        Label label = Label.of("message.error", "du.hs.message");
        Label rainbow = Label.of("message.error", "du.hs.rainbow");

        Text text = label.in(null);
        Text rainbowText = rainbow.in(null);

        System.out.println(text.toJson());

        System.out.println(text.toString(GenericTextAdapter.ASCI_CONSOL));
        System.out.println(text.toString(MinecraftTextAdapters.MINI_MESSAGE));



        System.out.println(rainbowText.toJson());

        System.out.println(rainbowText.toString(GenericTextAdapter.ASCI_CONSOL));
        System.out.println(rainbowText.toString(MinecraftTextAdapters.MINI_MESSAGE));
    }

}
