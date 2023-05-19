package johnsrep.johnsrep.commands;

import johnsrep.johnsrep.configs.CommandsConfiguration;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import johnsrep.johnsrep.configs.MessagesConfiguration;
import johnsrep.johnsrep.databaseRelated.MySQL;
import johnsrep.johnsrep.databaseRelated.ReputationCache;
import johnsrep.johnsrep.utils.ConfigsHelper;
import johnsrep.johnsrep.utils.CooldownManager;
import johnsrep.johnsrep.utils.ExecuteCommands;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.time.Duration;
import java.util.UUID;

public class ReputationCommand implements CommandExecutor {

    private MySQL mysql;
    private Configuration<MessagesConfiguration> messages;
    private final AnotherPlayerSet anotherPlayerSet;
    private final CheckReputation checkReputation;
    private final ReloadConfigCommand reloadConfigCommand;
    private final ConfigsHelper configsHelper;
    private final ReputationCache reputationCache;
    private final Configuration<MainConfiguration> conf;
    private final Configuration<CommandsConfiguration> commands;
    private final CooldownManager cooldownManager;
    ExecuteCommands executor;

    public ReputationCommand(
            MySQL mysql,
            Configuration<MessagesConfiguration> messages,
            ConfigsHelper configsHelper,
            ReloadConfigCommand reloadConfigCommand,
            ReputationCache reputationCache,
            Configuration<MainConfiguration> conf,
            ExecuteCommands executor,
            Configuration<CommandsConfiguration> commands,
            CheckReputation checkReputation,
            CooldownManager cooldownManager) {

        this.messages = messages;
        this.reloadConfigCommand = reloadConfigCommand;
        this.mysql = mysql;
        this.configsHelper = configsHelper;
        this.reputationCache = reputationCache;
        this.conf = conf;
        this.executor = executor;
        this.commands = commands;
        this.checkReputation = checkReputation;
        this.cooldownManager = cooldownManager;



        anotherPlayerSet = new AnotherPlayerSet(this.mysql, this.messages, this.configsHelper, this.reputationCache, this.conf, this.executor, this.commands);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only");
            return true;
        }

        switch (args.length) {
            case 0:
                if (!sender.hasPermission("rep.checkself")) {
                    this.configsHelper.sendMessage(sender, messages.data().messages().commandNoPermission());
                    return true;
                }
                checkReputation.checkReputation(sender, ((OfflinePlayer) sender).getPlayer().getName());
                break;
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    reloadConfigCommand.reloadConfig();
                    this.configsHelper.sendMessage(sender, messages.data().messages().configReload());
                }
                else if (!sender.hasPermission("rep.checkother")) {
                    this.configsHelper.sendMessage(sender, messages.data().messages().commandNoPermission());
                    return true;
                }
                else checkReputation.checkReputation(sender, args[0]);
                break;
            default:
                if (!sender.hasPermission("rep.set")) {
                    this.configsHelper.sendMessage(sender, messages.data().messages().commandNoPermission());
                    return true;
                }
                try {
                    UUID playerId = ((Player) sender).getUniqueId();
                    Duration timeLeft = cooldownManager.getRemainingCooldown(playerId);

                    if (timeLeft.isZero() || timeLeft.isNegative()) {
                        cooldownManager.setCooldown(
                                playerId,
                                Duration.ofSeconds(conf.data().otherSettings().cooldownToSetReputation()));
                        anotherPlayerSet.setReputation(sender, command,args);
                    }
                    else this.configsHelper.sendMessageWithPlaceholder(
                            sender,
                            messages.data().messages().commandOnCooldown(),
                            Placeholder.parsed("time",
                                    String.valueOf(cooldownManager.getRemainingCooldown(playerId).getSeconds())));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
        }


        return true;
    }
}
