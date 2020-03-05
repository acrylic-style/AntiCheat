package xyz.acrylicstyle.anticheat.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.anticheat.api.command.CustomCommand;

public class Reload implements CustomCommand {
    @Override
    public void onCommand(Player sender, String[] args) {
        AntiCheatPlugin.getInstance().getConfiguration().reloadWithoutException();
        sender.sendMessage(ChatColor.GREEN + "Reloaded configuration.");
    }
}
