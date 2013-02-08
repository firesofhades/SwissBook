package tk.mapzcraft.firesofhades.SwissBook;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class SwissBook extends JavaPlugin {
	public static final Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		getCommand("SwissBook").setExecutor(new SwissBookCommand(this));
	}

	public void onDisable() {

	}
}
