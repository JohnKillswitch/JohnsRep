package johnsrep.johnsrep.Commands;

import johnsrep.johnsrep.database.MySQL;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.sql.SQLException;

public class CheckReputation{
    private MySQL mysql = new MySQL();

    public CheckReputation(MySQL mysql) {
        this.mysql = mysql;
    }

    public void checkReputation(CommandSender sender, OfflinePlayer player) {

        try {
            Reputation reputation = mysql.getAllFromTable(player);
            int sum = 0;
            for (int i = 0; i<=reputation.values.size()-1; i++) {
                sum += reputation.values.get(i);
                String value = reputation.values.get(i) == 1 ? "+" : "-";
                sender.sendMessage(
                        reputation.fromPlayer.get(i) + " [" + reputation.values.get(i) +
                        "] - " + reputation.comments.get(i));
            }
            sender.sendMessage("Репутация: " + sum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
