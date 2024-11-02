package mods.tesseract.offhandlights;

import com.falsepattern.rple.internal.client.optifine.ColorDynamicLights;
import com.gtnewhorizons.angelica.dynamiclights.DynamicLights;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.tclproject.mysteriumlib.asm.annotations.EnumReturnSetting;
import net.tclproject.mysteriumlib.asm.annotations.Fix;
import net.tclproject.mysteriumlib.asm.annotations.ReturnedValue;

import java.lang.reflect.Method;

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

    @Fix(insertOnExit = true, returnSetting = EnumReturnSetting.ALWAYS, targetClass = "DynamicLights")
    public static int getLightLevel(Object a, Entity e, @ReturnedValue int l) throws Exception {
        if (e instanceof EntityPlayer p)
            return Math.max(getLightLevel(getOffhandItem(p)), l);
        return l;
    }

    @Fix(insertOnExit = true, returnSetting = EnumReturnSetting.ALWAYS)
    public static short getLightLevel(ColorDynamicLights a, Entity e, @ReturnedValue short l) {
        if (e instanceof EntityPlayer p) {
            short d = ColorDynamicLights.getLightLevel(getOffhandItem(p));
            if (d > l)
                return d;
        }
        return l;
    }

    @Fix(insertOnExit = true, returnSetting = EnumReturnSetting.ALWAYS)
    public static int getLuminanceFromEntity(DynamicLights c, Entity e, @ReturnedValue int l) {
        if (modState != 2 && e instanceof EntityPlayer g) {
            ItemStack k = getOffhandItem(g);
            if (k != null)
                return Math.max(l, DynamicLights.getLuminanceFromItemStack(k, e.isInWater()));
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
