package org.leycm.lang.label;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.leycm.lang.text.Text;
import org.leycm.lang.text.TextAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class TranslationProvider {
    private static TranslationProvider instance;
    private static final String DEFAULT_LANG_DIR = ".lang";

    private final Map<String, JsonObject> langCache = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final String langPath;
    private final TextAdapter<String> reader;

    public TranslationProvider(String path, TextAdapter<String> reader) throws IOException {
        this.langPath = path != null ? path : DEFAULT_LANG_DIR;
        this.reader = reader;
        Files.createDirectories(Path.of(langPath));
    }

    public Text translationOf(String key) {
        return getTranslationFromJson("messages", key, Locale.of("en", "US"));
    }

    public Text translationOf(String key, Locale lang) {
        return getTranslationFromJson("messages", key, lang);
    }

    public Text translationOf(String path, String key) {
        return getTranslationFromJson(path, key, Locale.of("en", "US"));
    }

    public Text translationOf(String path, String key, Locale lang) {
        return getTranslationFromJson(path, key, lang);
    }

    private Text getTranslationFromJson(String path, String key, Locale lang) {
        if (lang == null) lang = Locale.US;

        try {
            JsonObject rootObject = loadLanguageFile(lang.getLanguage() + "_" + lang.getCountry());
            if (rootObject == null) return Text.of(path + "/" + key);

            JsonElement currentElement = rootObject;
            if (path != null && !path.isEmpty()) {
                for (String part : path.split("\\.")) {
                    if (!currentElement.isJsonObject()) return Text.of(path + "/" + key);
                    currentElement = currentElement.getAsJsonObject().get(part);
                    if (currentElement == null || currentElement.isJsonNull()) return Text.of(path + "/" + key);
                }
            }

            if (!currentElement.isJsonObject()) return Text.of(path + "/" + key);
            JsonElement valueElement = currentElement.getAsJsonObject().get(key);
            if (valueElement == null || valueElement.isJsonNull()) return Text.of(path + "/" + key);

            if (valueElement.isJsonPrimitive() && valueElement.getAsJsonPrimitive().isString()) {
                return Text.fromString(valueElement.getAsString(), reader);
            }

        } catch (Exception e) {
            throw new RuntimeException("Fail to translate from json ", e);
        }

        return Text.of(path + "/" + key);
    }

    public JsonObject loadLanguageFile(String langCode) throws IOException {
        if (langCache.containsKey(langCode)) return langCache.get(langCode);

        String fullPath = Paths.get(langPath, langCode + ".json").toString();
        File langFile = new File(fullPath);

        if (!langFile.exists()) {
            String fallbackPath = Paths.get(langPath, "en_us.json").toString();
            File fallbackFile = new File(fallbackPath);
            if (!fallbackFile.exists()) return null;

            String content = new String(Files.readAllBytes(Paths.get(fallbackPath)));
            JsonObject rootObject = gson.fromJson(content, JsonObject.class);
            langCache.put(langCode, rootObject);
            return rootObject;
        }

        String content = new String(Files.readAllBytes(Paths.get(fullPath)));
        JsonObject rootObject = gson.fromJson(content, JsonObject.class);
        langCache.put(langCode, rootObject);
        return rootObject;
    }

    public void clearCache() {
        langCache.clear();
    }

    public static void createInstance(String path, TextAdapter<String> reader) {
        try {
            instance = new TranslationProvider(path, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TranslationProvider getInstance() {
        return instance;
    }
}
