package sircow.sunshinegrace;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import sircow.sunshinegrace.config.ForgeConfig;

@Mod(Constants.MOD_ID)
public class ForgeSunshineGrace {
    public ForgeSunshineGrace(FMLJavaModLoadingContext context) {
        CommonClass.init();
        ForgeConfig.loadServer();
    }
}
