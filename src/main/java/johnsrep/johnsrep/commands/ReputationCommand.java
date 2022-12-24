package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class ReputationCommand implements CommandExecutor {

    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;
    private final AnotherPlayerSet anotherPlayerSet;
    private final CheckReputation checkReputation;
    private final MiniMessage miniMessage;
    private final ReputationCache reputationCache;
    private final Configuration<MainConfiguration> conf;

    public ReputationCommand(
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            MiniMessage miniMessage,
            ReputationCache reputationCache,
            Configuration<MainConfiguration> conf) {

        this.messages = messages;
        this.mysql = mysql;
        this.miniMessage = miniMessage;
        this.reputationCache = reputationCache;
        this.conf = conf;



        anotherPlayerSet = new AnotherPlayerSet(this.mysql, this.messages, this.miniMessage, reputationCache, conf);
        checkReputation = new CheckReputation(this.mysql, this.messages, this.miniMessage, reputationCache);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда доступно только игроку");
            return true;
        }
        if (args.length == 0) {
            if (!sender.hasPermission("rep.checkself")) {
                sender.sendMessage(miniMessage.deserialize(messages.data().messages().commandNoPermission()));
                return true;
            }
            checkReputation.checkReputation(sender, ((Player) sender).getPlayer().getName());
        }
        else if (args.length == 1) {
            if (!sender.hasPermission("rep.checkother")) {
                sender.sendMessage(miniMessage.deserialize(messages.data().messages().commandNoPermission()));
                return true;
            }
            checkReputation.checkReputation(sender, args[0]);
        }
        else{
            if (!sender.hasPermission("rep.set")) {
                sender.sendMessage(miniMessage.deserialize(messages.data().messages().commandNoPermission()));
                return true;
            }
            try {
                anotherPlayerSet.setReputation(sender, command,args);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return true;
    }
}