package com.libus.instantbreak.events;

import com.libus.instantbreak.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Event implements Listener {

    private final Main plugin;

    private final String permissionBreak;
    private final String permissionBypassWhitelist;

    public Event(Main plugin) {
        this.plugin = plugin;
        this.permissionBreak = this.plugin.getConfig().getString("permission_break");
        this.permissionBypassWhitelist = this.plugin.getConfig().getString("permission_whitelist_bypass");
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
                        block.breakNaturally();
                    }
                }
            }
        }
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
        if (allowedItems.contains(itemType)) {
            return true;
        }
        return false;
    }


    /**
     * Send message to player letting them know their item may be used for instant break
     *
     * @param event PlayerItemHeldEvent runs when player changes selected item
     */
    @EventHandler
    public void notifySelectedItemAllowed(PlayerItemHeldEvent event) {
        boolean showMessage = plugin.getConfig().getBoolean("notify_active_item");
        Player player = event.getPlayer();
        if(player.hasPermission(permissionBreak) && showMessage) {
            int slotNumber = event.getNewSlot();
            ItemStack item = player.getInventory().getItem(slotNumber);
            String itemType = item.getType().toString();
            List<String> allowedItems = plugin.getConfig().getStringList("item_whitelist");
            String message = plugin.getConfig().getString("notify_active_item_message");
            if (allowedItems != null && allowedItems.contains(itemType)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(TextComponent.fromLegacyText(message)));
            }
        }
    }

}
