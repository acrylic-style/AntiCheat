package xyz.acrylicstyle.anticheat.command;

import util.StringCollection;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class CommandBindings {
    private StringCollection<CustomCommand> commands = new StringCollection<>();

    public void addCommand(String commandKey, CustomCommand command) {
        commands.add(commandKey, command);
    }

    public CustomCommand getCommand(String command) {
        return commands.get(command);
    }

    public void forEach(BiConsumer<String, CustomCommand> consumer) {
        commands.forEach(consumer);
    }

    public StringCollection<CustomCommand> filterKeys(Function<String, Boolean> function) {
        return commands.filterKeys(function);
    }

    public StringCollection<CustomCommand> getCommands() { return this.commands; }
}