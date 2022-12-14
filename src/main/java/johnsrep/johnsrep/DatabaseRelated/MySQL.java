package johnsrep.johnsrep.DatabaseRelated;

import johnsrep.johnsrep.Commands.Reputation;
import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.Configs.Configuration;
import johnsrep.johnsrep.Configs.MainConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

public class MySQL {

    Configuration<MainConfiguration> config;
    private final JohnsRep plugin;

    private Connection con;

    public MySQL(Configuration<MainConfiguration> conf, JohnsRep plugin) {
        config = conf;
        this.plugin = plugin;

    }

    public void connect() {
        if (!isConnected()) {
            try {
                con = DriverManager.getConnection("jdbc:mysql://" +
                        config.data().database().ipDB() + ":" +
                        config.data().database().portDB() + "/" +
                        config.data().database().nameDB(),
                        config.data().database().usernameDB(),
                        config.data().database().passwordDB());
                plugin.getLogger().log(Level.FINE,"DecaliumRep: connected to database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                plugin.getLogger().log(Level.FINE,"DecaliumRep: disconnected from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        return (con != null);
    }

    public Connection getConnection() {
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

        PreparedStatement ps1 = getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS CachedPlayers (" +
                "UUID VARCHAR(100)," +
                "Value INTEGER," +
                "UNIQUE(`UUID`))");
        ps1.executeUpdate();
    }

    public void insertInTable(OfflinePlayer playerTo, OfflinePlayer playerFrom, int repValue, String repComment) throws SQLException {
        PreparedStatement ps = getConnection().prepareStatement("INSERT INTO " +
                "Data (`UUIDto`, `UUIDfrom`, `Value`, `Comment`) " +
                "VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE `Value`=VALUES(`Value`), `Comment`=VALUES(`Comment`)");
        ps.setString(1, playerTo.getUniqueId().toString());
        ps.setString(2, playerFrom.getUniqueId().toString());
        ps.setInt(3, repValue);
        ps.setString(4, repComment);
        ps.executeUpdate();
    }
    public void getSumFromTable(OfflinePlayer player, MySQLCallback2 callback) {
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
