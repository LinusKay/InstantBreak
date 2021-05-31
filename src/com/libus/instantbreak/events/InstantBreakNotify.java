package com.libus.instantbreak.events;

import com.libus.instantbreak.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class InstantBreakNotify implements Listener {

    private final Main plugin;
    private final String permissionBreak;

    public InstantBreakNotify(Main plugin) {
        this.plugin = plugin;
        this.permissionBreak = this.plugin.getConfig().getString("permission_break");
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
        if (player.hasPermission(permissionBreak) && showMessage) {
            int slotNumber = event.getNewSlot();
            ItemStack item = player.getInventory().getItem(slotNumber);
            String itemType = item.getType().toString();
            List<String> allowedItems = plugin.getConfig().getStringList("item_whitelist");
            String message = plugin.getConfig().getString("notify_active_item_message");
            if (message != null && allowedItems.contains(itemType)) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(TextComponent.fromLegacyText(message)));
            }
        }
    }
}
