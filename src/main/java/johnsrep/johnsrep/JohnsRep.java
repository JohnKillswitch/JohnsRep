package johnsrep.johnsrep;

import johnsrep.johnsrep.Commands.ReputationCommand;
import johnsrep.johnsrep.Handlers.PlayerEnteringServer;
import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MainConfiguration;
import johnsrep.johnsrep.config.MessagesConfiguration;
import johnsrep.johnsrep.database.MySQL;
import johnsrep.johnsrep.database.ReputationCache;
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

        int pluginId = 16918;
        Metrics metrics = new Metrics(this, pluginId);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getPluginManager().registerEvents(this, this);
        } else {
            getLogger().warning("Could not find PlaceholderAPI! This plugin is required.");
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

        conf.reloadConfig();
        messages.reloadConfig();

        MySQL mysql = new MySQL(conf, this);
        mysql.connect();
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
        this.getCommand("rep").setExecutor(new ReputationCommand(mysql, messages, miniMessage, reputationCache, conf));

        getServer().getPluginManager().registerEvents(new PlayerEnteringServer(reputationCache), this);

        Placeholders placeholders = new Placeholders(mysql, messages, miniMessage, reputationCache);
        placeholders.register();
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
