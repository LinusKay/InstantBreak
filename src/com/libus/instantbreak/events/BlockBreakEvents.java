package com.libus.instantbreak.events;

import com.libus.instantbreak.Main;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakEvents implements Listener {

    private final Main plugin;

    private final String permissionBreak;
    private final String permissionBypassWhitelist;
    private final String permissionBypassCooldown;

    private final int cooldown;

    private final List<Player> cooldownList = new ArrayList<>();

    public BlockBreakEvents(Main plugin) {
        this.plugin = plugin;
        this.permissionBreak = this.plugin.getConfig().getString("permission_break");
        this.permissionBypassWhitelist = this.plugin.getConfig().getString("permission_bypass_whitelist");
        this.permissionBypassCooldown = this.plugin.getConfig().getString("permission_bypass_cooldown");
        this.cooldown = plugin.getConfig().getInt("cooldown");
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
        ItemStack breakItem = player.getInventory().getItemInMainHand();
        if (player.hasPermission(permissionBreak)) {
            if (itemAllowedBreak(breakItem)) {
                if (blockAllowedBreak(block) || player.hasPermission(permissionBypassWhitelist)) {
                    BlockBreakEvent breakEvent = new BlockBreakEvent(block, player);
                    plugin.getServer().getPluginManager().callEvent(breakEvent);
                    if (!breakEvent.isCancelled()) {
                        if (!player.hasPermission(permissionBypassCooldown) && cooldown > 0) {
                            if (!cooldownList.contains(player)) {
                                block.breakNaturally();
                                setCooldownTimer(player);
                            }
                        } else {
                            block.breakNaturally();
                        }
                    }
                }
            }
        }
    }

    /**
     * add player to cooldown list and remove after cooldown time
     *
     * @param player player to add to cooldown list
     */
    public void setCooldownTimer(Player player) {
        cooldownList.add(player);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> cooldownList.remove(player), cooldown * 20L);
    }

    /**
     * checks whether block is black/whitelisted
     *
     * @param block the block to check
     * @return true if block is not in blacklist, or is in whitelist, otherwise false
     */
    public boolean blockAllowedBreak(Block block) {
        boolean blacklistTrue = plugin.getConfig().getBoolean("use_block_blacklist");
        boolean whitelistTrue = plugin.getConfig().getBoolean("use_block_whitelist");
        String blockType = block.getType().toString();
        if (blacklistTrue) {
            List<String> blacklist = plugin.getConfig().getStringList("block_blacklist");
            return !blacklist.contains(blockType);
        } else if (whitelistTrue) {
            List<String> whitelist = plugin.getConfig().getStringList("block_whitelist");
            return whitelist.contains(blockType);
        }
        return false;
    }

    /**
     * Check whether item used to break block is whitelisted in config item_whitelist
     *
     * @param item the item to check
     * @return true if item is in whitelist, otherwise false
     */
    public boolean itemAllowedBreak(ItemStack item) {
        List<String> allowedItems = plugin.getConfig().getStringList("item_whitelist");
        String itemType = item.getType().toString();
        return allowedItems.contains(itemType);
    }


}
