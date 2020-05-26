package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

import java.util.concurrent.atomic.AtomicInteger;

public class CheckCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/ac check <Player>");
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        int moves = AntiCheatPlugin.moves.getOrDefault(target.getUniqueId(), new AtomicInteger()).get();
        int cps = AntiCheatPlugin.cps.getOrDefault(target.getUniqueId(), new AtomicInteger()).get();
        int maxCps = AntiCheatPlugin.maxCps.getOrDefault(target.getUniqueId(), 0);
        player.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s status:");
        player.sendMessage(ChatColor.YELLOW + "CPS: " + ChatColor.RED + cps);
        player.sendMessage(ChatColor.YELLOW + "Max CPS: " + ChatColor.RED + maxCps);
        player.sendMessage(ChatColor.YELLOW + "Move packets: " + ChatColor.RED + moves);
    }
}
