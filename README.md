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
items:
  - DIAMOND_PICKAXE
```