package johnsrep.johnsrep.databaseRelated;

import org.bukkit.Bukkit;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ReputationCache {
    private final MySQL mysql;

    public ReputationCache(MySQL mysql) {
        this.mysql = mysql;
    }

    HashMap<UUID, Integer> repCache = new HashMap<UUID, Integer>() {};

    public void setCache(UUID uuid) throws SQLException {
        mysql.getSumFromTable(Bukkit.getOfflinePlayer(uuid), sum -> {
            repCache.put(uuid, sum);
        });
    }
    public int getCache(UUID uuid) {
        return repCache.get(uuid);
    }
}
