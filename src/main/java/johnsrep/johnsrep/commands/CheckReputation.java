package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import johnsrep.johnsrep.utils.ConfigsHelper;
import johnsrep.johnsrep.utils.ExecuteCommands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.command.CommandSender;
import java.sql.SQLException;

public class CheckReputation {
    private final MySQL mysql;
    private final Configuration<MessagesConfiguration> messages;
    private final ConfigsHelper configsHelper;
    private final ReputationCache reputationCache;
    Configuration<CommandsConfiguration> commands;
    ExecuteCommands executor;

    public CheckReputation(
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            ConfigsHelper configsHelper,
            ReputationCache reputationCache,
            Configuration<CommandsConfiguration> commands,
            ExecuteCommands executor) {

        this.messages = messages;
        this.commands = commands;
        this.mysql = mysql;
        this.configsHelper = configsHelper;
        this.reputationCache = reputationCache;
        this.executor = executor;
    }
    MiniMessage mm = MiniMessage.miniMessage();

    public void checkReputation(CommandSender sender, String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) {
            this.configsHelper.sendMessageWithPlaceholder(
                    sender,
                    messages.data().messages().playerNotFound(),
                    Placeholder.parsed("name", playerName));
            return;
        }

        try {
            mysql.getAllFromTable(player, reputation -> {
                int sum = 0;

                this.configsHelper.sendMessageWithPlaceholder(
                        sender,
                        messages.data().messages().reputationOfPlayer(),
                        Placeholder.parsed("name", player.getName()));
                sender.sendMessage("");

                for (int i = 0; i<=reputation.values.size()-1; i++) {

                    sum += reputation.values.get(i);
                    String symbol = reputation.values.get(i) == 1 ?
                            messages.data().messages().plusSymbol() :
                            messages.data().messages().minusSymbol();

                    this.configsHelper.sendMessageWithPlaceholder(
                            sender,
                            messages.data().messages().reputationFormat(),
                            Placeholder.parsed("name", reputation.fromPlayer.get(i).getName()),
                            Placeholder.parsed("value", symbol),
                            Placeholder.parsed("comment", reputation.comments.get(i)));
                }


                sender.sendMessage("\n");

                if (sum == 0)
                    this.configsHelper.sendMessageWithPlaceholder(
                            sender,
                            messages.data().messages().reputationTotalFormat(),
                            Placeholder.parsed("value", String.valueOf(sum)));
                else if (sum > 0 )
                    this.configsHelper.sendMessageWithPlaceholder(
                            sender,
                            messages.data().messages().reputationTotalFormatPlus(),
                            Placeholder.parsed("value", String.valueOf(sum)));

                else
                this.configsHelper.sendMessageWithPlaceholder(
                        sender,
                        messages.data().messages().reputationTotalFormatMinus(),
                            Placeholder.parsed("value", String.valueOf(sum)));

                reputationCache.setCache(player.getUniqueId());
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
