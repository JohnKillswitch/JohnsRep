package johnsrep.johnsrep.Connection;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerEnteringServer implements Listener {


    @EventHandler
    private void onServerJoin(PlayerJoinEvent event) throws IOException {
        Player player = event.getPlayer();
    }
}
