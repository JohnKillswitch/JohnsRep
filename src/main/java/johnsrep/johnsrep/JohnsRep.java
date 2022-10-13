package johnsrep.johnsrep;

import johnsrep.johnsrep.Commands.ReputationCommand;
import johnsrep.johnsrep.Connection.PlayerEnteringServer;
import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MainConfiguration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.dazzleconf.ConfigurationOptions;

import java.sql.SQLException;

public final class JohnsRep extends JavaPlugin {



    @Override
    public void onEnable() {
        Configuration<MainConfiguration> conf = Configuration.create(
                this,
                "config.yml",
                MainConfiguration.class,
                ConfigurationOptions.defaults());
        Configuration<MessagesConfiguration> messages = Configuration.create(
                this,
                "messages.yml",
                MessagesConfiguration.class,
                ConfigurationOptions.defaults());

        MySQL mysql = new MySQL(conf);
        mysql.connect();
        try {
            mysql.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.getCommand("rep").setExecutor(new ReputationCommand(mysql, messages));
        getServer().getPluginManager().registerEvents(new PlayerEnteringServer(), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
