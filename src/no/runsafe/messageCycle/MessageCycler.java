package no.runsafe.messageCycle;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.api.event.plugin.IPluginEnabled;

import java.util.ArrayList;
import java.util.Iterator;

public class MessageCycler implements IPluginEnabled, IPluginDisabled, IConfigurationChanged
{
	private boolean cycleEnabled = false;
	private final IScheduler scheduler;
	private final IOutput output;
	private ArrayList<String> messages;
	private Iterator<String> messageIterator;
	private int messageDelay;

	public MessageCycler(IScheduler scheduler, IOutput output)
	{
		this.scheduler = scheduler;
		this.output = output;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.messages = (ArrayList<String>) configuration.getConfigValueAsList("messages");
		this.messageDelay = configuration.getConfigValueAsInt("cycleTime");
		setupIterator();
	}

	@Override
	public void OnPluginDisabled()
	{
		this.stopCycle();
	}

	@Override
	public void OnPluginEnabled()
	{
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

	private void broadcastNextMessage()
	{
		if (!this.messageIterator.hasNext())
		{
			this.setupIterator();
		}

		if (this.messageIterator.hasNext())
		{
			output.broadcastColoured("&6[SERVER]: &e%s", this.messageIterator.next());
		}

		this.registerNewMessage();
	}

	private void registerNewMessage()
	{
		if (this.cycleEnabled)
		{
			this.scheduler.startSyncTask(new Runnable()
			{
				public void run()
				{
					broadcastNextMessage();
				}
			}, this.messageDelay);
		}
	}
}
