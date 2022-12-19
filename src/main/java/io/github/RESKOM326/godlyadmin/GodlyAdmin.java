package io.github.RESKOM326.godlyadmin;

import org.bukkit.plugin.java.JavaPlugin;
import io.github.RESKOM326.godlyadmin.database.GodsDataManager;
import java.io.File;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import io.github.RESKOM326.godlyadmin.database.SQLGodsDataManager;
import org.bukkit.Bukkit;
//import org.bukkit.command.Command;
//import org.bukkit.command.PluginCommand;
//import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.configuration.file.FileConfiguration;

public class GodlyAdmin extends JavaPlugin
{
	static GodsDataManager dataManager;
	@Override
	public void onEnable() 
	{
		try 
		{
			this.saveDefaultConfig();
			FileConfiguration config = this.getConfig();
			String dbtype = config.getString("database.type").toUpperCase(Locale.ENGLISH);
			if(dbtype.equals("MYSQL")) 
			{
				String port = config.getString("database.port");
				if(!port.matches("^\\d+$")) 
				{
					throw new NumberFormatException();
				}
				String hostname = config.getString("database.host");
				String schema = config.getString("database.schema");
				String username = config.getString("database.username");
				String passwd = config.getString("database.password");
				dataManager = new SQLGodsDataManager(this, hostname, port, schema, username, passwd);
				boolean res = dataManager.initData();
				if(!res)
				{
					this.getLogger().log(Level.SEVERE, "Cannot initialize data!");
					System.exit(1);
				}
			}
			else if(dbtype.equals("FILE")) 
			{
				new File(this.getDataFolder(), "config.yml");
			}
			else 
			{
				this.getLogger().log(Level.SEVERE, "No database type was specified!");
				System.exit(1);
			}
			
			Bukkit.getServer().getPluginManager().registerEvents(new GodlyAdminPlayerListener(), this);
			
			// Get list of commands from config.yml and set Executor
			Iterator<String> it = this.getDescription().getCommands().keySet().iterator();
			while(it.hasNext())
			{
				this.getCommand(it.next()).setExecutor(new GodlyAdminCommandExecutor(this));
			}

			this.getLogger().log(Level.INFO, "***************************");
			this.getLogger().log(Level.INFO, "GodlyAdmin is enabled!");
			this.getLogger().log(Level.INFO, "***************************");
		} 
		catch(NumberFormatException e) 
		{
			this.getLogger().log(Level.SEVERE, "Database port is not a number");
			System.exit(1);
		}
	}
	@Override
	public void onDisable() 
	{
		dataManager.closeData();
		this.getLogger().log(Level.INFO, "***************************");
		this.getLogger().log(Level.INFO, "GodlyAdmin is disabled!");
		this.getLogger().log(Level.INFO, "***************************");
	}
}