package io.github.RESKOM326.godlyadmin.database;

public interface GodsDataManager
{
	/**
	 * Initialize gods data. Could be creating the SQL Server tables or files storing information
	 * @return True if could initialize data. False otherwise
	 */
	public boolean initData();
	/**
	 * Grants a player the rank of God
	 * @param UUID UUID of the player
	 * @param playername Username of the player
	 * @return True if could grant godhood to player. False otherwise
	 */
	public boolean addGod(String UUID, String playername);
	/**
	 * Revokes a player the rank of God
	 * @param UUID UUID of the player
	 * @return True if could revoke godhood from player. False otherwise
	 */
	public boolean quitGod(String UUID);
	/**
	 * Checks if a player has God status
	 * @param UUID UUID of the player
	 * @return True if the player is a God. False otherwise
	 */
	public boolean checkGod(String UUID);
	/**
	 * Closes the data source. Could be a disconnection from a SQL Server or closing a data file
	 * @return
	 */
	public boolean closeData();
}
