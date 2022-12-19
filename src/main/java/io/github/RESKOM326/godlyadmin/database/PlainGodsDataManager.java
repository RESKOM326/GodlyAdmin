package io.github.RESKOM326.godlyadmin.database;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import io.github.RESKOM326.godlyadmin.GodlyAdmin;

public class PlainGodsDataManager implements GodsDataManager 
{
	private final GodlyAdmin plugin;
	private File dbfile;
	
	public PlainGodsDataManager(GodlyAdmin plugin, File dbfile)
	{
		this.plugin = plugin;
		this.dbfile = dbfile;
	}
	@Override
	public boolean initData() 
	{
		if(!dbfile.exists())
		{
			try 
			{
				if(dbfile.createNewFile()) return true;
				return false;
			} 
			catch (IOException e) {
				this.plugin.getLogger().log(Level.SEVERE, "Cannot initialize .cfg data! -> " + e.getMessage());
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addGod(String UUID, String playername) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean quitGod(String UUID) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkGod(String UUID) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean closeData() 
	{
		// TODO Auto-generated method stub
		return false;
	}

}
