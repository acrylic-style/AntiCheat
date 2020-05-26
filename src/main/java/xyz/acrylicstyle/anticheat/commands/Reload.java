package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class Reload extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player sender, String[] args) {
        AntiCheatPlugin.getInstance().getConfiguration().reload();
        sender.sendMessage(ChatColor.GREEN + "Reloaded configuration.");
    }
}
