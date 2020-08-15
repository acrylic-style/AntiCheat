package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class VersionCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player sender, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "AntiCheat Version " + AntiCheatPlugin.getInstance().getDescription().getVersion());
    }
}