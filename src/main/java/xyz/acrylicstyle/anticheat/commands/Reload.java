package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.anticheat.command.CustomCommand;

public class Reload implements CustomCommand {
    @Override
    public void onCommand(Player sender, String[] args) {
        AntiCheatPlugin.getInstance().reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "設定を再読み込みしました。");
    }
}
