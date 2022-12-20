package io.github.RESKOM326.godlyadmin;

import org.bukkit.plugin.java.JavaPlugin;

import io.github.RESKOM326.godlyadmin.database.GodsDataManager;
import io.github.RESKOM326.godlyadmin.database.PlainGodsDataManager;

import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import io.github.RESKOM326.godlyadmin.database.SQLGodsDataManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class GodlyAdmin extends JavaPlugin
{
	public static GodsDataManager dataManager;
	public static FileConfiguration config;
	@Override
	public void onEnable() 
	{
		try 
		{
			loadConfig();
			this.getLogger().log(Level.INFO, "GodlyAdmin database data correctly processed!");
			
			Bukkit.getServer().getPluginManager().registerEvents(new GodlyAdminPlayerListener(this), this);
			
			Iterator<String> it = this.getDescription().getCommands().keySet().iterator();
			while(it.hasNext())
			{
				this.getCommand(it.next()).setExecutor(new GodlyAdminCommandExecutor(this));
			}

			this.getLogger().log(Level.INFO, "****************************");
			this.getLogger().log(Level.INFO, "** GodlyAdmin is enabled! **");
			this.getLogger().log(Level.INFO, "****************************");
		} 
		catch(NumberFormatException e) 
		{
			onErrorExit(e.getMessage());
		}
	}
	@Override
	public void onDisable() 
	{
		dataManager.closeData();
		this.getLogger().log(Level.INFO, "****************************");
		this.getLogger().log(Level.INFO, "** GodlyAdmin is enabled! **");
		this.getLogger().log(Level.INFO, "****************************");
	}
	
	private void onErrorExit(String msg)
	{
		this.getLogger().log(Level.SEVERE, msg);
		System.exit(1);
	}
	
	private void loadConfig()
	{
		config = this.getConfig();
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
				onErrorExit("Cannot initialize data!");
			}
		}
		else if(dbtype.equals("FILE")) 
		{
			dataManager = new PlainGodsDataManager(this, "GodlyAdmin.db");
			boolean res = dataManager.initData();
			if(!res)
			{
				onErrorExit("Cannot initialize data!");
			}
		}
		else 
		{
			onErrorExit("No database type was specified!");
		}
	}
}