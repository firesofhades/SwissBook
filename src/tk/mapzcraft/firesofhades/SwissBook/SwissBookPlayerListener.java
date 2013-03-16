package tk.mapzcraft.firesofhades.SwissBook;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

public class SwissBookPlayerListener implements Listener {
	public SwissBook plugin;

	public SwissBookPlayerListener(SwissBook plugin) {
		this.plugin = plugin;
	}

	Chest b;
	BlockVector c1;

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerJoinEvent event) {
		if (!(event.getPlayer().hasPlayedBefore())) {
			if (plugin.manual.getItemStack("Manual") != null) {
				ItemStack manual = plugin.manual.getItemStack("Manual");
				manual.setAmount(1);
				Inventory i = event.getPlayer().getInventory();

				i.addItem(manual);
				event.getPlayer().updateInventory();
				event.getPlayer().sendMessage("You have been given a manual, READ IT!!!");
			}
		}
	}
}
