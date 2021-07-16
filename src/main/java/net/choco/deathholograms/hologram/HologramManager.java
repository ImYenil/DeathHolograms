package net.choco.deathholograms.hologram;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.choco.deathholograms.Main;
import net.choco.deathholograms.enums.VariableType;
import net.choco.deathholograms.utility.ChatUtils;
import net.choco.deathholograms.utility.compatbridge.model.CompMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class HologramManager {

    Hologram h;

    public void createHologram(Location location) {
        HologramsAPI.createHologram(Main.getInstance(), location);
    }

    public void addLine(String s) {
        h.appendTextLine(s);
    }

    public void deleteHologram() {
        h.delete();
    }

    public void teleportHologram(Location location) {
        h.teleport(location);
    }

    public void addItemLine(ItemStack itemStack) {
        h.appendItemLine(itemStack);
    }

    public void deleteAll()
    {
        HologramsAPI.getHolograms(Main.getInstance()).stream().forEach(Hologram::delete);
    }

    public String getHologramToUse(Player p)
    {
        for(String s: Main.getInstance().getDeathMessages().keySet())
        {
            if(p.hasPermission("deathholograms." + s))
            {
                return s;
            }
        }
        return "ERROR_NO_HOLOGRAM";
    }

    public String applyVars(String s, Player p, Player k)
    {
        for(VariableType vars: VariableType.values())
        {
            s = vars.updateForString(s, p, k, k.getItemInHand());
        }

        return s;
    }

    public void manageDeath(Player p, Player killer)
    {
        String holoToUse = getHologramToUse(p);
        if(!holoToUse.equalsIgnoreCase("ERROR_NO_HOLOGRAM"))
        {
            createHologram(p.getEyeLocation());
            h.teleport(new Location(p.getWorld(), p.getEyeLocation().getX(), p.getEyeLocation().getY() + 1, p.getEyeLocation().getZ()));
            for(String s: Main.getInstance().getDeathMessages().get(holoToUse))
            {
                ItemStack stack = new ItemStack(CompMaterial.PLAYER_HEAD.getMaterial(), 1, (byte)CompMaterial.PLAYER_HEAD.getData());
                SkullMeta sm = (SkullMeta) stack.getItemMeta();
                if(s.equalsIgnoreCase("{playerHead}"))
                {
                    sm.setOwner(p.getName());
                    stack.setItemMeta(sm);
                    addItemLine(stack);
                }
                else if(s.equalsIgnoreCase("{killerHead}"))
                {
                    sm.setOwner(killer.getName());
                    stack.setItemMeta(sm);
                    addItemLine(stack);
                }
                else if(s.equalsIgnoreCase("{killedItemWith}"))
                {
                    if(killer.getItemInHand() == null || killer.getItemInHand().getType().equals(Material.AIR))
                    {
                        addLine("Fist");
                    }
                    else
                    {
                        stack.setType(killer.getItemInHand().getType());
                        addItemLine(stack);
                    }
                }
                else
                {
                    addLine(ChatUtils.color(applyVars(s, p, killer)));
                }
            }

            Main.getInstance().getHolograms().add(Main.getInstance().getHologramManager());
            removeTimer();
        }
    }

    public void removeTimer()
    {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            h.delete();
            deleteHologram();
        }, Main.getInstance().getWaitSeconds()* 20L);
    }
}
