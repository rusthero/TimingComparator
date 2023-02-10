package dev.rusthero.timingcomparator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class TimingComparatorMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("TimingComparator");

    @Override
    public void onInitialize() {
        LOGGER.info("Mod initialized");
    }
}
