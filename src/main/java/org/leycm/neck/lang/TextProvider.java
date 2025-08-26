package org.leycm.neck.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.leycm.neck.lang.adapters.MinecraftTextAdapters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TextProvider {
    private static TextProvider instance;
    private static final String DEFAULT_LANG_DIR = ".lang";

    private final Map<String, JsonObject> langCache = new HashMap<>();
    private final Map<Class<?>, Text.Adapter<?>> adapters = new HashMap<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final String langPath;
    private final Text.Adapter<String> reader;

    public TextProvider(String path, Text.Adapter<String> reader) throws IOException {
        this.langPath = path != null ? path : DEFAULT_LANG_DIR;
        this.reader = reader;
        Files.createDirectories(Path.of(langPath));
    }

    public <T> void registerTextAdapter(Class<T> type, Text.Adapter<T> adapter) {
        adapters.put(type, adapter);
    }

    @SuppressWarnings("unchecked")
    public <T> Text.Adapter<T> getTextAdapter(Class<T> type) {
        return (Text.Adapter<T>) adapters.get(type);
    }

    /**
     * Retrieves a text for the given key using the default language (en-en).
     * The text is searched in the "messages" path of the language file.
     *
     * @param key The key of the text to retrieve
     * @return A Translation object containing the key and its translated value
     */
    public @NotNull Text translationOf(String key) {
        return getTranslationFromJson("messages", key, Locale.of("en", "US"));
    }

    /**
     * Retrieves a text for the given key using the specified language.
     * The text is searched in the "messages" path of the language file.
     *
     * @param key The key of the text to retrieve
     * @param lang The language to use for the text
     * @return A Translation object containing the key and its translated value
     */
    public @NotNull Text translationOf(String key, Locale lang) {
        return getTranslationFromJson("messages", key, lang);
    }

    /**
     * Retrieves a text for the given key using the default language (en-en).
     * The text is searched in the specified path of the language file.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key The key of the text to retrieve
     * @return A Translation object containing the key and its translated value
     */
    public @NotNull Text translationOf(String path, String key) {
        return getTranslationFromJson(path, key, Locale.of("en", "US"));
    }

    /**
     * Retrieves a text for the given key using the specified language.
     * The text is searched in the specified path of the language file.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key The key of the text to retrieve
     * @param lang The language to use for the text (null will return the key as the text)
     * @return A Translation object containing the key and its translated value
     */
    @Contract("_, _, null -> new")
    public @NotNull Text translationOf(String path, String key, Locale lang) {
        return getTranslationFromJson(path, key, lang);
    }

    /**
     * Internal method to retrieve a text from a JSON language file.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key The key of the text to retrieve
     * @param lang The language to use for the text
     * @return A Translation object containing the key and its translated value
     *         (returns the key as the text if the path/key is not found)
     */
    @Contract("_, _, null -> new")
    private @NotNull Text getTranslationFromJson(String path, String key, Locale lang) {
        if (lang == null)
            lang = Locale.US;

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

    /**
     * Loads a language file from disk or returns a cached version if available.
     * If the requested language file doesn't exist, falls back to en-en.json.
     *
     * @param langCode The language code of the file to load (e.g., "en-en")
     * @return The parsed JsonObject containing all translations for the language
     * @throws IOException If there's an error reading the language file
     */
    public @Nullable JsonObject loadLanguageFile(@NotNull String langCode) throws IOException {
        if (langCache.containsKey(langCode)) return langCache.get(langCode);

        String fullPath = Paths.get(langPath, langCode + ".json").toString();
        File langFile = new File(fullPath);

        if (!langFile.exists()) {
            String fallbackPath = Paths.get(langPath, "en_US.json").toString();
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

    public void clearCash() {
        langCache.clear();
    }

    public static TextProvider createDefault(String path, Text.Adapter<String> reader) {
        try {return instance = new TextProvider(path, reader);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static TextProvider getDefault() {
        return instance;
    }

}
