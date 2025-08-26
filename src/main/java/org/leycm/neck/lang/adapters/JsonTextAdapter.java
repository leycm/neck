package org.leycm.neck.lang.adapters;

import com.google.gson.*;
import org.jetbrains.annotations.NotNull;
import org.leycm.neck.lang.Text;
import org.leycm.neck.lang.Style;
import org.leycm.neck.lang.Text.Part;

import java.lang.reflect.Type;

/**
 * JSON Adapter for Text class to convert between Text and JSON representation
 */
public interface JsonTextAdapter{


    Text.Adapter<String> EXTENDET = new Text.Adapter<String>() {
        private static final Gson GSON = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Text.class, new TextSerializer())
                .registerTypeAdapter(Text.class, new TextDeserializer())
                .create();

        @Override
        public @NotNull String to(@NotNull Text text) {
            return GSON.toJson(text);
        }

        @Override
        public @NotNull Text from(@NotNull String json) {
            return GSON.fromJson(json, Text.class);
        }

        /**
         * Serializer for Text class
         */
        private static class TextSerializer implements JsonSerializer<Text> {
            @Override
            public @NotNull JsonElement serialize(@NotNull Text text, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject jsonObject = new JsonObject();
                JsonArray partsArray = new JsonArray();

                for (Part part : text.getParts()) {
                    JsonObject partObject = new JsonObject();
                    partObject.addProperty("text", part.toPlainString());

                    if (part.getStyles() != null) {
                        partObject.add("style", context.serialize(part.getStyles()));
                    }
                    partsArray.add(partObject);
                }

                jsonObject.add("parts", partsArray);
                return jsonObject;
            }
        }

        /**
         * Deserializer for Text class
         */
        private static class TextDeserializer implements JsonDeserializer<Text> {
            @Override
            public @NotNull Text deserialize(@NotNull JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                JsonObject jsonObject = json.getAsJsonObject();
                JsonArray partsArray = jsonObject.getAsJsonArray("parts");

                Text text = new Text(Text.getProvider());

                for (JsonElement partElement : partsArray) {
                    JsonObject partObject = partElement.getAsJsonObject();
                    String textContent = partObject.get("text").getAsString();

                    Style style = null;
                    if (partObject.has("style")) {
                        style = context.deserialize(partObject.get("style"), Style.class);
                    }

                    text.getParts().add(new Part(textContent, style));
                }

                return text;
            }
        }
    };

    /**
     * Convenience method to convert Text to JSON string
     */
    static @NotNull String toJson(Text text) {
        return EXTENDET.to(text);
    }

    /**
     * Convenience method to convert JSON string to Text
     */
    static @NotNull Text fromJson(String json) {
        return EXTENDET.from(json);
    }
}