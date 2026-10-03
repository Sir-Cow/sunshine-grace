package sircow.sunshinegrace.platform;

import org.bukkit.Bukkit;
import sircow.sunshinegrace.platform.services.IPlatformHelper;

public final class PaperPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Paper";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return Bukkit.getPluginManager().getPlugin(modId) != null;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return Boolean.getBoolean("sunshinegrace.development");
    }
}
