package mods.tesseract.offhandlights;

import com.gtnewhorizons.angelica.dynamiclights.DynamicLights;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;

import java.lang.reflect.Method;

import static com.gtnewhorizons.angelica.dynamiclights.DynamicLights.getLuminanceFromItemStack;

public class FixesDynamicLights {
    public static Method m;

    public static byte modState;

    static {
        try {
            m = Class.forName("DynamicLights").getDeclaredMethod("getLightLevel", ItemStack.class);
        } catch (Exception ignored) {
        }
        if (Loader.isModLoaded("battlegear2")) {
            try {
                Class.forName("mods.battlegear2.api.core.InventoryPlayerBattle");
                modState = 0;
            } catch (Exception f) {
                modState = 1;
            }
        } else if (Loader.isModLoaded("backhand"))
            modState = 2;
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
        return Math.max(getLightLevel(getOffhandItem(f)), Math.max(getLightLevel(f.getHeldItem()), getLightLevel(f.getEquipmentInSlot(4))));
    }

    public static int angelica(DynamicLights c, Entity e) {
        EntityLivingBase f = (EntityLivingBase) e;
        if (f.isBurning())
            return 15;
        int l = 0;
        boolean w = e.isInWater();
        ItemStack k;
        if (f instanceof EntityPlayer g) {
            if ((k = getOffhandItem(g)) != null)
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

    public static ItemStack getOffhandItem(EntityPlayer p) {
        return switch (modState) {
            case 0 -> ((mods.battlegear2.api.core.InventoryPlayerBattle) p.inventory).getCurrentOffhandWeapon();
            case 1 ->
                ((mods.battlegear2.api.core.IInventoryPlayerBattle) p.inventory).battlegear2$getCurrentOffhandWeapon();
            case 2 -> xonin.backhand.api.core.BackhandUtils.getOffhandItem(p);
            default -> null;
        };
    }

    public static int getLightLevel(ItemStack k) throws Exception {
        return (int) m.invoke(null, k);
    }
}
