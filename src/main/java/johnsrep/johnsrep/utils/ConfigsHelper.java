package johnsrep.johnsrep.utils;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;

public class ConfigsHelper {
    private final MiniMessage miniMessage;
    public ConfigsHelper (MiniMessage miniMessage) {
        this.miniMessage = miniMessage;
    }
    public void sendMessage (CommandSender receiver, String message) {
        receiver.sendMessage(miniMessage.deserialize(message));
    }

    public void sendMessageWithPlaceholder (CommandSender receiver, String message, TagResolver... resolvers) {
        receiver.sendMessage(miniMessage.deserialize(message, resolvers));
    }
}
