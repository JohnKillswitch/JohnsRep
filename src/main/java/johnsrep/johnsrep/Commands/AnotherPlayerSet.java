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

import java.sql.SQLException;

public class AnotherPlayerSet implements CommandExecutor {
    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;

    public AnotherPlayerSet(MySQL mysql, Configuration<MessagesConfiguration> messages) {
        this.messages = messages;
        this.mysql = mysql;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            // /rep [nickname] [+/-] [коммент]
        OfflinePlayer repPlayer = Bukkit.getOfflinePlayerIfCached(sender.getName());
        if(repPlayer == null) {
            return false;
        }
//        if (repPlayer.equals(sender)) {
//            sender.sendMessage("Нельзя выдать себе");
//            return false;
//        }
        int value = 0;
        if(!args[1].equals("+") && !args[1].equals("-")) { // Проверка на знак + или -
            sender.sendMessage("Команда введена неправильно");
            sender.sendMessage("Правильное использование: /rep [Ник] [+/-] [Комментарий]");
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
        return true;
    }

}
