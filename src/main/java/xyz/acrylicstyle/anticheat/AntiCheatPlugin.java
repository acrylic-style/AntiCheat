package xyz.acrylicstyle.anticheat;

import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.anticheat.command.CommandBindings;
import xyz.acrylicstyle.anticheat.command.RootCommand;
import xyz.acrylicstyle.anticheat.command.RootCommandTC;
import xyz.acrylicstyle.anticheat.commands.Reload;
import xyz.acrylicstyle.anticheat.commands.SetConfig;
import xyz.acrylicstyle.anticheat.commands.Version;
import xyz.acrylicstyle.tomeito_core.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_core.utils.Log;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AntiCheatPlugin extends JavaPlugin implements Listener {
    private static AntiCheatPlugin instance = null;
    public static Collection<UUID, AtomicInteger> moves = new Collection<>();
    private FileConfiguration config = null;
    public static CommandBindings bindings = new CommandBindings();
    public static ConfigProvider version = null;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveResource("version.yml", true);
        try {
            version = new ConfigProvider("./plugins/AntiCheat/version.yml");
        } catch (IOException | InvalidConfigurationException e) {
            Log.error("An error occurred while loading config!");
            e.printStackTrace();
        }
        bindings.addCommand("set", new SetConfig());
        bindings.addCommand("reload", new Reload());
        bindings.addCommand("version", new Version());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setExecutor(new RootCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setTabCompleter(new RootCommandTC());
        config = this.getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        for (Player player : Bukkit.getOnlinePlayers()) onPlayerJoin(new PlayerJoinEvent(player, ""));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        moves.add(e.getPlayer().getUniqueId(), new AtomicInteger());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        Location from = e.getFrom();
        Location to = e.getTo();
        double x = to == null ? from.getX() : to.getX();
        double y = to == null ? from.getY() : to.getY();
        double z = to == null ? from.getZ() : to.getZ();
        //<editor-fold desc="Blink Detection" defaultstate="collapsed">
        int move = moves.get(e.getPlayer().getUniqueId()).incrementAndGet();
        new BukkitRunnable() {
            @Override
            public void run() {
                moves.get(e.getPlayer().getUniqueId()).decrementAndGet();
            }
        }.runTaskLater(this, 20);
        int threshold = config.getInt("PlayerMoveEvent.threshold", 30); // 30 should be safe because max ticks are 20. Normally cannot go above 20.
        if (move != -1 && threshold < move) {
            sendMessageToAllOperators(ChatColor.RED + "Kicked " + e.getPlayer().getName() + " for sending too many move packets. (" + move + " packets/s)");
            Log.warn("Kicking player " + e.getPlayer().getName() + " for sending too many move packets! (" + move + " packets/s)");
            e.getPlayer().kickPlayer("You are sending too many packets!");
            return;
        }
        //</editor-fold>
        //<editor-fold desc="Fly Detection (Partial, only works when player is going up)" defaultstate="collapsed">
        if (!player.hasPotionEffect(PotionEffectType.JUMP)
                && (!player.getAllowFlight() && player.getFlySpeed() <= 0.2)
                && player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    int flyThreshold = config.getInt("PlayerMoveEvent.flyThresholdVertical", 10); // 10 should be safe at most times
                    if ((player.getLocation().getY() - y) >= flyThreshold) {
                        sendMessageToAllOperators(ChatColor.RED + "Kicked " + e.getPlayer().getName() + " for flying (" + (player.getLocation().getY() - y) + " blocks/s)");
                        Log.warn("Kicking player " + e.getPlayer().getName() + " for flying (" + (player.getLocation().getY() - y) + " blocks/s)");
                        e.getPlayer().kickPlayer("Flying is not enabled on this server");
                    }
                }
            }.runTaskLater(this, 20);
        }
        //</editor-fold>
        //<editor-fold desc="Fly & Speed Detection" defaultstate="collapsed">
        if (!player.hasPotionEffect(PotionEffectType.SPEED)
                && !player.getAllowFlight()
                && player.getWalkSpeed() <= 0.3
                && player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    int speedThreshold = config.getInt("PlayerMoveEvent.speedThreshold", 30); // 30 should be safe at most times
                    if ((player.getLocation().getX() - x) >= speedThreshold) {
                        sendMessageToAllOperators(ChatColor.RED + "Kicked " + e.getPlayer().getName() + " for possible speed/fly hacking (" + (player.getLocation().getX() - (to == null ? from.getX() : to.getX())) + " blocks/s)");
                        Log.warn("Kicking player " + e.getPlayer().getName() + " for possible speed/fly hacking (" + (player.getLocation().getX() - (to == null ? from.getX() : to.getX())) + " blocks/s)");
                        e.getPlayer().kickPlayer("You are sending too many packets!");
                    }
                }
            }.runTaskLater(this, 20);
        }
        if (!player.hasPotionEffect(PotionEffectType.SPEED)
                && !player.getAllowFlight()
                && player.getWalkSpeed() <= 0.3
                && player.getGameMode() != GameMode.CREATIVE
                && player.getGameMode() != GameMode.SPECTATOR) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) return;
                    int speedThreshold = config.getInt("PlayerMoveEvent.speedThreshold", 30); // 30 should be safe at most times
                    if ((player.getLocation().getX() - z) >= speedThreshold) {
                        sendMessageToAllOperators(ChatColor.RED + "Kicked " + e.getPlayer().getName() + " for possible speed/fly hacking (" + (player.getLocation().getX() - (to == null ? from.getX() : to.getX())) + " blocks/s)");
                        Log.warn("Kicking player " + e.getPlayer().getName() + " for possible speed/fly hacking (" + (player.getLocation().getX() - (to == null ? from.getX() : to.getX())) + " blocks/s)");
                        e.getPlayer().kickPlayer("You are sending too many packets!");
                    }
                }
            }.runTaskLater(this, 20);
        }
        //</editor-fold>
    }

    public static CollectionList<Player> getOnlineOperators() {
        CollectionList<Player> players = new CollectionList<>();
        for (OfflinePlayer player : Bukkit.getOperators()) {
            if (player.isOnline()) players.add(Bukkit.getPlayer(player.getUniqueId()));
        }
        return players;
    }

    public static void sendMessageToAllOperators(String message) {
        getOnlineOperators().forEach(player -> player.sendMessage(message));
    }

    public static AntiCheatPlugin getInstance() { return instance; }
}
