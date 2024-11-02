package mods.tesseract.offhandlights;

import com.falsepattern.rple.internal.client.optifine.ColorDynamicLights;
import cpw.mods.fml.common.Mod;
import net.tclproject.mysteriumlib.asm.common.CustomLoadingPlugin;
import net.tclproject.mysteriumlib.asm.common.FirstClassTransformer;

@Mod(modid = "offhandlights", dependencies = "required-after:mycelium@[1.5.10,)")
public class OffhandLights extends CustomLoadingPlugin {
    public String[] getASMTransformerClass() {
        return new String[]{FirstClassTransformer.class.getName()};
    }

    public void registerFixes() {
        registerClassWithFixes("mods.tesseract.offhandlights.FixesDynamicLights");
    }

}
