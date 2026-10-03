package sircow.sunshinegrace;

import sircow.sunshinegrace.platform.Services;

public class CommonClass {
    public static void init() {
        if (Services.PLATFORM.isModLoaded("sunshinegrace") && !Services.PLATFORM.getPlatformName().equals("Paper")) {
            Constants.LOG.info("Initialising " + Constants.MOD_NAME);
        }
    }
}
