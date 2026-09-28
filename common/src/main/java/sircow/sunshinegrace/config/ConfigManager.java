package sircow.sunshinegrace.config;

import com.google.gson.*;
import net.minecraft.server.level.ServerLevel;
import sircow.sunshinegrace.Constants;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public final class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String SERVER_FILE_NAME = "sunshinegrace.json";

    private static ServerModConfig serverConfig = new ServerModConfig();

    private ConfigManager() {}

    public static void loadServer(Path configDirectory) {
        Path configFile = configDirectory.resolve(SERVER_FILE_NAME);

        try {
            Files.createDirectories(configDirectory);
        }
        catch (IOException exception) {
            Constants.LOG.error("Failed to create the config directory", exception);
            serverConfig = new ServerModConfig();
            return;
        }

        if (!Files.exists(configFile)) {
            serverConfig = new ServerModConfig();
            saveServer(configDirectory);
            return;
        }

        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            JsonElement element = JsonParser.parseReader(reader);

            if (!element.isJsonObject()) throw new IOException("Config root is not a JSON object");

            serverConfig = fromJson(element.getAsJsonObject(), new ServerModConfig());
        }
        catch (Exception exception) {
            Constants.LOG.error("Failed to load {}, using defaults", configFile, exception);
            serverConfig = new ServerModConfig();
            saveServer(configDirectory);
        }
    }

    public static void saveServer(Path configDirectory) {
        Path configFile = configDirectory.resolve(SERVER_FILE_NAME);
        JsonObject json = new JsonObject();

        json.addProperty("enableFirstJoinEffect", serverConfig.enableFirstJoinEffect);
        json.addProperty("duration", serverConfig.duration);
        json.addProperty("minimumYValue", serverConfig.minimumYValue);

        write(configFile, json);
    }

    public static ServerModConfig getServer() {
        return serverConfig;
    }

    private static ServerModConfig fromJson(JsonObject json, ServerModConfig config) {
        config.enableFirstJoinEffect = getBoolean(json, "enableFirstJoinEffect", config.enableFirstJoinEffect);
        config.duration = getInt(json, "duration", config.duration, 1, Integer.MAX_VALUE);
        config.minimumYValue = getDouble(json, "minimumYValue", config.minimumYValue, ServerLevel.MIN_ENTITY_SPAWN_Y, ServerLevel.MAX_ENTITY_SPAWN_Y);

        return config;
    }

    private static void write(Path configFile, JsonObject json) {
        Path temporaryFile = configFile.resolveSibling(configFile.getFileName() + ".tmp");

        try {
            Files.createDirectories(configFile.getParent());

            try (Writer writer = Files.newBufferedWriter(temporaryFile, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
                GSON.toJson(json, writer);
            }

            try {
                Files.move(temporaryFile, configFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, configFile, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException exception) {
            Constants.LOG.error("Failed to save {}", configFile, exception);
        }
    }

    private static boolean getBoolean(JsonObject json, String key, boolean defaultValue) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) return defaultValue;

        try {
            return element.getAsBoolean();
        }
        catch (RuntimeException exception) {
            return defaultValue;
        }
    }

    private static int getInt(JsonObject json, String key, int defaultValue, int minimum, int maximum) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) return defaultValue;

        try {
            return Math.clamp(element.getAsInt(), minimum, maximum);
        }
        catch (RuntimeException exception) {
            return defaultValue;
        }
    }

    private static double getDouble(JsonObject json, String key, double defaultValue, double minimum, double maximum) {
        JsonElement element = json.get(key);

        if (element == null || !element.isJsonPrimitive()) return defaultValue;

        try {
            double value = element.getAsDouble();

            if (!Double.isFinite(value)) {
                return defaultValue;
            }

            return Math.clamp(value, minimum, maximum);
        }
        catch (RuntimeException exception) {
            return defaultValue;
        }
    }
}
