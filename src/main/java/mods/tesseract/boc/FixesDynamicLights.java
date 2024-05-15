package mods.tesseract.boc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

import java.lang.reflect.Method;

public class FixesDynamicLights {
    public static Method m, n;

    static {
        try {
            m = Class.forName("DynamicLights").getDeclaredMethod("getLightLevel", ItemStack.class);
            try {
                n = Class.forName("mods.battlegear2.api.core.InventoryPlayerBattle").getMethod("getCurrentOffhandWeapon");
            } catch (Exception e) {
                try {
                    n = Class.forName("mods.battlegear2.api.core.InventoryPlayerBattle").getMethod("getOffhandItem");
                } catch (Exception f) {
                    n = Class.forName("mods.battlegear2.api.core.IInventoryPlayerBattle").getMethod("battlegear2$getCurrentOffhandWeapon");
                }
            }
        } catch (Exception ignored) {
        }
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, anotherMethodReturned = "p", targetClass = "DynamicLights")
    public static boolean getLightLevel(Object a, Entity e) {
        return e instanceof EntityPlayer;
    }

    public static int p(Object a, Entity e) throws Exception {
        EntityPlayer f = (EntityPlayer) e;
        return Math.max(getLightLevel((ItemStack) n.invoke(f.inventory)), Math.max(getLightLevel(f.getHeldItem()), getLightLevel(f.getCurrentArmor(3))));
    }

    public static int getLightLevel(ItemStack k) throws Exception {
        return (int) m.invoke(null, k);
    }
}
