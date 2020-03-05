package xyz.acrylicstyle.anticheat;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Collection;
import util.CollectionList;
import xyz.acrylicstyle.anticheat.api.AntiCheat;
import xyz.acrylicstyle.anticheat.api.AntiCheatConfiguration;
import xyz.acrylicstyle.anticheat.api.command.CommandBindings;
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

public class AntiCheatPlugin extends JavaPlugin implements Listener, AntiCheat {
    private static AntiCheatPlugin instance = null;
    public static Collection<UUID, AtomicInteger> moves = new Collection<>();
    private AntiCheatConfiguration config = null;
    public static CommandBindings bindings = new CommandBindings();
    public static ConfigProvider version = null;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveResource("version.yml", true);
        this.saveResource("config.yml", false);
        try {
            config = new AntiCheatConfigurationImpl("./plugins/AntiCheat/config.yml");
            version = new ConfigProvider("./plugins/AntiCheat/version.yml");
        } catch (IOException | InvalidConfigurationException e) {
            Log.error("An error occurred while loading config!");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        bindings.addCommand("set", new SetConfig());
        bindings.addCommand("reload", new Reload());
        bindings.addCommand("version", new Version());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setExecutor(new RootCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setTabCompleter(new RootCommandTC());
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getServicesManager().register(AntiCheat.class, this, this, ServicePriority.Normal);
        for (Player player : Bukkit.getOnlinePlayers()) onPlayerJoin(new PlayerJoinEvent(player, ""));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        moves.add(e.getPlayer().getUniqueId(), new AtomicInteger());
    }

    /*@EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        Location playerLocation = e.getPlayer().getLocation();
        //<editor-fold desc="??? Detection" defaultstate="collapsed">
        double distance = negativeToPositive(block.getLocation().getX() - playerLocation.getX())
                + negativeToPositive(block.getLocation().getY() - playerLocation.getY())
                + negativeToPositive(block.getLocation().getZ() - playerLocation.getZ());
        if (distance >= 10) {
            sendMessageToAllOperators(ChatColor.RED + "Kicking player " + e.getPlayer().getName()
                    + " for breaking a block that is located at too far! (Distance: " + distance + " blocks)");
            Log.warn("Kicking player " + e.getPlayer().getName()
                    + " for breaking a block that is located at too far! (Distance: " + distance + " blocks)");
            e.getPlayer().kickPlayer("Detected illegal packet");
            return;
        }
        //</editor-fold>
    }*/

    /*@EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Block block = e.getBlock();
        Player player = e.getPlayer();
        Location playerLocation = e.getPlayer().getLocation();
        //<editor-fold desc="??? Detection" defaultstate="collapsed">
        double distance = negativeToPositive(block.getLocation().getX() - playerLocation.getX())
                + negativeToPositive(block.getLocation().getY() - playerLocation.getY())
                + negativeToPositive(block.getLocation().getZ() - playerLocation.getZ());
        if (distance >= 10) {
            sendMessageToAllOperators(ChatColor.RED + "Kicking player " + e.getPlayer().getName()
                    + " for placing a block that is located at too far! (Distance: " + distance + " blocks)");
            Log.warn("Kicking player " + e.getPlayer().getName()
                    + " for placing a block that is located at too far! (Distance: " + distance + " blocks)");
            e.getPlayer().kickPlayer("Detected illegal packet");
            return;
        }
        //</editor-fold>
    }*/

    public static Collection<UUID, AtomicInteger> cps = new Collection<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //<editor-fold desc="Clickbot" defaultstate="collapsed">
        cps.putIfAbsent(e.getPlayer().getUniqueId(), new AtomicInteger());
        int currentCps = cps.get(e.getPlayer().getUniqueId()).incrementAndGet();
        new BukkitRunnable() {
            @Override
            public void run() {
                cps.get(e.getPlayer().getUniqueId()).decrementAndGet();
            }
        }.runTaskLater(this, 20);
        if (currentCps >= config.getClicksThreshold()) {
            sendMessageToAllOperators(ChatColor.RED + "Kicking player " + e.getPlayer().getName() + " for clicking too fast! (" + currentCps + " cps)");
            Log.debug("Kicking player " + e.getPlayer().getName() + " for clicking too fast! (" + currentCps + " cps)");
            e.getPlayer().kickPlayer("You are sending too many packets!");
        }
        //</editor-fold>
    }

    @EventHandler
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
        if (move != -1 && config.getBlinkPacketsThreshold() < move) {
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
                    if ((player.getLocation().getY() - y) >= config.getFlyVerticalThreshold()) {
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
                    double overall = negativeToPositive(player.getLocation().getX() - x) + negativeToPositive(player.getLocation().getZ() - z);
                    if (overall >= config.getSpeedThreshold()) {
                        sendMessageToAllOperators(ChatColor.RED + "Kicked " + e.getPlayer().getName() + " for possible speed/fly hacking (" + overall + " blocks/s)");
                        Log.warn("Kicking player " + e.getPlayer().getName() + " for possible speed/fly hacking (" + overall + " blocks/s)");
                        e.getPlayer().kickPlayer("You are sending too many packets!");
                    }
                }
            }.runTaskLater(this, 20);
        }
        //</editor-fold>
    }

    public double negativeToPositive(double i) {
        return (i < 0 ? -i : i);
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

    @Override
    public int getPlayerMoves(UUID uuid) throws NullPointerException {
        AtomicInteger atomicInteger = moves.get(uuid);
        if (atomicInteger == null) throw new NullPointerException();
        return atomicInteger.get();
    }

    @Override
    public @NotNull CommandBindings getCommandBindings() {
        return bindings;
    }

    @Override
    public @Nullable ConfigProvider getVersionInfo() {
        return version;
    }

    @Override
    public int getPlayerClicks(UUID uuid) throws NullPointerException {
        AtomicInteger atomicInteger = cps.get(uuid);
        if (atomicInteger == null) throw new NullPointerException();
        return atomicInteger.get();
    }

    @Override
    public AntiCheatConfiguration getConfiguration() throws NullPointerException {
        if (config == null) throw new NullPointerException("Configuration is null! (Perhaps configuration has set to null?)");
        return config;
    }
}
