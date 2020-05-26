package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class BypassCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.RED + "/ac bypass <add/remove> <player>");
            return;
        }
        if (args[0].equalsIgnoreCase("add")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "/ac bypass add <player>");
                return;
            }
            Player p = Bukkit.getPlayerExact(args[1]);
            if (p == null) {
                player.sendMessage(ChatColor.RED + "Player " + args[1] + " does not exist.");
                return;
            }
            if (AntiCheatPlugin.getInstance().getConfiguration().getBypassList().contains(p.getUniqueId())) {
                player.sendMessage(ChatColor.RED + args[1] + " is already in the bypass list.");
                return;
            }
            AntiCheatPlugin.getInstance().getConfiguration().addBypassList(p.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Added " + args[1] + " to the bypass list.");
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (args.length == 1) {
                player.sendMessage(ChatColor.RED + "/ac bypass remove <player>");
                return;
            }
            Player p = Bukkit.getPlayerExact(args[1]);
            if (p == null) {
                player.sendMessage(ChatColor.RED + "Player " + args[1] + " does not exist.");
                return;
            }
            if (!AntiCheatPlugin.getInstance().getConfiguration().getBypassList().contains(p.getUniqueId())) {
                player.sendMessage(ChatColor.RED + args[1] + " is not in the bypass list.");
                return;
            }
            AntiCheatPlugin.getInstance().getConfiguration().removeBypassList(p.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Removed " + args[1] + " from the bypass list.");
        }
    }
}
