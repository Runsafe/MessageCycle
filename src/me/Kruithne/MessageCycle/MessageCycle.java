package me.Kruithne.MessageCycle;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageCycle extends JavaPlugin {

	private int currentMessageIndex = 0;
	private String[] messages = {
		"Admins will not teleport you in either world, do not ask.",
		"Before you can build in either world you will require builder rights, ask an admin for them!",
		"To claim plots in creative world ask an admin nicely.",
		"People with the [WEB] tag are using our DynMap on our site, do not trust people who claim to be admins using this tag.",
		"Want an overview of the creative world? Visit http://minecraft.runsafe.no/dynmap",
		"Enjoy Spleef? We have a dedicated arena at /spleef",
		"Survival is free-for-all world PvP, hide your house well! You've been warned.",
		"If someone is spotted flying/hacking through walls on survival, report them to an admin.",
		"Need support? Come to our TS3 channel at voice.runsafe.no",
		"X-Ray mods are forbidden, you will be banned"
	};
	private int delayInSeconds = 600;
	private Server server = null;
	
	public void onEnable()
	{
		this.server = this.getServer();
		this.setNextMessage();
	}
	
	private void sendNextMessage()
	{
		if (this.currentMessageIndex < this.messages.length)
		{
			this.server.broadcastMessage(ChatColor.GOLD + "[Server] " + ChatColor.YELLOW + this.messages[this.currentMessageIndex]);
			this.currentMessageIndex++;
		}
		else
		{
			this.currentMessageIndex = 0;
		}
		setNextMessage();
	}
	
	private void setNextMessage()
	{
		int time = this.delayInSeconds * 20;
		this.server.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

		    public void run() {
		    	sendNextMessage();
		    }
		    
		}, time);
	}
	
	
	
}
