package ua.kusarigama.clib.Economy;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import ua.kusarigama.clib.CLib;

public class VaultUtil {
    private Economy econ;

    public VaultUtil() {
        setEconomy();
    }

    public void give(Player player, Integer amount) {
        econ.depositPlayer(player, amount);
    }

    public void take(Player player, Integer amount) {
        econ.withdrawPlayer(player, amount);
    }

    public boolean has(Player player, Integer amount) {
        return econ.has(player, amount);
    }

    public void set(Player player, Integer amount) {
        double curbalance = econ.getBalance(player);
        econ.withdrawPlayer(player, curbalance);
        econ.depositPlayer(player, amount);
    }

    public double getBalance(Player player) {
        return econ.getBalance(player);
    }

    private void setEconomy() {
        this.econ = CLib.getEconomy();
    }
}