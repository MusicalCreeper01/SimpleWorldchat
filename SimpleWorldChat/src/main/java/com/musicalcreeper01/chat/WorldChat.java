package com.musicalcreeper01.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class WorldChat extends JavaPlugin implements Listener {

	public List<Player> bypass = new ArrayList<Player>();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (bypass.contains(player)) {
			event.getRecipients().clear();
		}
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (bypass.contains(player)) {
				event.getRecipients().add(p);
			} else if (p.getWorld() != player.getWorld()
					&& event.getRecipients().contains(p))
				event.getRecipients().remove(p);
		}
		if (player.getWorld().getPlayers().size() == 1) {
			player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
					+ "No one can hear you!");
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("chat")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				if (args.length > 0) {
					if (args[0].equalsIgnoreCase("bypass")) {
						if(player.hasPermission("worldchat.bypass")){
							if (bypass.contains(player)) {
								player.sendMessage(ChatColor.GOLD
										+ "Bypass Dissabled");
								bypass.remove(player);
							} else {
								player.sendMessage(ChatColor.GOLD
										+ "Bypass Enabled");
								bypass.add(player);
							}
						}else{
							player.sendMessage(ChatColor.RED + "You don't have the permission worldchat.bypass!");
						}
					}else{
						return false;
					}
				}else{
					return false;
				}
			}
			return true;
		}
		return false;
	}
}