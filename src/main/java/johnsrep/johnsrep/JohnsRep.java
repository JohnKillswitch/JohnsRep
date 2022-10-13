package johnsrep.johnsrep;

import johnsrep.johnsrep.Commands.ReputationCommand;
import johnsrep.johnsrep.Connection.PlayerEnteringServer;
import johnsrep.johnsrep.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class JohnsRep extends JavaPlugin {
    MySQL mysql = new MySQL();


    @Override
    public void onEnable() {
        mysql.connect();
        try {
            mysql.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getCommand("rep").setExecutor(new ReputationCommand(this.mysql));
        getServer().getPluginManager().registerEvents(new PlayerEnteringServer(), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
