package impl.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.Gson;

public class JsonUtils {
    private static final Gson gson = new Gson();

    public static <T> T read(Path path, Class<T> clazz) {
        try {
            String content = Files.readString(path);
            return gson.fromJson(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from " + path, e);
        }
    }
}

