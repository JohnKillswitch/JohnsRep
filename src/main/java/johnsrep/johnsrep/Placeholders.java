package johnsrep.johnsrep;

import johnsrep.johnsrep.commands.CheckReputation;
import johnsrep.johnsrep.commands.Reputation;
import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import johnsrep.johnsrep.utils.ExecuteCommands;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholders extends PlaceholderExpansion {

    private final MySQL mysql;
    private final Configuration<MessagesConfiguration> messages;
    private final Configuration<CommandsConfiguration> commands;
    private final MiniMessage miniMessage;
    private final CheckReputation checkReputation;
    private final ReputationCache reputationCache;
    ExecuteCommands executor;

    public Placeholders(
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            MiniMessage miniMessage,
            ReputationCache reputationCache,
            Configuration<CommandsConfiguration> commands,
            ExecuteCommands executor) {
        this.messages = messages;
        this.mysql = mysql;
        this.miniMessage = miniMessage;
        this.reputationCache = reputationCache;
        this.commands = commands;
        this.executor = executor;

        checkReputation = new CheckReputation(
                this.mysql,
                this.messages,
                this.miniMessage,
                reputationCache,
                commands,
                executor);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "DecaliumRep";
    }

    @Override
    public @NotNull String getAuthor() {
        return "JohnKillswitch";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.1";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if(params.equalsIgnoreCase("total")) {
            int sum = 0;
                sum = reputationCache.getCache(player.getUniqueId());
            return String.valueOf(sum);
        }


        return null; // Placeholder is unknown by the Expansion
    }

    public interface MySQLCallbackPlaceholders {
        void returnData(Reputation reputation);
    }
}
