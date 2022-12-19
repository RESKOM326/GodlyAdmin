package io.github.RESKOM326.godlyadmin.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;

import io.github.RESKOM326.godlyadmin.GodlyAdmin;

public class PlainGodsDataManager implements GodsDataManager 
{
	private final GodlyAdmin plugin;
	private File dbfile;
	private static HashMap<String, String> pairs;
	
	public PlainGodsDataManager(GodlyAdmin plugin, String file)
	{
		this.plugin = plugin;
		dbfile = new File(plugin.getDataFolder(), file);
		pairs = new HashMap<String, String>();
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
		populateMap();
		return true;
	}

	@Override
	public boolean addGod(String UUID, String playername) 
	{
		pairs.put(UUID, playername);
		return true;
	}

	@Override
	public boolean quitGod(String UUID) 
	{
		pairs.remove(UUID);
		return true;
	}

	@Override
	public boolean checkGod(String UUID) 
	{
		return pairs.containsKey(UUID);
	}

	@Override
	public boolean closeData() 
	{
		// Dump Map into config file. 1 record per line and format "key,value"
		try
		{
			dbfile.delete();
			dbfile.createNewFile();
			PrintWriter pw = new PrintWriter(dbfile);
			Iterator<Entry<String, String>> it = pairs.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<String, String> keyVal = it.next();
				pw.println(keyVal.getKey() + "," + keyVal.getValue());
			}
			pw.close();
			return true;
		} 
		catch (FileNotFoundException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot save data to .cfg file! -> " + e.getMessage());
			return false;
		}
		catch(IOException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot create .cfg file! -> " + e.getMessage());
			return false;
		}
	}
	
	private void populateMap()
	{
		try {
			RandomAccessFile file = new RandomAccessFile(dbfile, "r");
			String str;

			while((str = file.readLine()) != null)
			{
				String[] keyValue = str.split(",");
				pairs.put(keyValue[0], keyValue[1]);
			}

			file.close();
		} 
		catch (IOException e) 
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot read data from .cfg file! -> " + e.getMessage());
			return;
		}
	}
}
