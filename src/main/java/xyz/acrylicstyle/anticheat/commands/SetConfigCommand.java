package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;
import xyz.acrylicstyle.tomeito_api.utils.Log;

public class SetConfigCommand extends PlayerCommandExecutor {
    @Override
    public void onCommand(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "/ac set <key> <value>");
            return;
        }
        Object value = args[1];
        if (!args[1].equals("null")) {
            try {
                value = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                if (args[1].equals("true")) {
                    value = true;
                } else if (args[1].equals("false")) {
                    value = false;
                } else {
                    try {
                        value = Double.parseDouble(args[1]);
                    } catch (NumberFormatException ignored2) {}
                }
            }
        } else value = null;
        AntiCheatPlugin.getInstance().getConfiguration().setThenSave(args[0], value);
        Log.info("Set " + args[0] + " to " + value + " (" + (value == null ? "null" : value.getClass().getCanonicalName()) + ")");
        sender.sendMessage(ChatColor.GREEN + "設定を保存しました。");
    }
}
