package johnsrep.johnsrep.databaseRelated;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.JohnsRep;

import java.nio.file.Path;
import java.text.MessageFormat;

public class HikariDataSourceCreation {

    private final JohnsRep plugin;
    private final Configuration<MainConfiguration> mainConfig;

    public HikariDataSourceCreation(JohnsRep plugin, Configuration<MainConfiguration> mainConfig) {
        this.plugin = plugin;
        this.mainConfig = mainConfig;
    }

    public HikariDataSource create() {
        HikariConfig config = new HikariConfig();
        setupConnection(config);
        setupPooling(config);
        return new HikariDataSource(config);
    }

    private void setupConnection(HikariConfig config) {
        String type = mainConfig.data().database().dbType();

        String url, username, password;

        if (type.equalsIgnoreCase("mysql")) {
            //"jdbc:mysql://localhost:3306/simpsons"
            url = MessageFormat.format("jdbc:mysql://{0}:{1}/{2}",
                    mainConfig.data().database().ipDB(),
                    mainConfig.data().database().portDB(),
                    mainConfig.data().database().nameDB());

            username = mainConfig.data().database().usernameDB();
            password = mainConfig.data().database().passwordDB();

            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        } else if (type.equalsIgnoreCase("sqlite")) {
            Path path = plugin.getDataFolder().toPath().resolve("database.sqlite.db");
            url = MessageFormat.format("jdbc:sqlite:{0}", path);
            username = "sa";
            password = "";

            config.setDriverClassName("org.sqlite.JDBC");

        } else {
            throw new UnsupportedOperationException("postrges/other databases are not supported yet.");
        }
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
    }

    private void setupPooling(HikariConfig hikariConfig) {

        hikariConfig.setPoolName("pool");
        hikariConfig.setMaximumPoolSize(6);
        hikariConfig.setMinimumIdle(10);
        hikariConfig.setMaxLifetime(1800000);
        hikariConfig.setConnectionTimeout(5000);
        hikariConfig.setInitializationFailTimeout(-1);
    }
}
