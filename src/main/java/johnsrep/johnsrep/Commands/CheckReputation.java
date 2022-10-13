package johnsrep.johnsrep.Commands;

import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MainConfiguration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class CheckReputation{
    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;

    public CheckReputation(MySQL mysql, Configuration<MessagesConfiguration> messages) {

        this.messages = messages;
        this.mysql = mysql;
    }


    public void checkReputation(CommandSender sender, OfflinePlayer player) {

        try {
            Reputation reputation = mysql.getAllFromTable(player);
            int sum = 0;
            for (int i = 0; i<=reputation.values.size()-1; i++) {
                sum += reputation.values.get(i);
                String value = reputation.values.get(i) == 1 ?
                        String.valueOf(messages.data().reputation().plusSymbol()) :
                        String.valueOf(messages.data().reputation().minusSymbol());
                sender.sendMessage(
                        reputation.fromPlayer.get(i) + " [" + reputation.values.get(i) +
                        "] - " + reputation.comments.get(i));
            }
            sender.sendMessage(messages.data().reputation() + ": " + sum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
