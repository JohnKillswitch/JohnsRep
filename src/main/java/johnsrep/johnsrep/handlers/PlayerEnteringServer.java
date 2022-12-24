package johnsrep.johnsrep.handlers;

import johnsrep.johnsrep.databaseRelated.ReputationCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;

public class PlayerEnteringServer implements Listener {

    private final ReputationCache reputationCache;

    public PlayerEnteringServer(ReputationCache reputationCache) {
        this.reputationCache = reputationCache;
    }


    @EventHandler
    private void onServerJoin(AsyncPlayerPreLoginEvent event) throws SQLException {
        reputationCache.setCache(event.getUniqueId());

    }
}
