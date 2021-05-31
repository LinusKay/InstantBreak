# InstaBreak

A Spigot plugin that allows players to instantly mine/break configured blocks.
## Features
* White/blacklisting to control which blocks can be instantly broken
* Whitelisting for items players can instantly break blocks with
* Custom permission nodes with bypasses
* Configurable cooldown timer
* Configurable user messages
```yaml
# config.yml

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
# cooldown time between instant block break
cooldown: 5
# change permission nodes
# allow player to instantly break permitted blocks
permission_break: "instantbreak.break"
# allow player to bypass black/whitelist
# this means they will ignore the black/whitelist, letting them break any block
permission_whitelist_bypass: "instantbreak.whitelist.bypass"
# allow player to bypass instant break cooldown
permission_bypass_cooldown: "instantbreak.bypass.cooldown"
```