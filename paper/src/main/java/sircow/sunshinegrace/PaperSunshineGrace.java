package sircow.sunshinegrace;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import sircow.sunshinegrace.command.SunshineGraceCommand;
import sircow.sunshinegrace.config.ConfigManager;
import sircow.sunshinegrace.event.PaperModEvents;
import sircow.sunshinegrace.help.SunshineGraceHelpTopic;

import java.util.Objects;

public final class PaperSunshineGrace extends JavaPlugin {
    private PaperModEvents events;

    @Override
    public void onEnable() {
        CommonClass.init();
        ConfigManager.loadServer(getDataFolder().toPath());

        events = new PaperModEvents(this);
        Bukkit.getPluginManager().registerEvents(events, this);

        SunshineGraceCommand command = new SunshineGraceCommand(events, getDataFolder().toPath());
        PluginCommand pluginCommand = Objects.requireNonNull(getCommand("sunshinegrace"));
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);

        Bukkit.getHelpMap().addTopic(new SunshineGraceHelpTopic(pluginCommand));

        Bukkit.getCommandMap().getKnownCommands().remove(Constants.MOD_ID + ":sunshinegrace");

        for (Player player : Bukkit.getOnlinePlayers()) {
            events.restoreGrace(player);
        }

        Bukkit.getScheduler().runTaskTimer(this, events::tick, 1L, 1L);
    }

    @Override
    public void onDisable() {
        if (events != null) events.hideBossBars();
    }
}
