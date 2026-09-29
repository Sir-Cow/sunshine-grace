package sircow.sunshinegrace.config;

import net.fabricmc.loader.api.FabricLoader;

public class FabricConfig {
    private FabricConfig() {}

    public static void loadServer() {
        ConfigManager.loadServer(FabricLoader.getInstance().getConfigDir());
    }

    public static void saveServer() {
        ConfigManager.saveServer(FabricLoader.getInstance().getConfigDir());
    }
}
