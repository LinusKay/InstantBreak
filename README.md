# InstaBreak

A Spigot plugin that allows players to instantly mine/break configured blocks.

```yaml
# only either use_blacklist or use_whitelist should be true
# blacklist is better for disallowing a small amount of blocks
# whitelist is better for disallowing a large amount of blocks
use_blacklist: true
blacklist:
  - ORANGE_WOOL
  - DIAMOND
use_whitelist: false
whitelist:
  - GRASS_BLOCK
  - STONE
# whitelist only certain items
use_item_whitelist: true
item_whitelist:
  - DIAMOND_PICKAXE
# show message to player when whitelisted item is selected
notify_active_item: true
notify_active_item_message: §e§lInstant Break is now active!
# change permission nodes
permission_break: "instantbreak.break"
permission_whitelist_bypass: "instantbreak.whitelist.bypass"
```