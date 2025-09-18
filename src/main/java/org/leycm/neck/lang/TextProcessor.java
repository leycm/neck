package org.leycm.neck.lang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Handles loading, caching, and retrieving localized text resources from JSON language files.
 * Supports registering custom adapters for text processing and provides methods to fetch translations
 * by key, path, and locale, with fallback to default language if necessary.
 */
public class TextProcessor {

    /**
     * Singleton instance of the TextProcessor.
     */
    private static TextProcessor instance;

    /**
     * Default directory for language files.
     */
    private static final String DEFAULT_LANG_DIR = ".lang";

    /**
     * Cache for loaded language JSON objects, keyed by language code.
     */
    private final Map<String, JsonObject> langCache = new HashMap<>();

    /**
     * Registered adapters for custom text processing, keyed by class type.
     */
    private final Map<Class<?>, Text.Adapter<?>> adapters = new HashMap<>();

    /**
     * Gson instance for JSON parsing and pretty printing.
     */
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Path to the directory containing language files.
     */
    private final String langPath;

    /**
     * Adapter used to process string translations.
     */
    private final Text.Adapter<String> reader;

    /**
     * Constructs a new TextProcessor with the specified language directory and string adapter.
     * Creates the language directory if it does not exist.
     *
     * @param path   Path to the language files directory (uses default if null)
     * @param reader Adapter for processing string translations
     * @throws IOException If the language directory cannot be created
     */
    public TextProcessor(String path, Text.Adapter<String> reader) throws IOException {
        this.langPath = path != null ? path : DEFAULT_LANG_DIR;
        this.reader = reader;
        Files.createDirectories(Path.of(langPath));
    }

    /**
     * Registers a custom text adapter for a specific type.
     *
     * @param type    The class type for which the adapter is registered
     * @param adapter The adapter instance
     * @param <T>     The type parameter
     */
    public <T> void registerTextAdapter(Class<T> type, Text.Adapter<T> adapter) {
        adapters.put(type, adapter);
    }

    /**
     * Retrieves the registered text adapter for the specified type.
     *
     * @param type The class type of the adapter
     * @param <T>  The type parameter
     * @return The registered adapter, or null if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Text.Adapter<T> getTextAdapter(Class<T> type) {
        return (Text.Adapter<T>) adapters.get(type);
    }

    /**
     * Retrieves a translation for the given key using the default language ("en_US").
     * The text is searched in the "messages" path of the language file.
     *
     * @param key The key of the text to retrieve
     * @return A Text object containing the key and its translated value
     */
    public @NotNull Text translationOf(String key) {
        return getTranslationFromJson("messages", key, Locale.of("en", "US"));
    }

    /**
     * Retrieves a translation for the given key using the specified language.
     * The text is searched in the "messages" path of the language file.
     *
     * @param key  The key of the text to retrieve
     * @param lang The language to use for the text
     * @return A Text object containing the key and its translated value
     */
    public @NotNull Text translationOf(String key, Locale lang) {
        return getTranslationFromJson("messages", key, lang);
    }

    /**
     * Retrieves a translation for the given key using the default language ("en_US").
     * The text is searched in the specified path of the language file.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key  The key of the text to retrieve
     * @return A Text object containing the key and its translated value
     */
    public @NotNull Text translationOf(String path, String key) {
        return getTranslationFromJson(path, key, Locale.of("en", "US"));
    }

    /**
     * Retrieves a translation for the given key using the specified language.
     * The text is searched in the specified path of the language file.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key  The key of the text to retrieve
     * @param lang The language to use for the text (null will return the key as the text)
     * @return A Text object containing the key and its translated value
     */
    @Contract("_, _, null -> new")
    public @NotNull Text translationOf(String path, String key, Locale lang) {
        return getTranslationFromJson(path, key, lang);
    }

    /**
     * Internal method to retrieve a translation from a JSON language file.
     * Returns the key as the text if the path or key is not found.
     *
     * @param path The path within the JSON structure where the text is located
     * @param key  The key of the text to retrieve
     * @param lang The language to use for the text
     * @return A Text object containing the key and its translated value
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
     * If the requested language file does not exist, falls back to "en_US.json".
     *
     * @param langCode The language code of the file to load (e.g., "en_US")
     * @return The parsed JsonObject containing all translations for the language, or null if not found
     * @throws IOException If there is an error reading the language file
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

    /**
     * Clears the language file cache.
     */
    public void clearCash() {
        langCache.clear();
    }

    /**
     * Creates and sets the default singleton instance of TextProcessor.
     *
     * @param path   Path to the language files directory
     * @param reader Adapter for processing string translations
     */
    public static void createDefault(String path, Text.Adapter<String> reader) {
        try {
            instance = new TextProcessor(path, reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the default singleton instance of TextProcessor.
     *
     * @return The default TextProcessor instance
     */
    public static TextProcessor getDefault() {
        return instance;
    }

}