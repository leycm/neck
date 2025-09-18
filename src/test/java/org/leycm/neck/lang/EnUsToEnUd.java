package org.leycm.neck.lang;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class EnUsToEnUd {

    private static final Map<String, String> UPSIDE_DOWN_MAP = new HashMap<>();

    static {
        UPSIDE_DOWN_MAP.put("a", "ɐ");
        UPSIDE_DOWN_MAP.put("b", "q");
        UPSIDE_DOWN_MAP.put("c", "ɔ");
        UPSIDE_DOWN_MAP.put("d", "p");
        UPSIDE_DOWN_MAP.put("e", "ǝ");
        UPSIDE_DOWN_MAP.put("f", "ɟ");
        UPSIDE_DOWN_MAP.put("g", "ƃ");
        UPSIDE_DOWN_MAP.put("h", "ɥ");
        UPSIDE_DOWN_MAP.put("i", "ᴉ");
        UPSIDE_DOWN_MAP.put("j", "ɾ");
        UPSIDE_DOWN_MAP.put("k", "ʞ");
        UPSIDE_DOWN_MAP.put("l", "ꞁ");
        UPSIDE_DOWN_MAP.put("m", "ɯ");
        UPSIDE_DOWN_MAP.put("n", "u");
        UPSIDE_DOWN_MAP.put("o", "o");
        UPSIDE_DOWN_MAP.put("p", "d");
        UPSIDE_DOWN_MAP.put("q", "b");
        UPSIDE_DOWN_MAP.put("r", "ɹ");
        UPSIDE_DOWN_MAP.put("s", "s");
        UPSIDE_DOWN_MAP.put("t", "ʇ");
        UPSIDE_DOWN_MAP.put("u", "n");
        UPSIDE_DOWN_MAP.put("v", "ʌ");
        UPSIDE_DOWN_MAP.put("w", "ʍ");
        UPSIDE_DOWN_MAP.put("x", "x");
        UPSIDE_DOWN_MAP.put("y", "ʎ");
        UPSIDE_DOWN_MAP.put("z", "z");
        UPSIDE_DOWN_MAP.put("A", "∀");
        UPSIDE_DOWN_MAP.put("B", "𐐒");
        UPSIDE_DOWN_MAP.put("C", "Ɔ");
        UPSIDE_DOWN_MAP.put("D", "◖");
        UPSIDE_DOWN_MAP.put("E", "Ǝ");
        UPSIDE_DOWN_MAP.put("F", "Ⅎ");
        UPSIDE_DOWN_MAP.put("G", "פ");
        UPSIDE_DOWN_MAP.put("H", "H");
        UPSIDE_DOWN_MAP.put("I", "I");
        UPSIDE_DOWN_MAP.put("J", "ſ");
        UPSIDE_DOWN_MAP.put("K", "ʞ");
        UPSIDE_DOWN_MAP.put("L", "˥");
        UPSIDE_DOWN_MAP.put("M", "W");
        UPSIDE_DOWN_MAP.put("N", "N");
        UPSIDE_DOWN_MAP.put("O", "O");
        UPSIDE_DOWN_MAP.put("P", "Ԁ");
        UPSIDE_DOWN_MAP.put("Q", "Ό");
        UPSIDE_DOWN_MAP.put("R", "ᴚ");
        UPSIDE_DOWN_MAP.put("S", "S");
        UPSIDE_DOWN_MAP.put("T", "┴");
        UPSIDE_DOWN_MAP.put("U", "∩");
        UPSIDE_DOWN_MAP.put("V", "Λ");
        UPSIDE_DOWN_MAP.put("W", "M");
        UPSIDE_DOWN_MAP.put("X", "X");
        UPSIDE_DOWN_MAP.put("Y", "⅄");
        UPSIDE_DOWN_MAP.put("Z", "Z");
        UPSIDE_DOWN_MAP.put("0", "0");
        UPSIDE_DOWN_MAP.put("1", "Ɩ");
        UPSIDE_DOWN_MAP.put("2", "ᄅ");
        UPSIDE_DOWN_MAP.put("3", "Ɛ");
        UPSIDE_DOWN_MAP.put("4", "ㄣ");
        UPSIDE_DOWN_MAP.put("5", "ϛ");
        UPSIDE_DOWN_MAP.put("6", "9");
        UPSIDE_DOWN_MAP.put("7", "ㄥ");
        UPSIDE_DOWN_MAP.put("8", "8");
        UPSIDE_DOWN_MAP.put("9", "6");
        UPSIDE_DOWN_MAP.put(".", "˙");
        UPSIDE_DOWN_MAP.put(",", "'");
        UPSIDE_DOWN_MAP.put("'", ",");
        UPSIDE_DOWN_MAP.put("\"", "„");
        UPSIDE_DOWN_MAP.put("?", "¿");
        UPSIDE_DOWN_MAP.put("!", "¡");
        UPSIDE_DOWN_MAP.put("[", "]");
        UPSIDE_DOWN_MAP.put("]", "[");
        UPSIDE_DOWN_MAP.put("(", ")");
        UPSIDE_DOWN_MAP.put(")", "(");
        UPSIDE_DOWN_MAP.put("{", "}");
        UPSIDE_DOWN_MAP.put("}", "{");
        UPSIDE_DOWN_MAP.put("<", ">");
        UPSIDE_DOWN_MAP.put(">", "<");
        UPSIDE_DOWN_MAP.put("_", "‾");
    }

    public static void main(String[] args) throws IOException {
        Path input = Paths.get(".lang/en_US.json");
        Path output = Paths.get(".lang/en_UD.json");

        Gson gson = new GsonBuilder().setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        JsonElement root = JsonParser.parseReader(Files.newBufferedReader(input));

        JsonElement transformed = transformJson(root);

        Files.writeString(output, gson.toJson(transformed));
        System.out.println("en_UD.json generated!");
    }

    private static @NotNull JsonElement transformJson(@NotNull JsonElement element) {
        if (element.isJsonObject()) {
            JsonObject obj = new JsonObject();
            for (Map.Entry<String, JsonElement> e : element.getAsJsonObject().entrySet()) {
                obj.add(e.getKey(), transformJson(e.getValue()));
            }
            return obj;
        } else if (element.isJsonArray()) {
            JsonArray arr = new JsonArray();
            for (JsonElement e : element.getAsJsonArray()) {
                arr.add(transformJson(e));
            }
            return arr;
        } else if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return new JsonPrimitive(transformString(element.getAsString()));
        }
        return element;
    }

    private static @NotNull String transformString(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile("<.*?>|[^<]+").matcher(input);
        while (m.find()) tokens.add(m.group());

        Collections.reverse(tokens);

        StringBuilder result = new StringBuilder();
        for (String token : tokens) {
            if (token.startsWith("<") && token.endsWith(">")) {
                if (token.startsWith("</")) {
                    token = "<" + token.substring(2);
                } else {
                    token = "</" + token.substring(1);
                }
                result.append(token);
            } else {
                result.append(reverseAndMap(token));
            }
        }
        return result.toString();
    }


    private static @NotNull String reverseAndMap(@NotNull String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = text.length() - 1; i >= 0; i--) {
            String ch = String.valueOf(text.charAt(i));
            sb.append(UPSIDE_DOWN_MAP.getOrDefault(ch, ch));
        }
        return sb.toString();
    }
}

