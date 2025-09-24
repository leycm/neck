package org.leycm;

import org.leycm.lang.adapter.AdventureTextAdapter;
import org.leycm.lang.adapter.BasicTextAdapter;
import org.leycm.lang.label.Label;
import org.leycm.lang.label.TranslationProvider;
import org.leycm.lang.text.Text;

import java.util.Locale;

public class LabelTest {

    public static void main(String[] args) {
        TranslationProvider.createInstance(".lang", AdventureTextAdapter.MINIMESSAGE);

        Text text = Label.of("command.success", "eco.get")
                .in(Locale.US)
                .replace("amount","100")
                .replace("currency", "$")
                .replace("target", "Steve");

        System.out.println(text.toString(BasicTextAdapter.ASCI));
        System.out.println("--------------------------------------------------------------");
        System.out.println(text.toString(BasicTextAdapter.HTML));
        System.out.println("--------------------------------------------------------------");
        System.out.println(text.toString(AdventureTextAdapter.MINIMESSAGE));
        System.out.println("--------------------------------------------------------------");
        System.out.println(text.toString(AdventureTextAdapter.LEGACY));
        System.out.println("--------------------------------------------------------------");
        System.out.println(text.toJson());
        System.out.println("--------------------------------------------------------------");
    }
}
