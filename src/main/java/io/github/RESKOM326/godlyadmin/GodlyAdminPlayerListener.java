package io.github.RESKOM326.godlyadmin;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import net.kyori.adventure.text.Component;

public class GodlyAdminPlayerListener implements Listener
{
	
	public GodlyAdminPlayerListener() {}
	public boolean fInTheChat(PlayerDeathEvent event) 
	{
		event.deathMessage(Component.text("Press F to pay respects to " + event.getEntity().getName()));
		return true;
	}
	
}
