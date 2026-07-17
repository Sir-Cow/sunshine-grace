package sircow.sunshinegrace;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sircow.sunshinegrace.effect.ForgeModEffects;

@Mod(Constants.MOD_ID)
public class SunshineGrace {
    public SunshineGrace(FMLJavaModLoadingContext context) {
        var modBusGroup = context.getModBusGroup();

        ForgeModEffects.init(modBusGroup);
        CommonClass.init();
    }
}
