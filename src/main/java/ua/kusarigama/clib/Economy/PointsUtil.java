package ua.kusarigama.clib.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;

public class PointsUtil {
    public void give(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.give(player.getUniqueId(), amount);
        }
    }

    public void take(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.take(player.getUniqueId(), amount);
        }
    }

    public boolean has(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        return api != null && api.look(player.getUniqueId()) >= amount;
    }

    public void set(Player player, Integer amount) {
        PlayerPointsAPI api = getAPI();
        if (api != null) {
            api.set(player.getUniqueId(), amount);
        }
    }

    private PlayerPointsAPI getAPI() {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints") == null) {
            return null;
        }
        return PlayerPoints.getInstance().getAPI();
    }
}
