package johnsrep.johnsrep.commands;

import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;

public class Reputation {

    public OfflinePlayer player;
    public List<String> comments;
    public List<Integer> values;
    public List<OfflinePlayer> fromPlayer;

    public Reputation() {
        comments = new ArrayList<String>();
        values = new ArrayList<Integer>();
        fromPlayer = new ArrayList<OfflinePlayer>();

    }
}

