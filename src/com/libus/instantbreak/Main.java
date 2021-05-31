package com.libus.instantbreak;

import com.libus.instantbreak.events.Event;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new Event(this), this);
        saveDefaultConfig();
    }
}
