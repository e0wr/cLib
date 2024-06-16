package ua.kusarigama.clib;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import ua.kusarigama.clib.Economy.PointsUtil;
import ua.kusarigama.clib.Economy.VaultUtil;
import ua.kusarigama.clib.Holograms.HologramsUtil;
import ua.kusarigama.clib.Text.TextUtil;

public final class CLib extends JavaPlugin {
    public PointsUtil pointsUtil;
    public VaultUtil vaultUtil;
    public HologramsUtil hologramsUtil;
    public TextUtil textUtil;

    private static Economy econ = null;
    private static CLib instance;

    @Override
    public void onEnable() {
        instance = this;

        //Economy
        pointsUtil = new PointsUtil();
        vaultUtil = new VaultUtil();

        //Holograms
        hologramsUtil = new HologramsUtil();

        //Text
        textUtil = new TextUtil();

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Выключен, отсутствует Vault!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public PointsUtil getPointsUtil() {
        return this.pointsUtil;
    }

    public VaultUtil getVaultUtil() {
        return this.vaultUtil;
    }

    public HologramsUtil getHologramsUtil() {
        return this.hologramsUtil;
    }

    public TextUtil getTextUtil() {
        return this.textUtil;
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static CLib getInstance() {
        return instance;
    }
}
