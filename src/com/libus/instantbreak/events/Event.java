package com.libus.instantbreak.events;

import com.libus.instantbreak.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;

import java.util.List;

public class Event implements Listener {

    private final Main plugin;

    public Event(Main plugin) {
        this.plugin = plugin;
    }

    /**
     * Called when player attempts to damage a block
     * If player is allowed to instant-break the block, attempt to break, otherwise silently ignore
     * break events will be caught by anti-grief/protection plugins
     *
     * @param event BlockDamageEvent
     */
    @EventHandler
    public void playerBreakBlock(BlockDamageEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (canBreak(block)) {
            BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
            plugin.getServer().getPluginManager().callEvent(breakEvent);
            if (!breakEvent.isCancelled()) {
                block.breakNaturally();
            }
        }
    }

    /**
     * checks whether block is black/whitelisted
     *
     * @param block the block to check
     * @return true if block is not in blacklist, or is in whitelist, otherwise false
     */
    public boolean canBreak(Block block) {
        boolean blacklistTrue = plugin.getConfig().getBoolean("use_blacklist");
        boolean whitelistTrue = plugin.getConfig().getBoolean("use_whitelist");
        String blockType = block.getType().toString();
        if (blacklistTrue) {
            List<String> blacklist = plugin.getConfig().getStringList("blacklist");
            return !blacklist.contains(blockType);
        } else if (whitelistTrue) {
            List<String> whitelist = plugin.getConfig().getStringList("whitelist");
            return whitelist.contains(blockType);
        }
        return false;
    }

}
