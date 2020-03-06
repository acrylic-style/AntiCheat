package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.anticheat.api.command.CustomCommand;

import java.util.concurrent.atomic.AtomicInteger;

public class Check implements CustomCommand {
    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/ac check <Player>");
            return;
        }
        Player target = Bukkit.getPlayerExact(args[0]);
        int moves = AntiCheatPlugin.moves.getOrDefault(target.getUniqueId(), new AtomicInteger()).get();
        int cps = AntiCheatPlugin.cps.getOrDefault(target.getUniqueId(), new AtomicInteger()).get();
        player.sendMessage(ChatColor.YELLOW + target.getName() + ChatColor.GREEN + "'s status:");
        player.sendMessage(ChatColor.YELLOW + "CPS: " + ChatColor.RED + cps);
        player.sendMessage(ChatColor.YELLOW + "Move packets: " + ChatColor.RED + moves);
    }
}
