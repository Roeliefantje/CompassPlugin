package me.Roeliefantje.CompassPlugin;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	
	
	public Player compassTarget = null;
	@Override
	public void onEnable() {
		//startup
		//reloads
		//plugin reloads
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			public void run() { setPlayerLocation();}
		},20, 20);
	}
	
	@Override
	public void onDisable() {
		//shutdown
		//reloads
		//plugin reloads
	}
	
	public boolean onCommand(CommandSender usr, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("Focus")) {
			if (usr instanceof Player) {
				Player p = (Player) usr;
				Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
				if(args.length == 1) {
					//Set target to player
					String targetName = args[0];
					Player target = null;
					for (Player player : onlinePlayers) {
						if(targetName.equals(player.getName())) {
							compassTarget = player;
							target = player;
							break;
						}
					}
					if (target != null) {
						Location focus = target.getLocation();
						p.setCompassTarget(focus);
						p.sendMessage("All compasses will now point to: " + args[0]);
						return true;
					} else {
						p.sendMessage(ChatColor.RED + "Player is not online!");
					}
					
				} else if (args.length == 3) {
					//Set target to Location
					Double x = Double.valueOf(args[0]);
					Double y = Double.valueOf(args[1]);
					Double z = Double.valueOf(args[2]);
					World w = p.getWorld();
					compassTarget = null;
					Location xyz = new Location(w, x, y, z);
					p.setCompassTarget(xyz);
					return true;
				} else {
					p.sendMessage(ChatColor.RED + "Wrong usage of command!");
				}
				
			}
		}
		
		if (label.equalsIgnoreCase("compass")) {
			if (usr instanceof Player) {
				usr.sendMessage(ChatColor.RED + "Compass added to your inventory");
				Player p = (Player) usr;
				PlayerInventory inv = p.getInventory();
				inv.addItem(new ItemStack(Material.COMPASS));
				return true;
			} else {
				usr.sendMessage("Mate I cant give the server a compass");
				return true;
			}
		}
		
		
		return false;
	}
	
	public void setPlayerLocation() {
//		Point the compass to the player, but only if this player exists, and is in the same world.
		if (compassTarget != null) {
			Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
			for(Player p : onlinePlayers) {
				if(p.getWorld() == compassTarget.getWorld()) {
					p.setCompassTarget(compassTarget.getLocation());
				}
			}
		}
	}
}
