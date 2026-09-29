package sircow.sunshinegrace.config;

import net.minecraftforge.fml.loading.FMLPaths;

public class ForgeConfig {
    private ForgeConfig() {}

    public static void loadServer() {
        ConfigManager.loadServer(FMLPaths.CONFIGDIR.get());
    }

    public static void saveServer() {
        ConfigManager.saveServer(FMLPaths.CONFIGDIR.get());
    }
}
