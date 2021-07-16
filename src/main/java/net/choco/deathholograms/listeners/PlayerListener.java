package net.choco.deathholograms.listeners;

import net.choco.deathholograms.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)
    {
        if(e.getEntity().getKiller() instanceof Player)
        {
            Main.getInstance().getHologramManager().manageDeath(e.getEntity(), e.getEntity().getKiller());
        }
    }
}
