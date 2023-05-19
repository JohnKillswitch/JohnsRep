package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReloadConfigCommand {

    private Configuration<MessagesConfiguration> messages;
    private final MiniMessage miniMessage;
    private final Configuration<MainConfiguration> conf;
    private final Configuration<CommandsConfiguration> commands;
    public ReloadConfigCommand (
            Configuration<MessagesConfiguration> messages,
            Configuration<MainConfiguration> conf,
            MiniMessage miniMessage,
            Configuration<CommandsConfiguration> commands) {

        this.messages = messages;
        this.conf = conf;
        this.commands = commands;
        this.miniMessage = miniMessage;

    }
    public void reloadConfig() {
        this.messages.reloadConfig();
        this.conf.reloadConfig();
        this.commands.reloadConfig();
    }
}
