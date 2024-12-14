package cn.tesseract.offhandlights;

import cn.tesseract.mycelium.asm.minecraft.HookLoader;
import cpw.mods.fml.common.Mod;

@Mod(modid = "offhandlights", dependencies = "required-after:mycelium@[2.0,)")
public class OffhandLights extends HookLoader {
    @Override
    protected void registerHooks() {
        registerHookContainer("cn.tesseract.offhandlights.DynamicLightsHook");
    }
}
