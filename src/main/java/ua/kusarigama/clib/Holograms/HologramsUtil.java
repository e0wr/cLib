package ua.kusarigama.clib.Holograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import org.bukkit.Location;

import java.util.List;

public class HologramsUtil {
    public void createHologram(String name, Location loc) {
        if(DHAPI.getHologram(name) != null)
            return;

        DHAPI.createHologram(name, loc);
    }

    public void createHologramList(String name, Location location, List<String> lines){
        if(DHAPI.getHologram(name) != null)
            return;

        DHAPI.createHologram(name, location, lines);
    }

    public void removeHologram(String hologram) {
        DHAPI.removeHologram(hologram);
    }

    public void addHologramLine(Hologram hologram, String line) {
        DHAPI.addHologramLine(hologram, line);
    }

    public Hologram getHologram(String name) {
        return DHAPI.getHologram(name);
    }

    public HologramPage getHologramPage(Hologram hologram, int page) {
        return DHAPI.getHologramPage(hologram, page);
    }
}
