package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.anticheat.command.CustomCommand;

public class SetConfig implements CustomCommand {
    @Override
    public void onCommand(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "/ac set <key> <value>");
            return;
        }
        if (!AntiCheatPlugin.getInstance().getConfig().getKeys(false).contains(args[0])) {
            sender.sendMessage(ChatColor.RED + "Cannot set config value for non-existent config key");
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
        AntiCheatPlugin.getInstance().getConfig().set(args[0], value);
        AntiCheatPlugin.getInstance().saveConfig();
        sender.sendMessage(ChatColor.GREEN + "設定を保存しました。");
    }
}
