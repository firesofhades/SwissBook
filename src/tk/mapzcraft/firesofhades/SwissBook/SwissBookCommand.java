package tk.mapzcraft.firesofhades.SwissBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.google.common.collect.Lists;

public class SwissBookCommand implements CommandExecutor {
	public SwissBook plugin;

	public SwissBookCommand(SwissBook plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		HashSet<Byte> t = new HashSet<Byte>();
		t.add((byte) 0);
		Player player = Bukkit.getPlayer(sender.getName());
		if ((label.toString().equalsIgnoreCase("manual"))
				&& player.hasPermission("swissbook.manual")) {
			if (plugin.manual.getItemStack("Manual") == null) {
				sender.sendMessage("no manual found!");
				return true;
			}
			ItemStack manual = plugin.manual.getItemStack("Manual");
			manual.setAmount(1);
			if (args.length == 0 || args.length == 1) {
				if (args.length == 1
						&& player.hasPermission("swissbook.manual.other")) {

					if (Bukkit.getPlayer(args[0]) != null) {
						if (Bukkit.getPlayer(args[0]).isOnline()) {
							player = Bukkit.getPlayer(args[0].toString());
						} else {
							sender.sendMessage("That player is not online");
							return true;
						}
					} else {
						sender.sendMessage("That player is not found");
						return true;
					}
				}
				Inventory i = player.getInventory();

				i.addItem(manual);
				player.updateInventory();
				if (args.length == 1) {
					sender.sendMessage("A manual has been given to "
							+ player.getName().toString());
					player.sendMessage("You have been given a manual by "
							+ sender.getName().toString());
				} else {
					sender.sendMessage("a manual has been placed in your inventory!");
				}
				return true;
			}

		}
		if (((label.toString().equalsIgnoreCase("swissbook"))
				|| (label.toString().equalsIgnoreCase("sb")))&&(args[0].equalsIgnoreCase("toggle"))){
			if(!plugin.aMode.contains(player.getName().toString())){
			plugin.aMode.add(player.getName().toString());
			player.sendMessage("swissbook: advanced mode enabled");
			return true;
			}
			else{
				plugin.aMode.remove(player.getName().toString());
				player.sendMessage("swissbook: advanced mode disabled");
				return true;
			}
		}
		if ((!player.getItemInHand().getType().equals(Material.BOOK_AND_QUILL)
				&& !player.getItemInHand().getType().equals(Material.WRITTEN_BOOK))|| !(sender instanceof Player)) {
			sender.sendMessage("Please take a book in your hand!");
			return true;
		}
		if ((label.toString().equalsIgnoreCase("swissbook"))
				|| (label.toString().equalsIgnoreCase("sb"))
				&& ((player.getItemInHand().getType()
						.equals(Material.BOOK_AND_QUILL)) || (player
						.getItemInHand().getType()
						.equals(Material.WRITTEN_BOOK)))) {
			ItemStack book = player.getItemInHand();
			String pageContent = "";
			String newContent = "";
			String gOutput = "";
			String fString = "";
			Integer page = 0;
			Integer bChar = 0;
			Integer fChar = 0;
			Integer page1 = 0;
			Integer bChar1 = 0;
			Integer fChar1 = 0;
			Integer interval = 10;
			BookMeta bm = (BookMeta) book.getItemMeta();
			ArrayList<String> pages = new ArrayList<String>();
			pages.add("");
			Boolean preview = false;
			char lf = 10;
			char olf = 126;
			char ospc = 124;
			char spc = 32;
			if (args[args.length - 1].equalsIgnoreCase("preview")) {
				preview = true;
			}

			if (args[0].equalsIgnoreCase("get")
					&& (args.length == 4 || args.length == 5)) {
				if ((!(sender.hasPermission("swissbook.get")) && (player
						.getItemInHand().getType()
						.equals(Material.BOOK_AND_QUILL)))
						|| ((!(sender.hasPermission("swissbook.get.signed")) && (player
								.getItemInHand().getType()
								.equals(Material.WRITTEN_BOOK))))) {
					sender.sendMessage("No permission!");
					return true;
				}
				try {
					fChar = Integer.parseInt(args[1]);
					bChar = Integer.parseInt(args[2]);
					page = Integer.parseInt(args[3]);
					if (args.length == 5) {
						interval = Integer.parseInt(args[4]);
						if (interval < 1) {
							sender.sendMessage("Please use an interval larger than 0");
							return true;
						}

					}

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}

				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Page can not be found");
						return true;
					}
				} else {
					sender.sendMessage("Page can not be found");
					return true;
				}
				pageContent = pages.get(page - 1);
				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal. ");
					return true;
				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}
				if (fChar == bChar) {
					gOutput = gOutput + ChatColor.AQUA + "(" + fChar.toString()
							+ ")" + ChatColor.WHITE
							+ pageContent.substring(fChar - 1, bChar);
				} else {
					while (fChar < bChar) {
						if (fChar + (interval - 1) > bChar) {
							gOutput = gOutput
									+ ChatColor.AQUA
									+ "("
									+ fChar.toString()
									+ ")"
									+ ChatColor.WHITE
									+ pageContent.substring(fChar - 1, fChar
											+ (bChar - fChar));
						} else {
							gOutput = gOutput
									+ ChatColor.AQUA
									+ "("
									+ fChar.toString()
									+ ")"
									+ ChatColor.WHITE
									+ pageContent.substring(fChar - 1, fChar
											+ (interval - 1));
						}

						if ((fChar + interval) <= bChar) {
							fChar = fChar + interval;
						} else {
							fChar = fChar + (bChar - fChar);
						}
					}
				}
				gOutput = gOutput + ChatColor.AQUA + "(" + (fChar + 1) + ")"
						+ ChatColor.WHITE;
				sender.sendMessage("Requested text reads: " + gOutput);
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("replaceall"))
					&& ((args.length == 4) || (args.length == 5))&&!plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.replace")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.replace.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
												
					
					fString = args[1];
					fString = fString.replace(ospc, spc);
					fString = fString.replace(olf, lf);
					newContent = args[2];
					newContent = newContent.replace(ospc, spc);
					newContent = newContent.replace(olf, lf);
					page = Integer.parseInt(args[3]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);

				if (!pageContent.contains(fString)) {
					player.sendMessage("could not find the text to replace: "+ fString);
					return true;
				}
				
					newContent = pageContent.replaceAll(fString, newContent);
			
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text replaced!");
					return true;
				}
			}
			
			if ((args[0].equalsIgnoreCase("replace"))
					&& ((args.length == 4) || (args.length == 5))&&!plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.replace")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.replace.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
												
					
					fString = args[1];
					fString = fString.replace(ospc, spc);
					fString = fString.replace(olf, lf);
					newContent = args[2];
					newContent = newContent.replace(ospc, spc);
					newContent = newContent.replace(olf, lf);
					page = Integer.parseInt(args[3]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);

				if (!pageContent.contains(fString)) {
					player.sendMessage("could not find the text to replace: "+ fString);
					return true;
				}
				
					newContent = pageContent.replace(fString, newContent);
			
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text replaced!");
					return true;
				}
			}
			
			
			if ((args[0].equalsIgnoreCase("replace"))
					&& ((args.length == 5) || (args.length == 6))&&plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.replace")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.replace.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
												
					
					fChar = Integer.parseInt(args[1]);
					bChar = Integer.parseInt(args[2]);
					page = Integer.parseInt(args[3]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				newContent = args[4];
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);

				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal. ");
					return true;

				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}
				if (fChar == 1) {
					newContent = newContent + pageContent.substring(bChar);
				} else {
					newContent = pageContent.substring(0, fChar - 1)
							+ newContent + pageContent.substring(bChar);
				}
				newContent = newContent.replace(ospc, spc);
				newContent = newContent.replace(olf, lf);
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text replaced!");
					return true;
				}
			}
			if ((player.getItemInHand().getType().equals(Material.WRITTEN_BOOK))
					&& (args[0].equalsIgnoreCase("sign")) && (args.length == 2)) {
				if (!(sender.hasPermission("swissbook.sign"))) {
					sender.sendMessage("No permission!");
					return true;
				}
				bm.setAuthor(args[1]);
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				book.setItemMeta(bm);
				sender.sendMessage("Author changed!");
				return true;
			}
			if ((player.getItemInHand().getType().equals(Material.WRITTEN_BOOK))
					&& (args[0].equalsIgnoreCase("title"))
					&& (args.length >= 2)) {
				if (!(sender.hasPermission("swissbook.title"))) {
					sender.sendMessage("No permission!");
					return true;
				}
				int x = 1;
				while (x < args.length) {
					newContent = newContent + args[x] + " ";
					x = x + 1;
				}
				x = newContent.length();
				newContent = newContent.substring(0, x - 1);
				bm.setTitle(newContent);
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				book.setItemMeta(bm);
				sender.sendMessage("Title changed!");
				return true;
			}
			if (args[0].equalsIgnoreCase("copyr")
					&& ((args.length == 7) || (args.length == 8))&&plugin.aMode.contains(player.getName().toString())) {
				if ((!(sender.hasPermission("swissbook.copyr")) && (player
						.getItemInHand().getType()
						.equals(Material.BOOK_AND_QUILL)))
						|| ((!(sender.hasPermission("swissbook.copyr.signed")) && (player
								.getItemInHand().getType()
								.equals(Material.WRITTEN_BOOK))))) {
					sender.sendMessage("No permission!");
					return true;
				}

				try {
					fChar = Integer.parseInt(args[1]);
					bChar = Integer.parseInt(args[2]);
					page = Integer.parseInt(args[3]);
					fChar1 = Integer.parseInt(args[4]);
					bChar1 = Integer.parseInt(args[5]);
					page1 = Integer.parseInt(args[6]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);
				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal.");
					return true;
				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}
				newContent = pageContent.substring(fChar - 1, bChar);

				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page1 - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page1 - 1);
				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal.");
					return true;
				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}

				if (fChar1 == 1) {
					newContent = newContent + pageContent.substring(bChar1);
				} else {
					newContent = pageContent.substring(0, fChar1 - 1)
							+ newContent + pageContent.substring(bChar1);
				}
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				}
				pages.set(page1 - 1, newContent);
				bm.setPages(pages);
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				book.setItemMeta(bm);
				sender.sendMessage("Text copied and replaced!");
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("insert"))
					&& ((args.length == 4) || (args.length == 5))&&!plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.insert")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.insert.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					newContent = (args[1]);
					newContent = newContent.replace(ospc, spc);
					newContent = newContent.replace(olf, lf);
					page = Integer.parseInt(args[2]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				fString = args[3];
				fString = fString.replace(ospc, spc);
				fString = fString.replace(olf, lf);
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (bm.getPageCount() < page) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page) {

							pages.add("");
						}
					}
				} else {
					if (bm.getPageCount() < page) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page) {
							pages.add("");
						}
					}
				}
				pageContent = pages.get(page - 1);
				if (!pageContent.contains(fString)) {
					player.sendMessage("could not find the text to insert after: "+ fString);
					return true;
				}

			
					newContent = ((pageContent.split(fString))[0]+fString+newContent);
					int x = 1;
				while((pageContent.split(fString)).length>x){
					newContent = newContent+pageContent.split(fString)[x];
					x = x+1;
				}

				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text inserted!");
					return true;
				}
			}

			
			
			if ((args[0].equalsIgnoreCase("insert"))
					&& ((args.length == 4) || (args.length == 5))&&plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.insert")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.insert.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					fChar = Integer.parseInt(args[1]);
					page = Integer.parseInt(args[2]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				newContent = args[3];

				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (bm.getPageCount() < page) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page) {

							pages.add("");
						}
					}
				} else {
					if (bm.getPageCount() < page) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page) {
							pages.add("");
						}
					}
				}
				pageContent = pages.get(page - 1);
				if (fChar < 1) {
					sender.sendMessage("Make sure you'r variables are legal.");
					return true;
				}

				if (fChar == 1) {
					newContent = newContent + pageContent;
				}

				if (fChar >= 2 && fChar <= pageContent.length()) {
					newContent = pageContent.substring(0, fChar - 1)
							+ newContent
							+ pageContent.substring(fChar - 1,
									pageContent.length());
				}

				if (fChar > pageContent.length()) {

					while (fChar - 1 > pageContent.length()) {
						pageContent = pageContent + " ";
					}
					newContent = pageContent + newContent;

				}
				newContent = newContent.replace(ospc, spc);
				newContent = newContent.replace(olf, lf);
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text inserted!");
					return true;
				}
			}

			if (args[0].equalsIgnoreCase("copy")
					&& ((args.length == 6) || (args.length == 7))&&plugin.aMode.contains(player.getName().toString())) {
				if ((!(sender.hasPermission("swissbook.copy")) && (player
						.getItemInHand().getType()
						.equals(Material.BOOK_AND_QUILL)))
						|| ((!(sender.hasPermission("swissbook.copy.signed")) && (player
								.getItemInHand().getType()
								.equals(Material.WRITTEN_BOOK))))) {
					sender.sendMessage("No permission!");
					return true;
				}

				try {
					fChar = Integer.parseInt(args[1]);
					bChar = Integer.parseInt(args[2]);
					page = Integer.parseInt(args[3]);
					fChar1 = Integer.parseInt(args[4]);
					page1 = Integer.parseInt(args[5]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;
				}
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);
				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal.");
					return true;
				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}
				newContent = pageContent.substring(fChar - 1, bChar);

				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (bm.getPageCount() < page1) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page1) {

							pages.add("");
						}
					}
				} else {
					if (bm.getPageCount() < page1) {
						if ((pages.size() + page) > 500) {
							sender.sendMessage("Exceeding page limit!");
							return true;
						}
						while (pages.size() < page1) {
							pages.add("");
						}
					}
				}
				pageContent = pages.get(page1 - 1);

				if (fChar1 < 1) {
					sender.sendMessage("Make sure you'r variables are legal. Character locations can not be 0. The first location can not exceed the current length of the text. The 2nd location can not be infront of the 1st");
					return true;
				}

				if (fChar1 == 1) {
					newContent = newContent + pageContent;
				}

				if (fChar1 >= 2 && fChar1 <= pageContent.length()) {
					newContent = pageContent.substring(0, fChar1 - 1)
							+ newContent
							+ pageContent.substring(fChar1 - 1,
									pageContent.length());
				}

				if (fChar1 > pageContent.length()) {

					while (fChar1 - 1 > pageContent.length()) {
						pageContent = pageContent + " ";
					}
					newContent = pageContent + newContent;

				}

				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				}
				pages.set(page1 - 1, newContent);
				bm.setPages(pages);
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				book.setItemMeta(bm);
				sender.sendMessage("Text copied!");
				return true;
			}
			
			if ((args[0].equalsIgnoreCase("delete"))
					&& ((args.length == 3) || (args.length == 4))&&!plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.delete")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.delete.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					fString = args[1];
					fString = fString.replace(ospc, spc);
					fString = fString.replace(olf, lf);
					page = Integer.parseInt(args[2]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}
				newContent = "";
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);

				
					newContent = (pageContent.split(fString))[0]+(pageContent.split(fString))[1];
			int x = 2;
			while((pageContent.split(fString)).length>x){
				newContent = newContent+fString+pageContent.split(fString)[x];
				x = x+1;
			}
		
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text deleted!");
					return true;
				}
			}

			
			if ((args[0].equalsIgnoreCase("delete"))
					&& ((args.length == 4) || (args.length == 5))&&plugin.aMode.contains(player.getName().toString())) {
				try {
					if ((!(sender.hasPermission("swissbook.delete")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.delete.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					fChar = Integer.parseInt(args[1]);
					bChar = Integer.parseInt(args[2]);
					page = Integer.parseInt(args[3]);

				} catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}
				newContent = "";
				if (bm.hasPages()) {
					pages = Lists.newArrayList(bm.getPages());
					if (pages.size() < page - 1) {
						sender.sendMessage("Can not find page!");
						return true;
					}
				} else {
					sender.sendMessage("Can not find page!");
					return true;
				}
				pageContent = pages.get(page - 1);

				if (bChar < fChar || fChar < 1 || fChar > pageContent.length()) {
					sender.sendMessage("Make sure you'r variables are legal.");
					return true;
				}
				if (bChar > pageContent.length()) {
					bChar = pageContent.length();
				}
				if (fChar == 1) {
					newContent = newContent + pageContent.substring(bChar);
				} else {
					newContent = pageContent.substring(0, fChar - 1)
							+ newContent + pageContent.substring(bChar);
				}
				newContent = newContent.replace(ospc, spc);
				newContent = newContent.replace(olf, lf);
				pages.set(page - 1, newContent);
				if (preview) {
					sender.sendMessage(newContent + "(PREVIEW!!!)");
					return true;
				} else {
					player.getInventory().remove(
							player.getInventory().getHeldItemSlot());
					bm.setPages(pages);
					book.setItemMeta(bm);
					sender.sendMessage("Text deleted!");
					return true;
				}
			}

			if ((args[0].equalsIgnoreCase("addp"))
					&& ((args.length == 1) || (args.length == 2))) {
				try {
					if ((!(sender.hasPermission("swissbook.page.add")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.page.add.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					page = 1;
					if (args.length == 2) {
						page = Integer.parseInt(args[1]);
					}
					if (bm.hasPages()) {
						pages = Lists.newArrayList(bm.getPages());
					} else {
						sender.sendMessage("Can not find page!");
						return true;
					}
				}

				catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}
				if ((pages.size() + page) > 500) {
					sender.sendMessage("Exceeding page limit!");
					return true;
				}
				while (page > 0) {
					pages.add("");
					page = page - 1;
				}
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				bm.setPages(pages);
				book.setItemMeta(bm);
				sender.sendMessage("Page(s) added!");
				return true;
			}
			if ((args[0].equalsIgnoreCase("insertp"))
					&& ((args.length == 2) || (args.length == 3))) {
				try {
					if ((!(sender.hasPermission("swissbook.page.insert")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.page.insert.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					page = 1;
					if (args.length == 3) {
						page = Integer.parseInt(args[2]);
					}
					fChar = Integer.parseInt(args[1]);
					if (bm.hasPages()) {
						pages = Lists.newArrayList(bm.getPages());
						if (pages.size() < page - 1) {
							sender.sendMessage("Can not find page!");
							return true;
						}
					} else {
						sender.sendMessage("Can not find page!");
						return true;
					}
				}

				catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}
				if ((pages.size() + page) > 500) {
					sender.sendMessage("Exceeding page limit!");
					return true;
				}
				while (page > 0) {
					pages.add(fChar - 1, "");
					page = page - 1;
				}
				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				bm.setPages(pages);
				book.setItemMeta(bm);
				sender.sendMessage("Page(s) inserted!");
				return true;
			}
			if ((args[0].equalsIgnoreCase("movep")) && (args.length == 3)) {
				try {
					if ((!(sender.hasPermission("swissbook.page.move")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.page.move.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}

					page = Integer.parseInt(args[1]);

					fChar = Integer.parseInt(args[2]);
					if (bm.hasPages()) {
						pages = Lists.newArrayList(bm.getPages());
						if (pages.size() < page - 1) {
							sender.sendMessage("Can not find page!");
							return true;
						}
					} else {
						sender.sendMessage("Can not find page!");
						return true;
					}
				}

				catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}

				newContent = pages.get(page - 1);
				pages.remove(page - 1);
				pages.add(fChar - 1, newContent);

				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				bm.setPages(pages);
				book.setItemMeta(bm);
				sender.sendMessage("Page moved!");
				return true;
			}
			if ((args[0].equalsIgnoreCase("deletep"))
					&& ((args.length == 3) || (args.length == 2))) {
				try {
					if ((!(sender.hasPermission("swissbook.page.delete")) && (player
							.getItemInHand().getType()
							.equals(Material.BOOK_AND_QUILL)))
							|| ((!(sender
									.hasPermission("swissbook.page.delete.signed")) && (player
									.getItemInHand().getType()
									.equals(Material.WRITTEN_BOOK))))) {
						sender.sendMessage("No permission!");
						return true;
					}
					page = Integer.parseInt(args[1]);
					if (args.length == 3) {
						fChar = Integer.parseInt(args[2]);
					}
					if (bm.hasPages()) {
						pages = Lists.newArrayList(bm.getPages());
						if (pages.size() < page - 1) {
							sender.sendMessage("Can not find page!");
							return true;
						}
					} else {
						sender.sendMessage("Can not find page!");
						return true;
					}
				}

				catch (NumberFormatException e) {
					sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
					return true;

				}
				if (args.length == 3 && page >= fChar) {
					sender.sendMessage("Can not find page!");
					return true;
				}
				if (args.length == 3) {
					while (page <= fChar) {
						pages.remove(page - 1);
						fChar = fChar - 1;
					}
				} else {
					pages.remove(page - 1);
				}

				player.getInventory().remove(
						player.getInventory().getHeldItemSlot());
				bm.setPages(pages);
				book.setItemMeta(bm);
				sender.sendMessage("Page(s) deleted!");
				return true;
			}
			if ((args[0].equalsIgnoreCase("more"))
					&& ((args.length == 1) || (args.length == 2))) {
				if (!(sender.hasPermission("swissbook.more"))) {
					sender.sendMessage("No permission!");
					return true;
				}
				if(!(player.getItemInHand().equals(Material.WRITTEN_BOOK))){return true;}
				if (args.length == 2) {
					try {
						Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage("Failed parsing, make sure you are using numbers where neccesary");
						return true;
					}
				}
				Integer x = player.getInventory().getItemInHand().getAmount() + 1;
				if (args.length == 2) {
					x = player.getInventory().getItemInHand().getAmount()
							+ Integer.parseInt(args[1]);
				}
				player.getInventory().getItemInHand().setAmount(x);
				sender.sendMessage("Book(s) added!");
				return true;
			}

			if (args[0].equalsIgnoreCase("setm") && (args.length == 1)) {
				if (!(sender.hasPermission("swissbook.setmanual"))) {
					sender.sendMessage("No permission!");
					return true;
				}
				if (player.getItemInHand().getType()
						.equals(Material.WRITTEN_BOOK)) {

					plugin.manual.set("Manual", player.getItemInHand());
					try {
						plugin.manual.save(plugin.manualFile);
					} catch (IOException e) {
						e.printStackTrace();
					}

					sender.sendMessage("Manual set!");
					return true;

				}
			}
			if (args[0].equalsIgnoreCase("unsign")) {
				if (!(sender.hasPermission("swissbook.unsign"))) {
					sender.sendMessage("No permission!");
					return true;
				}
				if (!book.getType().equals(Material.WRITTEN_BOOK)
						|| (!((BookMeta) book.getItemMeta()).getAuthor()
								.toString().equalsIgnoreCase(player.getName()
								.toString())&&!(sender.hasPermission("swissbook.unsign.other")))) {
					sender.sendMessage("You are not holding a written book you signed!");
					return true;
				}
				bm.setAuthor("");
				bm.setTitle("");
				book.setItemMeta(bm);
				book.setType(Material.BOOK_AND_QUILL);
				sender.sendMessage("Book unsigned!");
				return true;
			}
		}
		if (!plugin.aMode.contains(player.getName().toString())){
		sender.sendMessage("Please read the documentation. advanced mode is currently: disabled");
		return true;
		}
		if (plugin.aMode.contains(player.getName().toString())){
			sender.sendMessage("Please read the documentation. advanced mode is currently: enabled");
			return true;
			}
			sender.sendMessage("Please read the documentation.");
	return true;}

}
