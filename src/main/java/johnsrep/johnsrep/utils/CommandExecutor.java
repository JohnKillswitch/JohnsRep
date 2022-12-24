package johnsrep.johnsrep.utils;

import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import org.bukkit.entity.Player;

public class CommandExecutor {

    private final Configuration<CommandsConfiguration> commands;
    private final JohnsRep plugin;

    public CommandExecutor (JohnsRep plugin, Configuration<CommandsConfiguration> commands) {
        this.commands = commands;
        this.plugin = plugin;
    }

    public void executeSelfCheckCommands(Player player) {
        commands.data();
    }

}
