package tk.mapzcraft.firesofhades.SwissBook;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
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
				event.getPlayer().sendMessage(
						"You have been given a manual, READ IT!!!");
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPickupItem(PlayerPickupItemEvent event) {
		ArrayList<String> om = new ArrayList<String>();
		om.addAll(plugin.manual.getStringList("oldManuals"));
		if(plugin.manual.contains("Manual")){
		om.add(((BookMeta) plugin.manual.getItemStack("Manual").getItemMeta())
				.getTitle().toString());
		}
		int x = 0;
		while (om.size() > x) {
			if (event.getItem().getItemStack().getType().equals(Material.WRITTEN_BOOK)) {
				if (((BookMeta) event.getItem().getItemStack().getItemMeta())
						.getTitle().toString().equals(om.get(x).toString())) {
					event.setCancelled(true);
					event.getPlayer().sendMessage("you can not pickup manuals, to get a new manual type: /manual");
				    event.getItem().remove();
				}
			}
			x = x + 1;
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityInteract(PlayerInteractEntityEvent event) {
		if(event.getRightClicked().getType().equals(EntityType.VILLAGER)){
		ArrayList<String> om = new ArrayList<String>();
		om.addAll(plugin.manual.getStringList("oldManuals"));
		om.add(((BookMeta) plugin.manual.getItemStack("Manual").getItemMeta())
				.getTitle().toString());

		int x = 0;
		ItemStack[] invarray = event.getPlayer().getInventory().getContents();
		while (invarray.length > x) {
			if (invarray[x] != null) {
				if (invarray[x].getType().equals(Material.WRITTEN_BOOK)) {
					int omt = 0;
					while (om.size() > omt) {
						if (((BookMeta) invarray[x].getItemMeta()).getTitle()
								.toString().equals(om.get(omt))) {
							event.setCancelled(true);
							event.getPlayer()
									.sendMessage(
											"you can not trade with villagers while carrying a manual");
						}
						omt = omt + 1;
					}
				}
			}
			x = x + 1;
		}
		}

	}
}
