package net.choco.deathholograms.enums;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public enum VariableType {

    KILLER("killer"),

    PLAYER("player"),

    REMAININGHEALTH("remainingHealth");

    String variable;

    VariableType(String variable)
    {
        this.variable = "{" + variable + "}";
    }

    public String updateForString(String s, Player p, Player k, ItemStack stack)
    {
        String toUpdate = null;
        if(equals(KILLER))
        {
            toUpdate = k.getName();
        }
        else if(equals(PLAYER))
        {
            toUpdate = p.getName();
        }
        else if(equals(REMAININGHEALTH))
        {
            toUpdate = ((int) (k.getHealth()/2)) + "";
        }

        return s.replace(variable, toUpdate);
    }
}
