package io.github.RESKOM326.godlyadmin.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import io.github.RESKOM326.godlyadmin.GodlyAdmin;

public class SQLGodsDataManager implements GodsDataManager
{
	private final GodlyAdmin plugin;
	private static Connection conn = null;
	private final String username;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	
	public SQLGodsDataManager(GodlyAdmin plugin, String hostname, String port, String database, String username, String password)
	{
		this.plugin = plugin;
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	@Override
	public boolean initData()
	{
		try
		{
			connectDB();
			String sql = "CREATE TABLE IF NOT EXISTS godhood(UUID VARCHAR(36) NOT NULL, playername VARCHAR(45) NOT NULL, PRIMARY KEY(UUID));";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot initialize SQL data! -> " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean addGod(String UUID, String playername)
	{
		try
		{
			connectDB();
			String sql = "SELECT UUID FROM godhood WHERE UUID = (?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, UUID);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				this.plugin.getLogger().log(Level.INFO, "Player is already a god!");
				return false;
			}
			sql = "INSERT INTO godhood(UUID, playername) VALUES (?, ?);";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, UUID);
			ps2.setString(2, playername);
			ps2.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.WARNING, "Player could not be ascended! -> " + e.getMessage());
			return false;
		}
	}

	@Override
	public boolean quitGod(String UUID)
	{
		try
		{
			connectDB();
			String sql = "SELECT UUID FROM godhood WHERE UUID = (?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, UUID);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
			{
				this.plugin.getLogger().log(Level.INFO, "Player is not registered as a God!");
				return false;
			}
			sql = "DELETE FROM godhood WHERE UUID = ?;";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, UUID);
			ps2.executeUpdate();
			return true;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.WARNING, "Player could not be mortalized! -> " + e.getMessage());
			return false;
		}
	}
	
	@Override
	public boolean closeData()
	{
		try
		{
			conn.close();
			conn = null;
			return true;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot close connection to SQL database! -> " + e.getMessage());
			return false;
		}
		catch(NullPointerException e)
		{
			return true;
		}
	}
	
	@Override
	public boolean checkGod(String UUID)
	{
		try
		{
			connectDB();
			String sql = "SELECT UUID FROM godhood WHERE UUID = (?);";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, UUID);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
			{
				return true;
			}
			return false;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.WARNING, "Cannot check god status! -> " + e.getMessage());
			return false;
		}
	}
	
	private void connectDB()
	{
		try
		{
			if(conn != null) return;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database, this.username, this.password);
			return;
		}
		catch(ClassNotFoundException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "JDBC Driver not found! -> " + e.getMessage());
			return;
		}
		catch(SQLException e)
		{
			this.plugin.getLogger().log(Level.SEVERE, "Cannot connect to SQL database! -> " + e.getMessage());
			return;
		}
	}
}
