package mods.tesseract.offhandlights;

import com.gtnewhorizons.angelica.dynamiclights.DynamicLights;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

import java.lang.reflect.Method;

import static com.gtnewhorizons.angelica.dynamiclights.DynamicLights.getLuminanceFromItemStack;

public class FixesDynamicLights {
    public static Method m, n;

    static {
        try {
            m = Class.forName("DynamicLights").getDeclaredMethod("getLightLevel", ItemStack.class);
        } catch (Exception ignored) {
        }
        try {
            n = Class.forName("mods.battlegear2.api.core.InventoryPlayerBattle").getMethod("getCurrentOffhandWeapon");
        } catch (Exception e) {
            try {
                n = Class.forName("mods.battlegear2.api.core.InventoryPlayerBattle").getMethod("getOffhandItem");
            } catch (Exception f) {
                try {
                    n = Class.forName("mods.battlegear2.api.core.IInventoryPlayerBattle").getMethod("battlegear2$getCurrentOffhandWeapon");
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, anotherMethodReturned = "optifine", targetClass = "DynamicLights")
    public static boolean getLightLevel(Object a, Entity e) {
        return e instanceof EntityPlayer;
    }

    @Fix(returnSetting = EnumReturnSetting.ON_TRUE, anotherMethodReturned = "angelica")
    public static boolean getLuminanceFromEntity(DynamicLights c, Entity e) {
        return e instanceof EntityLivingBase;
    }

    public static int optifine(Object a, Entity e) throws Exception {
        EntityPlayer f = (EntityPlayer) e;
        return Math.max(getLightLevel((ItemStack) n.invoke(f.inventory)), Math.max(getLightLevel(f.getHeldItem()), getLightLevel(f.getCurrentArmor(3))));
    }

    public static int angelica(DynamicLights c, Entity e) throws Exception {
        EntityLivingBase f = (EntityLivingBase) e;
        if (f.isBurning())
            return 15;
        int l = 0;
        boolean w = e.isInWater();
        ItemStack k;
        if (f instanceof EntityPlayer g) {
            if ((k = (ItemStack) n.invoke(g.inventory)) != null)
                l = getLuminanceFromItemStack(k, w);
        } else if (f instanceof EntityCreeper h && h.timeSinceIgnited != 0)
            return Math.min(15, h.timeSinceIgnited);
        for (int i = 0; i < 5; i++) {
            if ((k = f.getEquipmentInSlot(i)) != null) {
                l = Math.max(l, getLuminanceFromItemStack(k, w));
            }
        }
        return l;
    }

    public static int getLightLevel(ItemStack k) throws Exception {
        return (int) m.invoke(null, k);
    }
}
