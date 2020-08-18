package xyz.acrylicstyle.anticheat.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import util.Collection;
import util.CollectionList;
import util.ICollectionList;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;
import xyz.acrylicstyle.tomeito_api.command.PlayerCommandExecutor;

public class RootCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command cannot be invoked from console.");
            return true;
        }
        if (!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to do that.");
            return true;
        }
        if (args.length == 0) {
            $sendMessage(sender);
            return true;
        }
        Collection<String, PlayerCommandExecutor> commands = AntiCheatPlugin.bindings
                .filterKeys(cmd -> cmd.equals(args[0]))
                .mapValues((k, v) -> (PlayerCommandExecutor) v);
        if (commands.size() == 0) {
            $sendMessage(sender);
            return true;
        }
        CollectionList<String> argsList = ICollectionList.asList(args);
        argsList.shift();
        commands.forEach((cmd, customCommand) -> customCommand.onCommand((Player) sender, argsList.toArray(new String[0])));
        return true;
    }

    public String getCommandHelp(String command, String description, ChatColor withColor, String... replace) {
        for (String s : replace) description = description.replaceAll(s, withColor + s + ChatColor.AQUA);
        return ChatColor.YELLOW + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description;
    }

    public String getCommandHelp(String command, String description) {
        return ChatColor.YELLOW + command + ChatColor.GRAY + " - " + ChatColor.AQUA + description;
    }

    public void $sendMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "-----------------------------------");
        sender.sendMessage(getCommandHelp("/ac check <Player>", "Check player's status."));
        sender.sendMessage(getCommandHelp("/ac get <key>", "Get config value."));
        sender.sendMessage(getCommandHelp("/ac set <key> <value>", "Set config value."));
        sender.sendMessage(getCommandHelp("/ac reload", "Reloads config."));
        sender.sendMessage(getCommandHelp("/ac version", "Shows AntiCheat plugin's version."));
        sender.sendMessage(getCommandHelp("/ac notify", "Toggles AC Notification."));
        sender.sendMessage(getCommandHelp("/ac bypass <add/remove> <player>", "Add or remove from bypass list."));
        sender.sendMessage(ChatColor.GOLD + "-----------------------------------");
    }
}
