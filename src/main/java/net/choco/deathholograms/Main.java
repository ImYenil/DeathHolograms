package net.choco.deathholograms;

import lombok.Getter;
import net.choco.deathholograms.hologram.HologramManager;
import net.choco.deathholograms.listeners.PlayerListener;
import net.choco.deathholograms.manager.FileManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private FileManager fileManager;

    @Getter
    private HologramManager hologramManager;

    @Getter
    private static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "AutoLapis" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;

    @Getter
    private HashMap<String,List<String>> deathMessages = new HashMap<>();

    @Getter
    private ArrayList<HologramManager> holograms = new ArrayList<>();

    @Getter
    private int waitSeconds;

    @Override
    public void onEnable() {

        instance = this;

        if(Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays"))
        {
            hologramManager = new HologramManager();
        }
        fileManager = new FileManager(this);
        fileManager.getConfig("config.yml").copyDefaults(true).save();
        long startTime = System.currentTimeMillis();

        setup();

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        log(LOG_LEVEL.INFO, "The plugin has been activated (" + (System.currentTimeMillis() - startTime) / 1000.0 + "s)");

    }

    @Override
    public void onDisable() {

        HandlerList.unregisterAll(this);

        getServer().getScheduler().cancelTasks(this);

        log(LOG_LEVEL.INFO, "The plugin has been disabled");
    }

    public void setup()
    {
        deathMessages.clear();
        for(String s: getFileManager().getConfig("config.yml").get().getConfigurationSection("Holograms").getValues(false).keySet())
        {
            List<String> values = (List<String>) getFileManager().getConfig("config.yml").get().getList("Holograms." + s);
            deathMessages.put(s, values);
        }

        waitSeconds = getFileManager().getConfig("config.yml").get().getInt("hologram-length");
    }

    public static void log(LOG_LEVEL level, String text) {
        getInstance().getServer().getConsoleSender().sendMessage(getPREFIX() + " " + ChatColor.DARK_GRAY + "[" + level.getName() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + text);
    }

    public enum LOG_LEVEL
    {
        INFO("INFO", 0, ChatColor.GREEN + "INFO"),
        WARNING("WARNING", 1, ChatColor.YELLOW + "WARNING"),
        ERROR("ERROR", 2, ChatColor.RED + "ERROR"),
        DEBUG("DEBUG", 3, ChatColor.AQUA + "DEBUG");

        @Getter
        private String name;

        private LOG_LEVEL(String s, int n, String name) {
            this.name = name;
        }
    }
}
