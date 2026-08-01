package sircow.sunshinegrace;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class SunshineGrace {
    public SunshineGrace(FMLJavaModLoadingContext context) {
        CommonClass.init();
    }
}
