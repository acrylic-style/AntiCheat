package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class GetConfigCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(@NotNull Player sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "/ac get <key>");
            return;
        }
        Object o = AntiCheatPlugin.getInstance().getConfiguration().get(args[0]);
        sender.sendMessage(ChatColor.GREEN + args[0] + ": " + ChatColor.RED + o + " " + ChatColor.GRAY + "(" + (o == null ? null : o.getClass().getCanonicalName()) + ")");
    }
}
