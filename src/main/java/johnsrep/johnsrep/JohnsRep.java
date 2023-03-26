package johnsrep.johnsrep;

import com.zaxxer.hikari.HikariDataSource;
import johnsrep.johnsrep.commands.ReputationCommand;
import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.databaseRelated.HikariDataSourceCreation;
import johnsrep.johnsrep.handlers.PlayerEnteringServer;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import johnsrep.johnsrep.utils.ExecuteCommands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import space.arim.dazzleconf.ConfigurationOptions;

import java.sql.SQLException;

public final class JohnsRep extends JavaPlugin implements Listener {



    @Override
    public void onEnable() {

        int pluginId = 17673;
        Metrics metrics = new Metrics(this, pluginId);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required to work.");
            Bukkit.getPluginManager().disablePlugin(this);
        }


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

        Configuration<CommandsConfiguration> commands = Configuration.create(
                this,
                "commands.yml",
                CommandsConfiguration.class,
                ConfigurationOptions.defaults());

        conf.reloadConfig();
        messages.reloadConfig();
        commands.reloadConfig();

        HikariDataSourceCreation hikariConnection = new HikariDataSourceCreation(this, conf);
        HikariDataSource hikari = hikariConnection.create();

        MySQL mysql = new MySQL(conf, this, hikari);
        try {
            mysql.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        TagResolver resolver = TagResolver.resolver(
                TagResolver.standard(),
                TagResolver.resolver(
                        "prefix", (queue, ctx) -> Tag.preProcessParsed(messages.data().messages().pluginPrefix())
                )
        );

        ReputationCache reputationCache = new ReputationCache(mysql);
        MiniMessage miniMessage = MiniMessage.builder().tags(resolver).build();

        ExecuteCommands executor = new ExecuteCommands(this, commands, miniMessage);

        this.getCommand("rep").setExecutor(new ReputationCommand(mysql, messages, miniMessage, reputationCache, conf, executor, commands));

        getServer().getPluginManager().registerEvents(new PlayerEnteringServer(reputationCache), this);

        Placeholders placeholders = new Placeholders(mysql, messages, miniMessage, reputationCache, commands, executor);
        placeholders.register();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
