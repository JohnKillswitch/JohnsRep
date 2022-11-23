package johnsrep.johnsrep.Commands;

import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MainConfiguration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import johnsrep.johnsrep.database.ReputationCache;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class AnotherPlayerSet {
    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;
    private final MiniMessage miniMessage;
    private final ReputationCache reputationCache;
    private final Configuration<MainConfiguration> conf;

    public AnotherPlayerSet(
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
    }

    MiniMessage mm = MiniMessage.miniMessage();

    public boolean setReputation(CommandSender sender, Command command, String[] args) throws SQLException {

            // /rep [nickname] [+/-] [коммент]
        Player player = (Player) sender;
        if (player.getStatistic(Statistic.PLAY_ONE_MINUTE) < conf.data().otherSettings().needTimePlayed()*20*3600) {
            sender.sendMessage(miniMessage.deserialize(messages.data().messages().needMorePlayedTime()));
            return false;
        }

        OfflinePlayer repPlayer = Bukkit.getOfflinePlayerIfCached(args[0]);
        if(repPlayer == null) {
            sender.sendMessage(
                    miniMessage.deserialize(
                            messages.data().messages().playerNotFound(),
                            Placeholder.parsed("name", args[0])));
            return false;
        }
        if (repPlayer.equals(sender)) {
            sender.sendMessage(miniMessage.deserialize(messages.data().messages().reputationSelf()));
            return false;
        }
        int value = 0;
        if(!args[1].equals("+") && !args[1].equals("-")) { // Проверка на знак + или -
            sender.sendMessage(miniMessage.deserialize(
                    messages.data().messages().commandUsedWrong() + "\n" +
                            messages.data().messages().commandRightUsing()));
            return false;
        }
        else {
            if(args[1].equals("+"))
                value = 1;
            if(args[1].equals("-"))
                value = -1;
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
        sender.sendMessage(
                mm.deserialize(
                        messages.data().messages().commandSuccessfullyReputation(),
                        Placeholder.parsed("prefix", messages.data().messages().pluginPrefix()),
                        Placeholder.parsed("name", repPlayer.getName())));

        if (repPlayer.isOnline())
            repPlayer.getPlayer().sendMessage(
                    mm.deserialize(
                            messages.data().messages().playerReceiveReputation(),
                            Placeholder.parsed("prefix", messages.data().messages().pluginPrefix()),
                            Placeholder.parsed("name", sender.getName())));

        reputationCache.setCache(repPlayer.getUniqueId());

        return true;
    }

}
