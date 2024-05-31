package mods.tesseract.offhandlights;

import cpw.mods.fml.common.Mod;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

@Mod(modid = "offhandlights")
public class OffhandLights extends CustomLoadingPlugin {
    public String[] getASMTransformerClass() {
        return new String[]{FirstClassTransformer.class.getName()};
    }

    public void registerFixes() {
        registerClassWithFixes("mods.tesseract.offhandlights.FixesDynamicLights");
    }
}
