package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class Version extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player sender, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "AntiCheat Version " + AntiCheatPlugin.version.getString("version"));
        sender.sendMessage(ChatColor.GREEN + "Build: #" + AntiCheatPlugin.version.getInt("build"));
        sender.sendMessage(ChatColor.GREEN + "Built at: " + AntiCheatPlugin.version.getString("date"));
        sender.sendMessage(ChatColor.GREEN + "Commit: " + AntiCheatPlugin.version.getString("commit")
                + " @ " + AntiCheatPlugin.version.getString("github")
                + " by " + AntiCheatPlugin.version.getString("committer"));
    }
}