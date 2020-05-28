package xyz.acrylicstyle.anticheat;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
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
import xyz.acrylicstyle.anticheat.commands.*;
import xyz.acrylicstyle.anticheat.reflection.Reflections;
import xyz.acrylicstyle.tomeito_api.providers.ConfigProvider;
import xyz.acrylicstyle.tomeito_api.utils.Log;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AntiCheatPlugin extends JavaPlugin implements Listener, AntiCheat {
    public static final String PREFIX = ChatColor.GOLD + "[AC] ";
    private static AntiCheatPlugin instance = null;
    public static Collection<UUID, AtomicInteger> moves = new Collection<>();
    private AntiCheatConfiguration config = null;
    public static CommandBindings bindings = new CommandBindings();
    public static ConfigProvider version = null;
    public static CollectionList<UUID> notifyOff = new CollectionList<>();

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        this.saveResource("version.yml", true);
        this.saveResource("config.yml", false);
        config = new AntiCheatConfigurationImpl("./plugins/AntiCheat/config.yml");
        version = new ConfigProvider("./plugins/AntiCheat/version.yml");
        bindings.addCommand("set", new SetConfigCommand());
        bindings.addCommand("reload", new ReloadCommand());
        bindings.addCommand("version", new VersionCommand());
        bindings.addCommand("check", new CheckCommand());
        bindings.addCommand("get", new GetConfigCommand());
        bindings.addCommand("notify", new NotifyCommand());
        bindings.addCommand("bypass", new BypassCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setExecutor(new RootCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("ac")).setTabCompleter(new RootCommandTC());
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getServicesManager().register(AntiCheat.class, this, this, ServicePriority.Normal);
        for (Player player : Bukkit.getOnlinePlayers()) onPlayerJoin(new PlayerJoinEvent(player, ""));
        new BukkitRunnable() {
            @Override
            public void run() {
                messages.clone().forEach((uuid, string) -> {
                    sendMessageToAllOperators(string);
                    Log.info(string);
                });
                messages.clear();
            }
        }.runTaskTimer(this, 20, 20);
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

    public static Collection<UUID, Integer> maxCps = new Collection<>();
    public static Collection<UUID, AtomicInteger> cps = new Collection<>();
    public static Collection<UUID, String> messages = new Collection<>();

    private boolean log(Player player, String reason, String value) {
        if (config.kickPlayer()) {
            sendMessageToAllOperators(PREFIX + ChatColor.RED + "Kicking player " + player.getName() + " for " + reason + " " + value);
            Log.info(PREFIX + ChatColor.RED + "Kicking player " + player.getName() + " for " + reason + " " + value);
        } else {
            messages.remove(player.getUniqueId());
            messages.add(player.getUniqueId(), PREFIX + ChatColor.RED + player.getName() + " is possible " + reason + " " + value);
        }
        return config.kickPlayer();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getPlayer().hasPermission("anticheat.bypass")
                        || config.getBypassList().contains(e.getPlayer().getUniqueId())) return;
                //<editor-fold desc="Clickbot detection" defaultstate="collapsed">
                if (!e.getPlayer().hasPermission("anticheat.bypass") && config.detectClickBot()) {
                    if (e.getAction() == Action.LEFT_CLICK_BLOCK) return; // dont count block breaks
                    EquipmentSlot slot = Reflections.getHand(e);
                    if (slot != null && slot != EquipmentSlot.HAND) return;
                    if (!cps.containsKey(e.getPlayer().getUniqueId()))
                        cps.add(e.getPlayer().getUniqueId(), new AtomicInteger());
                    if (!maxCps.containsKey(e.getPlayer().getUniqueId())) maxCps.add(e.getPlayer().getUniqueId(), 0);
                    int currentCps = cps.get(e.getPlayer().getUniqueId()).incrementAndGet();
                    if (maxCps.get(e.getPlayer().getUniqueId()) < currentCps)
                        maxCps.put(e.getPlayer().getUniqueId(), currentCps);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cps.get(e.getPlayer().getUniqueId()).decrementAndGet();
                        }
                    }.runTaskLater(AntiCheatPlugin.this, 20);
                    if (currentCps >= config.getClicksThreshold()) {
                        if (log(e.getPlayer(), "clicking too fast", "(" + currentCps + " cps)"))
                            e.getPlayer().kickPlayer("You are sending too many packets!");
                    }
                }
                //</editor-fold>
            }
        }.runTaskAsynchronously(this);
    }

    public static CollectionList<UUID> teleportedRecently = new CollectionList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        teleportedRecently.add(e.getPlayer().getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                teleportedRecently.remove(e.getPlayer().getUniqueId());
            }
        }.runTaskLater(this, 30);
    }

    private void kickPlayer(Player player, String reason) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.kickPlayer(reason);
            }
        }.runTask(this);
    }

    public static double round(double d) {
        return Math.round(d*10D)/10D;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent e) {
        new Thread(() -> {
            Player player = e.getPlayer();
            Location from = e.getFrom();
            Location to = e.getTo();
            double x = to == null ? from.getX() : to.getX();
            double y = to == null ? from.getY() : to.getY();
            double z = to == null ? from.getZ() : to.getZ();
            int move = moves.get(e.getPlayer().getUniqueId()).incrementAndGet();
            new BukkitRunnable() {
                @Override
                public void run() {
                    moves.get(e.getPlayer().getUniqueId()).decrementAndGet();
                }
            }.runTaskLaterAsynchronously(this, 20);
            if (e.getPlayer().hasPermission("anticheat.bypass")
                    || config.getBypassList().contains(e.getPlayer().getUniqueId())) return;
            //<editor-fold desc="Blink Detection" defaultstate="collapsed">
            if (config.detectBlink()) {
                if (move != -1 && config.getBlinkPacketsThreshold() < move) {
                    if (log(e.getPlayer(), "sending too many move packets", "(" + move + " packets/s)")) {
                        kickPlayer(e.getPlayer(), "You are sending too many packets!");
                    }
                    return;
                }
            }
            //</editor-fold>
            //<editor-fold desc="Fly Detection (Partial, only works when player is going up)" defaultstate="collapsed">
            if (config.detectFly()) {
                if (!player.hasPotionEffect(PotionEffectType.JUMP)
                        && (!player.getAllowFlight() && player.getFlySpeed() <= 0.2)
                        && player.getGameMode() != GameMode.CREATIVE
                        && player.getGameMode() != GameMode.SPECTATOR
                        && !Reflections.isGliding(e.getPlayer())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isOnline()) return;
                            if (Reflections.isGliding(e.getPlayer())) return;
                            if (e.getPlayer().isFlying()) return;
                            if (teleportedRecently.contains(e.getPlayer().getUniqueId()))
                                return; // if the player teleported recently, cancel it
                            if ((player.getLocation().getY() - y) >= config.getFlyVerticalThreshold() && (player.getLocation().getY() - y) < 100) {
                                if (log(e.getPlayer(), "flying", "(" + round(player.getLocation().getY() - y) + " blocks/s)")) {
                                    kickPlayer(e.getPlayer(), "Flying is not enabled on this server");
                                }
                            }
                        }
                    }.runTaskLater(this, 20);
                }
            }
            //</editor-fold>
            //<editor-fold desc="Fly & Speed Detection" defaultstate="collapsed">
            if (config.detectSpeed()) {
                if (!player.hasPotionEffect(PotionEffectType.SPEED)
                        && !player.getAllowFlight()
                        && player.getWalkSpeed() <= 0.3
                        && player.getGameMode() != GameMode.CREATIVE
                        && player.getGameMode() != GameMode.SPECTATOR
                        && !Reflections.isGliding(e.getPlayer())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!player.isOnline()) return;
                            if (Reflections.isGliding(e.getPlayer())) return;
                            if (e.getPlayer().isFlying()) return;
                            if (teleportedRecently.contains(e.getPlayer().getUniqueId()))
                                return; // if the player teleported recently, cancel it
                            double overall = negativeToPositive(player.getLocation().getX() - x) + negativeToPositive(player.getLocation().getZ() - z);
                            if (overall >= config.getSpeedThreshold()) {
                                if (log(e.getPlayer(), "speed/fly", "(" + round(overall) + " blocks/s)")) {
                                    kickPlayer(e.getPlayer(), "You are sending too many packets!");
                                }
                            }
                        }
                    }.runTaskLater(this, 20);
                }
            }
            //</editor-fold>
        }).start();
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
        getOnlineOperators().filter(p -> !notifyOff.contains(p.getUniqueId())).forEach(player -> player.sendMessage(message));
    }

    public static AntiCheatPlugin getInstance() { return instance; }

    @Override
    public int getPlayerMoves(@NotNull UUID uuid) throws NullPointerException {
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
    public int getPlayerClicks(@NotNull UUID uuid) throws NullPointerException {
        AtomicInteger atomicInteger = cps.get(uuid);
        if (atomicInteger == null) throw new NullPointerException();
        return atomicInteger.get();
    }

    @Override
    public @NotNull AntiCheatConfiguration getConfiguration() throws NullPointerException {
        if (config == null) throw new NullPointerException("Configuration is null! (Perhaps configuration has set to null?)");
        return config;
    }
}
