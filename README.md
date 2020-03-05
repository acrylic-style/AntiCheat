# AntiCheat
Bring anti-cheat system to your server!

## Types of hacks
Currently it can detect these types of hacks:
- Click bot (CivBreak)
- Blink
- Timer
- Fly
- Speed

## Note
- Disable Fly if your plugin uses velocity that launches player to sky.

## Default values
| Type | Default Value | Reason if any |
| ---- | ---- | ---- |
| Blink | 40 packets/s | 20 should be safe because ticks in 1 seconds are 20. But the lag breaks this. |
| Fly (Vertical | 12 | Cannot go beyond this point without using velocity (using plugin) that goes up or using hack |
| Click bot | 35 | 35 is basically impossible without using a hack or 2 fingers. |
| Speed | 30 | It may not detect speed (Since it's slower than fly), so it's for fly hack detection. |
