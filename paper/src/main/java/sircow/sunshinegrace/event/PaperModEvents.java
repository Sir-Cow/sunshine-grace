package sircow.sunshinegrace.event;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import sircow.sunshinegrace.Constants;
import sircow.sunshinegrace.config.ConfigManager;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public final class PaperModEvents implements Listener {
    public static final String POSITION_ACTION_BAR = "actionbar";
    public static final String POSITION_BOSS_BAR = "bossbar";

    private static final int DISPLAY_INTERVAL_TICKS = 20;

    private final NamespacedKey hasJoinedBefore, graceRemaining, position;
    private final Map<UUID, Integer> grace = new LinkedHashMap<>();
    private final Map<UUID, BossBar> bossBars = new LinkedHashMap<>();

    public PaperModEvents(JavaPlugin plugin) {
        this.hasJoinedBefore = new NamespacedKey(plugin, "has_joined_before");
        this.graceRemaining = new NamespacedKey(plugin, "grace_remaining");
        this.position = new NamespacedKey(plugin, "position");
    }

    public void tick() {
        Iterator<Map.Entry<UUID, Integer>> entries = grace.entrySet().iterator();

        while (entries.hasNext()) {
            Map.Entry<UUID, Integer> entry = entries.next();
            int remaining = entry.getValue() - 1;
            Player player = Bukkit.getPlayer(entry.getKey());

            if (remaining <= 0) {
                entries.remove();

                if (player != null) {
                    player.getPersistentDataContainer().remove(graceRemaining);
                    hideBossBar(player);
                }
                continue;
            }

            entry.setValue(remaining);

            if (remaining % DISPLAY_INTERVAL_TICKS == 0 && player != null) {
                player.getPersistentDataContainer().set(graceRemaining, PersistentDataType.INTEGER, remaining);
                updateDisplay(player, remaining);
            }
        }
    }

    public void restoreGrace(Player player) {
        Integer remaining = player.getPersistentDataContainer().get(graceRemaining, PersistentDataType.INTEGER);

        if (remaining == null || remaining <= 0) return;

        grace.put(player.getUniqueId(), remaining);
        updateDisplay(player, remaining);
    }

    public void setGrace(Player player, int ticks) {
        if (ticks <= 0) {
            removeGrace(player);
            return;
        }

        grace.put(player.getUniqueId(), ticks);
        player.getPersistentDataContainer().set(graceRemaining, PersistentDataType.INTEGER, ticks);
        updateDisplay(player, ticks);
    }

    public String getPosition(Player player) {
        String stored = player.getPersistentDataContainer().getOrDefault(position, PersistentDataType.STRING, POSITION_ACTION_BAR);

        return POSITION_BOSS_BAR.equals(stored) ? POSITION_BOSS_BAR : POSITION_ACTION_BAR;
    }

    public void setPosition(Player player, String newPosition) {
        player.getPersistentDataContainer().set(position, PersistentDataType.STRING, newPosition);

        Integer remaining = grace.get(player.getUniqueId());

        hideBossBar(player);

        if (remaining != null) updateDisplay(player, remaining);
    }

    public void hideBossBars() {
        for (Map.Entry<UUID, BossBar> entry : bossBars.entrySet()) {
            Player player = Bukkit.getPlayer(entry.getKey());

            if (player != null) player.hideBossBar(entry.getValue());
        }

        bossBars.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PersistentDataContainer data = player.getPersistentDataContainer();

        restoreGrace(player);

        byte joinedBefore = data.getOrDefault(hasJoinedBefore, PersistentDataType.BYTE, (byte) 0);

        if (joinedBefore != 0) return;

        data.set(hasJoinedBefore, PersistentDataType.BYTE, (byte) 1);

        if (ConfigManager.getServer().enableFirstJoinEffect) setGrace(player, ConfigManager.getServer().duration);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Integer remaining = grace.remove(player.getUniqueId());

        hideBossBar(player);

        if (remaining != null) player.getPersistentDataContainer().set(graceRemaining, PersistentDataType.INTEGER, remaining);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getEntity() instanceof Mob)) return;
        if (!(event.getTarget() instanceof Player player)) return;
        if (!grace.containsKey(player.getUniqueId())) return;
        if (player.getLocation().getY() >= ConfigManager.getServer().minimumYValue) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Monster)) return;
        if (!ConfigManager.getServer().enableAttackingMonsterRemovesEffect) return;
        if (!(event.getDamageSource().getCausingEntity() instanceof Player player)) return;
        if (!grace.containsKey(player.getUniqueId())) return;
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        removeGrace(player);
    }

    private void removeGrace(Player player) {
        grace.remove(player.getUniqueId());
        player.getPersistentDataContainer().remove(graceRemaining);
        hideBossBar(player);
    }

    private void updateDisplay(Player player, int remainingTicks) {
        if (POSITION_BOSS_BAR.equals(getPosition(player))) {
            updateBossBar(player, remainingTicks);
            return;
        }

        hideBossBar(player);
        player.sendActionBar(displayText(remainingTicks));
    }

    private void updateBossBar(Player player, int remainingTicks) {
        BossBar bar = bossBars.get(player.getUniqueId());

        if (bar == null) {
            bar = BossBar.bossBar(displayText(remainingTicks), progress(remainingTicks), BossBar.Color.YELLOW, BossBar.Overlay.PROGRESS);
            bossBars.put(player.getUniqueId(), bar);
            player.showBossBar(bar);
            return;
        }

        bar.name(displayText(remainingTicks));
        bar.progress(progress(remainingTicks));
    }

    private void hideBossBar(Player player) {
        BossBar bar = bossBars.remove(player.getUniqueId());

        if (bar != null) player.hideBossBar(bar);
    }

    private float progress(int remainingTicks) {
        return Math.clamp((float) remainingTicks / ConfigManager.getServer().duration, 0.0F, 1.0F);
    }

    private Component displayText(int remainingTicks) {
        int totalSeconds = (remainingTicks + 19) / 20;
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;

        return Component.text(String.format(Locale.ROOT, "%s: %d:%02d", Constants.MOD_NAME, minutes, seconds));
    }
}
