package xyz.acrylicstyle.anticheat.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import util.CollectionList;
import util.ICollectionList;
import xyz.acrylicstyle.anticheat.AntiCheatPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static xyz.acrylicstyle.tomeito_api.utils.TabCompleterHelper.filterArgsList;

public class RootCommandTC implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> emptyList = new ArrayList<>();
        if (args.length == 0) return AntiCheatPlugin.bindings.getCommands().keysList();
        if (args.length == 1) return filterArgsList(AntiCheatPlugin.bindings.getCommands().keysList(), args[0]);
        if (args.length == 2) {
            if (args[0].equals("check")) return filterArgsList(new CollectionList<>(Bukkit.getOnlinePlayers()).map(Player::getName), args[1]);
            if (args[0].equals("set") || args[0].equals("get")) {
                CollectionList<String> list = ICollectionList.asList(args[0].split("\\."));
                String prefix = "";
                if (list.size() > 0) list.shift();
                if (list.size() > 0) {
                    list.shift();
                    prefix = list.join(".");
                }
                Map<String, Object> map = AntiCheatPlugin.getInstance().getConfiguration().getConfigSectionValue(prefix, false);
                if (map == null) return new ArrayList<>();
                return filterArgsList(new ArrayList<>(map.keySet()), args[1]).map(s -> (list.size() != 0 ? list.join(".") + "." : "") + s);
            }
            if (args[0].equals("bypass")) return filterArgsList(Arrays.asList("add", "remove"), args[1]);
        }
        if (args.length == 3) {
            if (args[0].equals("bypass")
                    && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
                return filterArgsList(ICollectionList.asList(new ArrayList<>(Bukkit.getOnlinePlayers())).map(Player::getName), args[2]);
            }
        }
        return emptyList;
    }
}
