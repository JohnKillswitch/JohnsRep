package johnsrep.johnsrep.databaseRelated;

import com.zaxxer.hikari.HikariDataSource;
import johnsrep.johnsrep.commands.Reputation;
import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.configs.Configuration;
import johnsrep.johnsrep.configs.MainConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

import java.sql.*;
import java.util.UUID;

public class MySQL {

    private Configuration<MainConfiguration> config;
    private final JohnsRep plugin;

    private Connection con;
    private HikariDataSource ds;


    public MySQL(Configuration<MainConfiguration> conf, JohnsRep plugin, HikariDataSource ds) {
        this.config = conf;
        this.plugin = plugin;
        this.ds = ds;

    }

//    public void connect() throws SQLException {
//        if (!isConnected()) {
//            ds = new HikariDataSource();
//            //"jdbc:mysql://localhost:3306/simpsons"
//            ds.setJdbcUrl("jdbc:mysql://localhost:3306/simpsons");
//            ds.setJdbcUrl("jdbc:mysql://" +
//                    config.data().database().ipDB() + ":" +
//                    config.data().database().portDB() + "/" +
//                    config.data().database().nameDB());
//
//            ds.setUsername(config.data().database().usernameDB());
//            ds.setPassword(config.data().database().passwordDB());
//            con = ds.getConnection();
//            plugin.getLogger().log(Level.FINE,"JohnsRep: connected to database");
//        }
//    }

//    public void disconnect() {
//        if (isConnected()) {
//            try {
//                con.close();
//                plugin.getLogger().log(Level.FINE,"JohnsRep: disconnected from database");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public boolean isConnected() {
        return (con != null);
    }

    public Connection getConnection() throws SQLException {
        con = ds.getConnection();
        return con;
    }
    public void createTable() throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Data (" +
                "UUIDto VARCHAR(100)," +
                "UUIDfrom VARCHAR(100)," +
                "Value INTEGER," +
                "Comment VARCHAR(100)," +
                "UNIQUE(`UUIDto`, `UUIDfrom`))");
        ps.executeUpdate();
        con.close();

        PreparedStatement ps1 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS CachedPlayers (" +
                "UUID VARCHAR(100)," +
                "Value INTEGER," +
                "UNIQUE(`UUID`))");
        ps1.executeUpdate();
        con.close();
    }

    public void insertInTable(OfflinePlayer playerTo, OfflinePlayer playerFrom, int repValue, String repComment) throws SQLException {
        if (config.data().database().dbType().equalsIgnoreCase("mysql")) {
            PreparedStatement ps = getConnection().prepareStatement("INSERT INTO " +
                    "Data (`UUIDto`, `UUIDfrom`, `Value`, `Comment`) " +
                    "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `Value`=VALUES(`Value`), `Comment`=VALUES(`Comment`)");
            ps.setString(1, playerTo.getUniqueId().toString());
            ps.setString(2, playerFrom.getUniqueId().toString());
            ps.setInt(3, repValue);
            ps.setString(4, repComment);
            ps.executeUpdate();
            con.close();
        }
        else {
            PreparedStatement ps = getConnection().prepareStatement("INSERT OR REPLACE INTO " +
                    "Data (`UUIDto`, `UUIDfrom`, `Value`, `Comment`) " +
                    "VALUES (?, ?, ?, ?)");
            ps.setString(1, playerTo.getUniqueId().toString());
            ps.setString(2, playerFrom.getUniqueId().toString());
            ps.setInt(3, repValue);
            ps.setString(4, repComment);
            ps.executeUpdate();
            con.close();
        }
    }
    public void getSumFromTable(OfflinePlayer player, MySQLCallback2 callback) throws SQLException {
        Server server = plugin.getServer();
        server.getScheduler().runTaskAsynchronously(plugin, () -> {
            PreparedStatement ps = null;
            try {
                ps = getConnection().prepareStatement("SELECT SUM(Value) FROM Data WHERE UUIDto = ?");
                ps.setString(1, player.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                rs.next();
                int sum = rs.getInt(1);
                callback.returnData(sum);
                con.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }

    public Reputation getAllFromTableSync(OfflinePlayer playerTo) throws SQLException {
            Reputation reputation = new Reputation();
            PreparedStatement ps;
            try {
                ps = getConnection().prepareStatement("SELECT Value, Comment, UUIDfrom FROM Data WHERE UUIDto = ?");

                ps.setString(1, playerTo.getUniqueId().toString());
                ResultSet rs = ps.executeQuery();
                reputation.player = playerTo;
                while (rs.next()) {
                    reputation.fromPlayer.add(Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("UUIDfrom"))));
                    reputation.values.add(rs.getInt("Value"));
                    reputation.comments.add(rs.getString("Comment"));
                }
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return reputation;

    }

    public void getAllFromTable(OfflinePlayer playerTo, MySQLCallback callback) throws SQLException {
        Server server = plugin.getServer();
        server.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Reputation reputation = getAllFromTableSync(playerTo);
                callback.returnData(reputation);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

    }

    public interface MySQLCallback {
        void returnData(Reputation reputation) throws SQLException;
    }
    public interface MySQLCallback2 {
        void returnData(int sum) throws SQLException;
    }

}
