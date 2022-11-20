package johnsrep.johnsrep.database;

import johnsrep.johnsrep.Commands.Reputation;
import johnsrep.johnsrep.JohnsRep;
import johnsrep.johnsrep.config.Configuration;
import johnsrep.johnsrep.config.MainConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MySQL {

    Configuration<MainConfiguration> config;
    private final JohnsRep plugin;

    private Connection con;

    static ConsoleCommandSender console = Bukkit.getConsoleSender();

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
                console.sendMessage("DecaliumRep: connected to database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                con.close();
                console.sendMessage("DecaliumRep: disconnected from database");
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




    public Reputation getAllFromTableSync(OfflinePlayer playerTo) throws SQLException {
            Reputation reputation = new Reputation();
            PreparedStatement ps = null;
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

}
