package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import java.sql.SQLException;

public class CheckReputation {
    private final MySQL mysql;
    private final Configuration<MessagesConfiguration> messages;
    private final MiniMessage miniMessage;
    private final ReputationCache reputationCache;
    Configuration<CommandsConfiguration> commands;

    public CheckReputation(
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            MiniMessage miniMessage,
            ReputationCache reputationCache,
            Configuration<CommandsConfiguration> commands) {

        this.messages = messages;
        this.commands = commands;
        this.mysql = mysql;
        this.miniMessage = miniMessage;
        this.reputationCache = reputationCache;
    }
    MiniMessage mm = MiniMessage.miniMessage();

    public void checkReputation(CommandSender sender, String playerName) {
        OfflinePlayer player = Bukkit.getOfflinePlayerIfCached(playerName);
        if (player == null) {
            sender.sendMessage(
                    miniMessage.deserialize(
                            messages.data().messages().playerNotFound(),
                            Placeholder.parsed("name", playerName)));
            return;
        }

        try {
            mysql.getAllFromTable(player, reputation -> {
                int sum = 0;
                sender.sendMessage(
                        miniMessage.deserialize(
                                messages.data().messages().reputationOfPlayer(),
                                Placeholder.parsed("name", player.getName())));
                sender.sendMessage("");
                for (int i = 0; i<=reputation.values.size()-1; i++) {

                    sum += reputation.values.get(i);
                    String value = reputation.values.get(i) == 1 ?
                            messages.data().messages().plusSymbol() :
                            messages.data().messages().minusSymbol();

                    sender.sendMessage(
                            miniMessage.deserialize(messages.data().messages().reputationFormat(),
                                    Placeholder.parsed("name", reputation.fromPlayer.get(i).getName()),
                                    Placeholder.parsed("value", value),
                                    Placeholder.parsed("comment", reputation.comments.get(i))));
                }
                sender.sendMessage("\n");
                if (sum == 0)
                    sender.sendMessage(miniMessage.deserialize(messages.data().messages().reputationTotalFormat(),
                            Placeholder.parsed("value", String.valueOf(sum))));
                else if (sum > 0 )
                    sender.sendMessage(miniMessage.deserialize(messages.data().messages().reputationTotalFormatPlus(),
                            Placeholder.parsed("value", String.valueOf(sum))));

                else
                    sender.sendMessage(miniMessage.deserialize(messages.data().messages().reputationTotalFormatMinus(),
                            Placeholder.parsed("value", String.valueOf(sum))));
                reputationCache.setCache(player.getUniqueId());
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
