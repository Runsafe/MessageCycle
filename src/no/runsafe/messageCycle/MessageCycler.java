package no.runsafe.messageCycle;

import no.runsafe.framework.api.IConfiguration;
import no.runsafe.framework.api.IOutput;
import no.runsafe.framework.api.IScheduler;
import no.runsafe.framework.api.IServer;
import no.runsafe.framework.api.event.player.IPlayerJoinEvent;
import no.runsafe.framework.api.event.plugin.IConfigurationChanged;
import no.runsafe.framework.api.event.plugin.IPluginDisabled;
import no.runsafe.framework.minecraft.event.player.RunsafePlayerJoinEvent;

import java.util.Iterator;
import java.util.List;

public class MessageCycler implements IPluginDisabled, IConfigurationChanged, IPlayerJoinEvent, Runnable
{
	public MessageCycler(IServer server, IScheduler scheduler, IOutput output)
	{
		this.server = server;
		this.scheduler = scheduler;
		this.output = output;
	}

	@Override
	public void OnConfigurationChanged(IConfiguration configuration)
	{
		this.messages = configuration.getConfigValueAsList("messages");
		this.messageDelay = configuration.getConfigValueAsInt("cycleTime");
		setupIterator();
	}

	@Override
	public void OnPluginDisabled()
	{
		this.stopCycle();
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent runsafePlayerJoinEvent)
	{
		if (!cycleEnabled)
			startCycle();
	}

	private void startCycle()
	{
		this.cycleEnabled = true;
		this.setupIterator();
		this.registerNewMessage();
	}

	private void pauseCycle()
	{
		this.cycleEnabled = false;
		if (task > 0)
			scheduler.cancelTask(task);
	}

	private void stopCycle()
	{
		pauseCycle();
		this.messageIterator = null;
		this.messages.clear();
	}

	private void setupIterator()
	{
		if (messageIterator == null)
			this.messageIterator = this.messages.iterator();
	}

	private void broadcastNextMessage()
	{
		if (!this.cycleEnabled)
			return;

		if (server.getOnlinePlayers().size() == 0)
		{
			pauseCycle();
			return;
		}

		if (!this.messageIterator.hasNext())
			this.setupIterator();

		if (this.messageIterator.hasNext())
			output.broadcastColoured("&6[SERVER]: &e%s", this.messageIterator.next());

		this.registerNewMessage();
	}

	private void registerNewMessage()
	{
		if (this.cycleEnabled)
		{
			task = this.scheduler.startSyncTask(this, this.messageDelay);
		}
	}

	public void run()
	{
		task = 0;
		broadcastNextMessage();
	}

	private final IServer server;
	private final IScheduler scheduler;
	private final IOutput output;
	private List<String> messages;
	private Iterator<String> messageIterator;
	private boolean cycleEnabled = false;
	private int messageDelay;
	private int task = 0;
}
