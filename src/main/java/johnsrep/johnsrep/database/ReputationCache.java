package johnsrep.johnsrep.database;

import johnsrep.johnsrep.Commands.Reputation;
import org.bukkit.Bukkit;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ReputationCache {
    private final MySQL mysql;

    public ReputationCache(MySQL mysql) {
        this.mysql = mysql;
    }

    HashMap<UUID, Integer> repCache = new HashMap<UUID, Integer>() {
    };

    public void setCache(UUID uuid) throws SQLException {
        Reputation reputation = mysql.getAllFromTableSync(Bukkit.getOfflinePlayer(uuid));
        int sum = 0;
        for (int i = 0; i<=reputation.values.size()-1; i++) {
            sum+=reputation.values.get(i);
        }
        repCache.put(uuid, sum);
    }
    public int getCache(UUID uuid) {
        return repCache.get(uuid);
    }
}
