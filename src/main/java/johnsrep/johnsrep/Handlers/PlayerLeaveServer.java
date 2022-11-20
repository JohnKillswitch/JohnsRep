package johnsrep.johnsrep.Handlers;

import johnsrep.johnsrep.database.ReputationCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class PlayerLeaveServer {


    private final ReputationCache reputationCache;

    public PlayerLeaveServer(ReputationCache reputationCache) {
        this.reputationCache = reputationCache;
    }

    @EventHandler
    private void onServerLeave(PlayerQuitEvent event) throws SQLException {
        reputationCache.setCache(event.getPlayer().getUniqueId());

    }
}
