package org.leycm.neck.json;

import com.google.gson.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class JsonHashMap extends HashMap<String, Object> {

    private static final Gson GSON;

    static {
        GsonBuilder builder = new GsonBuilder();

        builder.registerTypeAdapter(UUID.class, (JsonSerializer<UUID>) (src, _, _) -> new JsonPrimitive(src.toString()));

        builder.registerTypeAdapter(Instant.class, (JsonSerializer<Instant>) (src, _, _) -> new JsonPrimitive(src.toString()));

        builder.registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, _, _) -> new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));

        builder.serializeNulls();
        GSON = builder.create();
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
