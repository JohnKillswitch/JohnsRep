package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import johnsrep.johnsrep.utils.ConfigsHelper;
import johnsrep.johnsrep.utils.ExecuteCommands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;

public class AnotherPlayerSet {
    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;
    private final ConfigsHelper configsHelper;
    private final ReputationCache reputationCache;
    private final Configuration<MainConfiguration> conf;
    private final Configuration<CommandsConfiguration> commands;
    ExecuteCommands executor;

    public AnotherPlayerSet (
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            ConfigsHelper configsHelper,
            ReputationCache reputationCache,
            Configuration<MainConfiguration> conf,
            ExecuteCommands executor,
            Configuration<CommandsConfiguration> commands) {

        this.configsHelper = configsHelper;
        this.messages = messages;
        this.mysql = mysql;
        this.reputationCache = reputationCache;
        this.conf = conf;
        this.executor = executor;
        this.commands = commands;
    }

    public boolean setReputation(CommandSender sender, Command command, String[] args) throws SQLException {

            // /rep [nickname] [+/-] [коммент]
        Player player = (Player) sender;
        if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) < conf.data().otherSettings().needTimePlayed()*20*3600) {
            this.configsHelper.sendMessage(sender, messages.data().messages().needMorePlayedTime());
            return false;
        }

        OfflinePlayer repPlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        if(repPlayer == null) {
            this.configsHelper.sendMessageWithPlaceholder(
                    sender,
                    messages.data().messages().playerNotFound(),
                    Placeholder.parsed("name", args[0]));
            return false;
        }

        if (repPlayer.equals(sender)) {
            this.configsHelper.sendMessage(sender, messages.data().messages().reputationSelf());
            return false;
        }

        int value = 0;
        switch (args[1]) {
            case "+":
                value = 1;
                executor.executeCommands(commands.data().commands().plusReputationAdded(),player,repPlayer);
                break;
            case "-":
                value = -1;
                executor.executeCommands(commands.data().commands().minusReputationAdded(),player,repPlayer);
                break;
            default:
                this.configsHelper.sendMessage(sender,
                        messages.data().messages().commandUsedWrong() + "\n" +
                                messages.data().messages().commandRightUsing());
                return false;
        }


        StringBuilder repComment = new StringBuilder();
        for(int i = 2; i <= (args.length - 1); i++){ // Составление коммента из аргументов
            repComment.append(args[i]).append(" ");
        }
        try {
            mysql.insertInTable(repPlayer, (Player)sender, value, repComment.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.configsHelper.sendMessageWithPlaceholder(
                sender,
                messages.data().messages().commandSuccessfullyReputation(),
                Placeholder.parsed("prefix", messages.data().messages().pluginPrefix()),
                Placeholder.parsed("name", repPlayer.getName()));


        if (repPlayer.isOnline())
            this.configsHelper.sendMessageWithPlaceholder(
                    sender,
                    messages.data().messages().playerReceiveReputation(),
                    Placeholder.parsed("prefix", messages.data().messages().pluginPrefix()),
                    Placeholder.parsed("name", sender.getName()));

        reputationCache.setCache(repPlayer.getUniqueId());

        return true;
    }

}
