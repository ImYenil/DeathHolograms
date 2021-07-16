package net.choco.deathholograms.utility;

import org.bukkit.ChatColor;

public class ChatUtils {

    public static String color(String to) {
        return ChatColor.translateAlternateColorCodes('&', to);
    }
}
