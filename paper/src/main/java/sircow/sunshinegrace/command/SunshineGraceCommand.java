package sircow.sunshinegrace.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.config.ConfigManager;
import sircow.sunshinegrace.config.ServerModConfig;
import sircow.sunshinegrace.event.PaperModEvents;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class SunshineGraceCommand implements CommandExecutor, TabCompleter {
    private static final String SET_PERMISSION = "sunshinegrace.command.set";
    private static final String CONFIG_PERMISSION = "sunshinegrace.command.config";
    private static final String NO_PERMISSION = "You do not have permission to use this command.";
    private static final String COMMAND_USAGE = "Usage: /sunshinegrace <position|set|config>";
    private static final String POSITION_USAGE = "Usage: /sunshinegrace position <actionbar|bossbar>";
    private static final String SET_USAGE = "Usage: /sunshinegrace set <player> [ticks]";
    private static final String SET_RANGE = "The tick value must be between 1 and 2147483647.";
    private static final String CONFIG_USAGE = "Usage: /sunshinegrace config <setting> [value]";
    private static final String BOOLEAN_FAILURE = "Expected true or false.";
    private static final String DURATION_FAILURE = "duration must be a whole number between 1 and 2147483647.";
    private static final String SETTING_ENABLE_FIRST_JOIN = "enableFirstJoinEffect";
    private static final String SETTING_ENABLE_ATTACKING = "enableAttackingMonsterRemovesEffect";
    private static final String SETTING_DURATION = "duration";
    private static final String SETTING_MINIMUM_Y = "minimumYValue";

    private static final List<String> CONFIG_SETTINGS = List.of(SETTING_ENABLE_FIRST_JOIN, SETTING_ENABLE_ATTACKING, SETTING_DURATION, SETTING_MINIMUM_Y);

    private final PaperModEvents events;
    private final Path configDirectory;

    public SunshineGraceCommand(PaperModEvents events, Path configDirectory) {
        this.events = events;
        this.configDirectory = configDirectory;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(message(COMMAND_USAGE));
            return true;
        }

        if (isPositionCommand(args[0])) {
            handlePosition(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            handleSet(sender, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("config")) {
            handleConfig(sender, args);
            return true;
        }

        sender.sendMessage(message(COMMAND_USAGE));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String[] args) {
        if (args.length == 1) return complete(List.of("position", "pos", "set", "config"), args[0]);

        if (isPositionCommand(args[0])) {
            if (args.length == 2) return complete(List.of(PaperModEvents.POSITION_ACTION_BAR, PaperModEvents.POSITION_BOSS_BAR), args[1]);
            return List.of();
        }

        if (args[0].equalsIgnoreCase("set") && sender.hasPermission(SET_PERMISSION)) {
            if (args.length == 2) {
                List<String> names = Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();

                return complete(names, args[1]);
            }
            if (args.length == 3) return complete(List.of(String.valueOf(ConfigManager.getServer().duration)), args[2]);
        }

        if (args[0].equalsIgnoreCase("config") && sender.hasPermission(CONFIG_PERMISSION)) {
            if (args.length == 2) return complete(CONFIG_SETTINGS, args[1]);
            if (args.length == 3) {
                if (SETTING_ENABLE_FIRST_JOIN.equalsIgnoreCase(args[1]) || SETTING_ENABLE_ATTACKING.equalsIgnoreCase(args[1])) {
                    return complete(List.of("true", "false"), args[2]);
                }
                return List.of();
            }
            return List.of();
        }

        return List.of();
    }

    private void handlePosition(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(failure("Only players can change the position."));
            return;
        }

        if (args.length == 1) {
            player.sendMessage(message("Position: " + events.getPosition(player)));
            return;
        }

        String requested = args[1].toLowerCase(Locale.ROOT);

        if (!PaperModEvents.POSITION_ACTION_BAR.equals(requested) && !PaperModEvents.POSITION_BOSS_BAR.equals(requested)) {
            player.sendMessage(failure(POSITION_USAGE));
            return;
        }

        events.setPosition(player, requested);
        player.sendMessage(message("Position set to " + requested + "."));
    }

    private void handleSet(CommandSender sender, String[] args) {
        if (!sender.hasPermission(SET_PERMISSION)) {
            sender.sendMessage(failure(NO_PERMISSION));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(failure(SET_USAGE));
            return;
        }

        int ticks = ConfigManager.getServer().duration;

        if (args.length >= 3) {
            if (!args[2].matches("-?\\d+")) {
                sender.sendMessage(failure(SET_USAGE));
                return;
            }

            try {
                ticks = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException exception) {
                sender.sendMessage(failure(SET_RANGE));
                return;
            }
        }

        Player target = Bukkit.getPlayerExact(args[1]);

        if (target == null) {
            sender.sendMessage(failure("Player " + args[1] + " is not online."));
            return;
        }

        events.setGrace(target, ticks);

        if (ticks <= 0) {
            sender.sendMessage(message("Removed sunshine grace from " + target.getName() + "."));
            return;
        }

        sender.sendMessage(message("Set sunshine grace for " + target.getName() + " to " + ticks + " ticks."));
    }

    private void handleConfig(CommandSender sender, String[] args) {
        if (!sender.hasPermission(CONFIG_PERMISSION)) {
            sender.sendMessage(failure(NO_PERMISSION));
            return;
        }

        ServerModConfig config = ConfigManager.getServer();

        if (args.length == 1) {
            sender.sendMessage(message(SETTING_ENABLE_FIRST_JOIN + " = " + configValue(config, SETTING_ENABLE_FIRST_JOIN)));
            sender.sendMessage(message(SETTING_ENABLE_ATTACKING + " = " + configValue(config, SETTING_ENABLE_ATTACKING)));
            sender.sendMessage(message(SETTING_DURATION + " = " + configValue(config, SETTING_DURATION)));
            sender.sendMessage(message(SETTING_MINIMUM_Y + " = " + configValue(config, SETTING_MINIMUM_Y)));
            return;
        }

        String key = canonicalSetting(args[1]);

        if (key == null) {
            sender.sendMessage(failure(CONFIG_USAGE));
            return;
        }

        if (args.length == 2) {
            sender.sendMessage(message(key + " = " + configValue(config, key)));
            return;
        }

        if (!setConfigValue(sender, args[2], key, config)) return;

        ConfigManager.saveServer(configDirectory);
        sender.sendMessage(message(key + " set to " + configValue(config, key) + "."));
    }

    private boolean setConfigValue(CommandSender sender, String value, String key, ServerModConfig config) {
        if (SETTING_ENABLE_FIRST_JOIN.equals(key) || SETTING_ENABLE_ATTACKING.equals(key)) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                sender.sendMessage(failure(BOOLEAN_FAILURE));
                return false;
            }

            boolean enabled = value.equalsIgnoreCase("true");

            if (SETTING_ENABLE_FIRST_JOIN.equals(key)) {
                config.enableFirstJoinEffect = enabled;
            }

            if (SETTING_ENABLE_ATTACKING.equals(key)) {
                config.enableAttackingMonsterRemovesEffect = enabled;
            }

            return true;
        }

        if (SETTING_DURATION.equals(key)) {
            int duration;

            try {
                duration = Integer.parseInt(value);
            }
            catch (NumberFormatException exception) {
                sender.sendMessage(failure(DURATION_FAILURE));
                return false;
            }

            if (duration < 1) {
                sender.sendMessage(failure(DURATION_FAILURE));
                return false;
            }

            config.duration = duration;
            return true;
        }

        double minimumY;

        try {
            minimumY = Double.parseDouble(value);
        }
        catch (NumberFormatException exception) {
            sender.sendMessage(failure(minimumYFailure()));
            return false;
        }

        if (!Double.isFinite(minimumY) || minimumY < ConfigManager.getMinimumYLowerBound() || minimumY > ConfigManager.getMinimumYUpperBound()) {
            sender.sendMessage(failure(minimumYFailure()));
            return false;
        }

        config.minimumYValue = minimumY;
        return true;
    }

    private static String canonicalSetting(String value) {
        for (String setting : CONFIG_SETTINGS) {
            if (setting.equalsIgnoreCase(value)) return setting;
        }

        return null;
    }

    private static String configValue(ServerModConfig config, String key) {
        if (SETTING_ENABLE_FIRST_JOIN.equals(key)) return String.valueOf(config.enableFirstJoinEffect);
        if (SETTING_ENABLE_ATTACKING.equals(key)) return String.valueOf(config.enableAttackingMonsterRemovesEffect);
        if (SETTING_DURATION.equals(key)) return String.valueOf(config.duration);

        return String.valueOf(config.minimumYValue);
    }

    private static String minimumYFailure() {
        return SETTING_MINIMUM_Y + " must be a number between " + formatBound(ConfigManager.getMinimumYLowerBound()) + " and " + formatBound(ConfigManager.getMinimumYUpperBound()) + ".";
    }

    private static String formatBound(double value) {
        long whole = (long) value;

        if (whole == value) return String.valueOf(whole);

        return String.valueOf(value);
    }

    private static Component message(String text) {
        return prefix().append(body(text, NamedTextColor.GRAY, NamedTextColor.WHITE));
    }

    private static Component failure(String text) {
        return prefix().append(body(text, NamedTextColor.RED, NamedTextColor.RED));
    }

    private static Component body(String text, NamedTextColor baseColor, NamedTextColor delimiterColor) {
        TextComponent.Builder builder = Component.text();
        int start = 0;
        int open = indexOfArgument(text, 0);

        while (open != -1) {
            char closing = text.charAt(open) == '<' ? '>' : ']';
            int close = text.indexOf(closing, open + 1);

            if (close == -1) break;

            builder.append(Component.text(text.substring(start, open), baseColor));
            builder.append(argument(text.substring(open, close + 1), delimiterColor));
            start = close + 1;
            open = indexOfArgument(text, start);
        }

        return builder.append(Component.text(text.substring(start), baseColor)).build();
    }

    private static Component argument(String token, NamedTextColor delimiterColor) {
        TextComponent.Builder builder = Component.text();
        String content = token.substring(1, token.length() - 1);
        int start = 0;
        int separator = content.indexOf('|');

        builder.append(Component.text(token.substring(0, 1), delimiterColor));

        while (separator != -1) {
            builder.append(Component.text(content.substring(start, separator), NamedTextColor.GRAY));
            builder.append(Component.text("|", delimiterColor));
            start = separator + 1;
            separator = content.indexOf('|', start);
        }

        builder.append(Component.text(content.substring(start), NamedTextColor.GRAY));
        builder.append(Component.text(token.substring(token.length() - 1), delimiterColor));

        return builder.build();
    }

    private static int indexOfArgument(String text, int fromIndex) {
        int angle = text.indexOf('<', fromIndex);
        int bracket = text.indexOf('[', fromIndex);

        if (angle == -1) return bracket;
        if (bracket == -1) return angle;

        return Math.min(angle, bracket);
    }

    private static Component prefix() {
        return Component.text("[", NamedTextColor.DARK_RED)
                .append(Component.text(Constants.MOD_NAME, NamedTextColor.GOLD))
                .append(Component.text("] ", NamedTextColor.DARK_RED));
    }

    private static boolean isPositionCommand(String value) {
        return value.equalsIgnoreCase("position") || value.equalsIgnoreCase("pos");
    }

    private static List<String> complete(List<String> options, String input) {
        String prefix = input.toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<>();

        for (String option : options) {
            if (option.startsWith(prefix)) matches.add(option);
        }

        return matches;
    }
}
