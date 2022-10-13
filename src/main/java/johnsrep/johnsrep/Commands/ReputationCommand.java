package johnsrep.johnsrep.Commands;

import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ReputationCommand implements CommandExecutor {
    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;

    public ReputationCommand(MySQL mysql, Configuration<MessagesConfiguration> messages) {
        this.messages = messages;
        this.mysql = mysql;
    }

    private final AnotherPlayerSet anotherPlayerSet = new AnotherPlayerSet(this.mysql, this.messages);
    private final CheckReputation checkReputation = new CheckReputation(this.mysql, this.messages);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда доступно только игроку");
            return false;
        }
        if (args.length == 0) {
            checkReputation.checkReputation(sender, (OfflinePlayer) sender);
        }
        else if (args.length == 1) {
            checkReputation.checkReputation(sender, Bukkit.getOfflinePlayerIfCached(args[0]));
        }
        else anotherPlayerSet.onCommand(sender, command, label,args);

        return true;
    }
}
