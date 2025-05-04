package cn.tesseract.offhandlights;

import cn.tesseract.mycelium.asm.minecraft.HookLoader;

public class OffhandLights extends HookLoader {
    @Override
    protected void registerHooks() {
        registerHookContainer("cn.tesseract.offhandlights.DynamicLightsHook");
    }
}
