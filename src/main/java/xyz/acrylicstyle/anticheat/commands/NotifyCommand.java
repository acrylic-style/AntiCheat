package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class NotifyCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player player, String[] args) {
        if (AntiCheatPlugin.notifyOff.contains(player.getUniqueId())) {
            AntiCheatPlugin.notifyOff.remove(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Turned on notification.");
        } else {
            AntiCheatPlugin.notifyOff.add(player.getUniqueId());
            player.sendMessage(ChatColor.GREEN + "Turned off notification.");
        }
    }
}
