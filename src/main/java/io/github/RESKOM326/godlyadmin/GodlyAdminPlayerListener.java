package io.github.RESKOM326.godlyadmin;

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
import org.bukkit.util.RayTraceResult;
import net.kyori.adventure.text.Component;

public class GodlyAdminPlayerListener implements Listener
{
	private final double DAMAGE = 1000000000;
	private final int MAX_DISTANCE = 120;
	
	public GodlyAdminPlayerListener() {}
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
			Entity ent = pl.getTargetEntity(MAX_DISTANCE, true);
			if(ent == null || !(ent instanceof Damageable)) return false;
			World world = pl.getWorld();
			Location loc = ent.getLocation();
			world.strikeLightning(loc);
			((Damageable) ent).damage(DAMAGE);
			world.createExplosion(loc, 5F, true, true);
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
			// Teleport
			Player pl = event.getPlayer();
			Block bck = pl.getTargetBlock(null, 300);
			if(bck == null) return false;
			Location loc = bck.getLocation();
			boolean res = pl.teleport(loc);
			if(!res) return false;
			return true;
		}
		return false;
	}
	
}
