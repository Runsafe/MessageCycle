package me.Kruithne.MessageCycle;

import java.util.ArrayList;
import java.util.Iterator;

import no.runsafe.framework.interfaces.IConfiguration;
import no.runsafe.framework.interfaces.IOutput;
import no.runsafe.framework.interfaces.IPluginDisabled;
import no.runsafe.framework.interfaces.IPluginEnabled;
import no.runsafe.framework.interfaces.IScheduler;

public class MessageCycler implements IPluginEnabled, IPluginDisabled
{
	private boolean cycleEnabled = false;
	private IConfiguration config;
	private IScheduler scheduler;
	private IOutput output;
	private ArrayList<String> messages;
	private Iterator<String> messageIterator;
	private int messageDelay;
	
	public MessageCycler(IConfiguration config, IScheduler scheduler, IOutput output)
	{
		this.config = config;
		this.scheduler = scheduler;
		this.output = output;
	}

	@Override
	public void OnPluginDisabled()
	{
		this.stopCycle();
	}

	@Override
	public void OnPluginEnabled()
	{
		this.loadDataFromConfig();
		this.startCycle();
	}
	
	private void startCycle()
	{
		this.cycleEnabled = true;
		this.setupIterator();
		this.registerNewMessage();
	}
	
	private void stopCycle()
	{
		this.cycleEnabled = false;
		this.messageIterator = null;
		this.messages.clear();
	}
	
	private void setupIterator()
	{
		this.messageIterator = this.messages.iterator();
	}
	
	private void loadDataFromConfig()
	{
		this.messages = (ArrayList<String>) this.config.getConfigValueAsList("messages");
		this.messageDelay = this.config.getConfigValueAsInt("cycleTime");
	}
	
	private void broadcastNextMessage()
	{
		if (!this.messageIterator.hasNext())
		{
			this.setupIterator();
		}
		
		if (this.messageIterator.hasNext())
		{
			this.output.outputToServer(String.format(Constants.broadcastFormat, this.messageIterator.next()));
		}
		
		this.registerNewMessage();
	}
	
	private void registerNewMessage()
	{
		if (this.cycleEnabled)
		{
			this.scheduler.setTimedEvent(new Runnable() {
				public void run()
				{
					broadcastNextMessage();
				}
			}, this.messageDelay);
		}
	}

}
