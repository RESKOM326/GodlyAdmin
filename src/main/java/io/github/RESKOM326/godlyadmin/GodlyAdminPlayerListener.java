package io.github.RESKOM326.godlyadmin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import io.github.RESKOM326.godlyadmin.utils.*;
import net.kyori.adventure.text.Component;

public class GodlyAdminPlayerListener implements Listener
{
	private final double DAMAGE = 1000000000;
	private Option opts;
	private GodlyAdmin plugin;
	
	private Set<Material> transparent = new HashSet<Material>();
	
	public GodlyAdminPlayerListener(GodlyAdmin plugin)
	{
		this.plugin = plugin;
		opts = new Option();
		this.plugin.getLogger().log(Level.INFO, "GodlyAdmin configuration data correctly processed!");
		logDataInfo();
		transparent.add(Material.AIR);
		transparent.add(Material.WATER);
		transparent.add(Material.GLASS_PANE);
	}
	public boolean fInTheChat(PlayerDeathEvent event) 
	{
		event.deathMessage(Component.text("Press F to pay respects to " + event.getEntity().getName()));
		return true;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public boolean godlySmite(PlayerInteractEvent event)
	{
		boolean godCondition = GodlyAdmin.dataManager.checkGod(event.getPlayer().getPlayerProfile().getId().toString());
		boolean materialCondition = event.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRIDENT;
		boolean clickCondition = event.getAction() == Action.LEFT_CLICK_AIR;
		if(godCondition && materialCondition && clickCondition)
		{
			// SMITE!
			Player pl = event.getPlayer();
			Entity ent = pl.getTargetEntity(opts.getMaxSmiteRange(), true);
			if(ent == null || !(ent instanceof Damageable)) return false;
			World world = pl.getWorld();
			Location loc = ent.getLocation();
			world.strikeLightning(loc);
			((Damageable) ent).damage(DAMAGE);
			world.createExplosion(loc, opts.getSmitePower(), opts.getSmiteFireable(), opts.getSmiteBreakable());
			return true;
			
		}
		return false;
	}
	
	@EventHandler(priority=EventPriority.HIGH)
	public boolean instantTp(PlayerInteractEvent event)
	{
		boolean godCondition = GodlyAdmin.dataManager.checkGod(event.getPlayer().getPlayerProfile().getId().toString());
		boolean materialCondition = event.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRIDENT;
		boolean clickCondition = event.getAction() == Action.RIGHT_CLICK_AIR;
		if(godCondition && materialCondition && clickCondition)
		{
			// Teleport to the targeted block, but never inside the block: teleport to adjacent air
			Player pl = event.getPlayer();
		    List<Block> lastTwoTargetBlocks = pl.getLastTwoTargetBlocks(transparent, opts.getMaxTpRange());
		    if(lastTwoTargetBlocks.size() != 2) return false;
		    Block adjacentBlock = lastTwoTargetBlocks.get(0);
			Location loc = adjacentBlock.getLocation();
			loc.setYaw(pl.getLocation().getYaw());
			loc.setPitch(pl.getLocation().getPitch());
			boolean res = pl.teleport(loc);
			if(!res) return false;
			return true;
		}
		return false;
	}
	
	private void logDataInfo()
	{
		this.plugin.getLogger().log(Level.INFO, "Maximum smite range    --> " + opts.getMaxSmiteRange());
		this.plugin.getLogger().log(Level.INFO, "Maximum teleport range --> " + opts.getMaxTpRange());
		this.plugin.getLogger().log(Level.INFO, "Smite breaks blocks    --> " + opts.getSmiteBreakable());
		this.plugin.getLogger().log(Level.INFO, "Smite power level      --> " + opts.getSmitePower());
		this.plugin.getLogger().log(Level.INFO, "Smite ignites blocks   --> " + opts.getSmiteFireable());
	}
}
