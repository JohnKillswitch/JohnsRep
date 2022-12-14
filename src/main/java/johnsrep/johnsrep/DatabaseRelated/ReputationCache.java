package johnsrep.johnsrep.DatabaseRelated;

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

//  OLD VERSION (shit btw)
//        Reputation reputation = mysql.getAllFromTable(Bukkit.getOfflinePlayer(uuid), reputation1 -> {
//
//        });
//        int sum = 0;
//        for (int i = 0; i<=reputation.values.size()-1; i++) {
//            sum+=reputation.values.get(i);
//        }
    }
    public int getCache(UUID uuid) {
        return repCache.get(uuid);
    }
}
