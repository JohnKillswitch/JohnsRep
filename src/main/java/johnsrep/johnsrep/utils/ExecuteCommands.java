package johnsrep.johnsrep.utils;

import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExecuteCommands {

    private final Configuration<CommandsConfiguration> commands;
    private final JohnsRep plugin;
    private final MiniMessage miniMessage;

    MiniMessage mm = MiniMessage.miniMessage();

    public ExecuteCommands(JohnsRep plugin, Configuration<CommandsConfiguration> commands, MiniMessage miniMessage) {
        this.commands = commands;
        this.plugin = plugin;
        this.miniMessage = miniMessage;
    }

    public void executeCommands(List<String> commands, Player playerFrom, OfflinePlayer playerTo) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (String command : commands) {
            command = command
                    .replaceAll("<senderName>", playerFrom.getName())
                    .replaceAll("<recipientName>", playerTo.getName());
            Bukkit.dispatchCommand(console, command);
        }
    }

}
