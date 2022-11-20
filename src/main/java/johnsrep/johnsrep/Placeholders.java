package johnsrep.johnsrep;

import johnsrep.johnsrep.Commands.CheckReputation;
import johnsrep.johnsrep.Commands.Reputation;
import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import johnsrep.johnsrep.database.ReputationCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.UUID;

public class Placeholders extends PlaceholderExpansion {

    private final MySQL mysql;
    private final Configuration<MessagesConfiguration> messages;
    private final MiniMessage miniMessage;
    private final CheckReputation checkReputation;
    private final ReputationCache reputationCache;

    public Placeholders(MySQL mysql, Configuration<MessagesConfiguration> messages, MiniMessage miniMessage, ReputationCache reputationCache) {
        this.messages = messages;
        this.mysql = mysql;
        this.miniMessage = miniMessage;
        this.reputationCache = reputationCache;

        checkReputation = new CheckReputation(this.mysql, this.messages, this.miniMessage, reputationCache);
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
