package cn.tesseract.offhandlights;

import cn.tesseract.mycelium.asm.Hook;
import cn.tesseract.mycelium.asm.ReturnCondition;
import com.falsepattern.rple.internal.client.dynlights.ColorDynamicLights;
import com.gtnewhorizons.angelica.dynamiclights.DynamicLights;
import cpw.mods.fml.common.Loader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.lang.reflect.Method;

public class DynamicLightsHook {
    public static Method optifineLightLevel;

    public static int modState = -1;

    static {
        try {
            optifineLightLevel = Class.forName("DynamicLights").getDeclaredMethod("getLightLevel", ItemStack.class);
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

    @Hook(injector = "exit", returnCondition = ReturnCondition.ALWAYS, targetClass = "DynamicLights")
    public static int getLightLevel(Object a, Entity e, @Hook.ReturnValue int l) throws Exception {
        if (e instanceof EntityPlayer p)
            return Math.max(getLightLevel(getOffhandItem(p)), l);
        return l;
    }

    @Hook(injector = "exit", returnCondition = ReturnCondition.ALWAYS)
    public static int getLightLevel(com.falsepattern.falsetweaks.modules.dynlights.base.DynamicLights a, Entity e, @Hook.ReturnValue int l) {
        if (e instanceof EntityPlayer p)
            return Math.max(com.falsepattern.falsetweaks.modules.dynlights.base.DynamicLights.getLightLevel(getOffhandItem(p)), l);
        return l;
    }

    @Hook(injector = "exit", returnCondition = ReturnCondition.ALWAYS)
    public static short getLightLevel(ColorDynamicLights a, Entity e, @Hook.ReturnValue short l) {
        if (l == 0 && e instanceof EntityPlayer p) {
            return ColorDynamicLights.getLightLevel(getOffhandItem(p));
        }
        return l;
    }

    @Hook(injector = "exit", returnCondition = ReturnCondition.ALWAYS)
    public static int getLuminanceFromEntity(DynamicLights c, Entity e, @Hook.ReturnValue int l) {
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
        return (int) optifineLightLevel.invoke(null, k);
    }
}
