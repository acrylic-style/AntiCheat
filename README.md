# AntiCheat
Bring anti-cheat system to your server!

## Types of hacks
Currently it can detect these types of hacks:
- Click bot
- Blink, Timer
- Fly, HighJump(does not detect on low height) (Off by default)
- Speed, Fly, Timer (Off by default)

## Note
- Disable Fly if your plugin uses velocity that launches player to sky.

## Default values
| Type | Default Value |  |
| ---- | ---- | ---- |
| Blink | 60 packets/s | 20 should be safe because ticks in a second is 20. But the lag breaks this. |
| Fly (Vertical) | 17 blocks/s | Cannot go beyond this point without using velocity (using plugin) that goes up or using hack |
| Click bot | 30 cps | 30 is basically impossible without using a hack or 2 fingers. |
| Speed | 14 blocks/s | It may not detect speed (Since it's slower than fly), so it's for fly hack detection. |
