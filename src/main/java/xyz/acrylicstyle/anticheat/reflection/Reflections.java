package xyz.acrylicstyle.anticheat.reflection;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;
import util.ReflectionHelper;
import util.reflect.Ref;

public class Reflections {
    public static boolean checkPlayerInteractEvent_getHand() {
        return ReflectionHelper.findMethod(PlayerInteractEvent.class, "getHand") != null;
    }

    @Nullable
    public static EquipmentSlot getHand(PlayerInteractEvent e) {
        if (checkPlayerInteractEvent_getHand()) {
            return (EquipmentSlot) Ref.getMethod(PlayerInteractEvent.class, "getHand").invoke(e);
        } else return null;
    }

    public static boolean isGliding(Player player) {
        try {
            return (boolean) Ref.getMethod(Player.class, "isGliding").invoke(player);
        } catch (Exception e) {
            return false;
        }
    }
}
