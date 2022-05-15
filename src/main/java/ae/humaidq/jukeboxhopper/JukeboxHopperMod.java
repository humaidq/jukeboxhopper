package ae.humaidq.jukeboxhopper;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JukeboxHopperMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("jukeboxhopper");

	@Override
	public void onInitialize() {
		LOGGER.info("Initialised Jukebox Hopper Mod!");
	}
}

