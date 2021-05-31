package com.libus.instantbreak;

import com.libus.instantbreak.events.BlockBreakEvents;
import com.libus.instantbreak.events.InstantBreakNotify;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        getServer().getPluginManager().registerEvents(new BlockBreakEvents(this), this);
        getServer().getPluginManager().registerEvents(new InstantBreakNotify(this), this);
        saveDefaultConfig();
    }
}
