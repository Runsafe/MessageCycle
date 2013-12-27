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
		messages = configuration.getConfigValueAsList("messages");
		messageDelay = configuration.getConfigValueAsInt("cycleTime");
		setupIterator();
	}

	@Override
	public void OnPluginDisabled()
	{
		stopCycle();
	}

	@Override
	public void OnPlayerJoinEvent(RunsafePlayerJoinEvent runsafePlayerJoinEvent)
	{
		if (!cycleEnabled)
			startCycle();
	}

	private void startCycle()
	{
		cycleEnabled = true;
		setupIterator();
		registerNewMessage();
	}

	private void pauseCycle()
	{
		cycleEnabled = false;
		if (task > 0)
			scheduler.cancelTask(task);
	}

	private void stopCycle()
	{
		pauseCycle();
		messageIterator = null;
		messages.clear();
	}

	private void setupIterator()
	{
		if (messageIterator == null)
			messageIterator = messages.iterator();
	}

	private void broadcastNextMessage()
	{
		if (!cycleEnabled)
			return;

		if (server.getOnlinePlayers().size() == 0)
		{
			pauseCycle();
			return;
		}

		if (!messageIterator.hasNext())
			setupIterator();

		if (messageIterator.hasNext())
			output.broadcastColoured("&6[SERVER]: &e%s", messageIterator.next());

		this.registerNewMessage();
	}

	private void registerNewMessage()
	{
		if (cycleEnabled)
		{
			task = scheduler.startSyncTask(this, messageDelay);
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
