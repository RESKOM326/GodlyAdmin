package io.github.RESKOM326.godlyadmin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;


public class GodlyAdminCommandExecutor implements CommandExecutor 
{
	private final GodlyAdmin plugin;
	
	public GodlyAdminCommandExecutor(GodlyAdmin plugin) 
	{
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		// TODO Auto-generated method stub
		boolean result = false;
		if(command.getName().equalsIgnoreCase("ignite")) 
		{
			result = ignite(sender, args);
		}
		else if(command.getName().equalsIgnoreCase("hideme")) 
		{
			result = hideme(sender, args);
		}
		else if(command.getName().equalsIgnoreCase("unhideme")) 
		{
			result = unhideme(sender, args);
		}
		else if(command.getName().equalsIgnoreCase("ascend")) 
		{
			result = ascend(sender, args);
		}
		else if(command.getName().equalsIgnoreCase("mortalize"))
		{
			result = mortalize(sender, args);
		}
		return result;
	}
	
	private boolean ignite(CommandSender sender, String[] args) 
	{
		try 
		{
			if(args.length != 2) 
			{
				return false;
			}
			Player target = Bukkit.getServer().getPlayer(args[0]);
			if(target == null) 
			{
				sender.sendMessage("Player "+ args[0] + " is offline or does not exist");
				return true;
			}
			target.setFireTicks(20 * Integer.parseInt(args[1]));
			Bukkit.getServer().sendMessage(Component.text(args[0] + " is being purified"));
			return true;				
		} 
		catch(IllegalArgumentException e) 
		{
			System.err.print("Bad username");
			return false;
		}
	}
	
	private boolean hideme(CommandSender sender, String[] args) 
	{
		try 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("This command must be issued by a player");
				return true;
			}
			Player p = (Player) sender;
			if(args.length == 0) 
			{
				for(Player targets : Bukkit.getServer().getOnlinePlayers()) 
				{
					if(!targets.canSee(p)) 
					{
						continue;
					}
					targets.hidePlayer(plugin, p);
				}
				return true;
			}
			else if(args.length > 0) 
			{
				for(int i = 0; i < args.length; i++) 
				{
					Player target = Bukkit.getServer().getPlayer(args[i]);
					if(target == null) 
					{
						sender.sendMessage("Player " + args[i] + " is offline or does not exist");
						continue;
					}
					if(!target.canSee(p)) 
					{
						continue;
					}
					target.hidePlayer(plugin, p);
				}
				return true;
			}				
		} 
		catch(IllegalArgumentException e) 
		{
			System.err.print("Bad username");
		}
		return false;
	}
	
	private boolean unhideme(CommandSender sender, String[] args) 
	{
		try 
		{
			if(!(sender instanceof Player)) 
			{
				sender.sendMessage("This command must be issued by a player");
				return true;
			}
			Player p = (Player) sender;
			if(args.length == 0) 
			{
				for(Player targets : Bukkit.getServer().getOnlinePlayers()) 
				{
					if(targets.canSee(p)) 
					{
						continue;
					}
					targets.showPlayer(plugin, p);
				}
				return true;
			}
			else if(args.length > 0) 
			{
				for(int i = 0; i < args.length; i++) 
				{
					Player target = Bukkit.getServer().getPlayer(args[i]);
					if(target == null) 
					{
						sender.sendMessage("Player " + args[i] + " is offline or does not exist");
						continue;
					}
					if(target.canSee(p)) 
					{
						continue;
					}
					target.showPlayer(plugin, p);
				}
				return true;
			}
		} 
		catch(IllegalArgumentException e) 
		{
			System.err.print("Bad username");
		}
		return false;
	}
	
	private boolean ascend(CommandSender sender, String[] args) 
	{
		if(args.length != 1)
		{
			return false;
		}
		Player target = Bukkit.getServer().getPlayer(args[0]);
		if(target == null)
		{
			sender.sendMessage("Player " + args[0] + " is offline or does not exist");
			return false;
		}
		PlayerProfile ppr = target.getPlayerProfile();
		return GodlyAdmin.dataManager.addGod(ppr.getId().toString(), ppr.getName());
	}
	
	private boolean mortalize(CommandSender sender, String[] args)
	{
		if(args.length != 1)
		{
			return false;
		}
		Player target = Bukkit.getServer().getPlayer(args[0]);
		if(target == null)
		{
			sender.sendMessage("Player " + args[0] + " is offline or does not exist");
			return false;
		}
		PlayerProfile ppr = target.getPlayerProfile();
		return GodlyAdmin.dataManager.quitGod(ppr.getId().toString());
	}
}
