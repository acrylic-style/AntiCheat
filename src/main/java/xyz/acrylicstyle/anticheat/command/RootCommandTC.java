package xyz.acrylicstyle.anticheat.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.CollectionList;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;

import java.util.ArrayList;
import java.util.List;

public class RootCommandTC implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> emptyList = new ArrayList<>();
        if (args.length == 0) return AntiCheatPlugin.bindings.getCommands().keysList();
        if (args.length == 1) return filterArgsList(AntiCheatPlugin.bindings.getCommands().keysList(), args[0]);
        return emptyList;
    }

    private CollectionList<String> filterArgsList(CollectionList<String> list, String s) {
        return list.filter(s2 -> s2.toLowerCase().replaceAll(".*:(.*)", "$1").startsWith(s.toLowerCase().replaceAll(".*:(.*)", "$1")));
    }
}
