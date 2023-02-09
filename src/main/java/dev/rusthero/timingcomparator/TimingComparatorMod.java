package dev.rusthero.timingcomparator;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimingComparatorMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("TimingComparator");

    @Override
    public void onInitialize() {
        LOGGER.info("Mod initialized");
    }
}
