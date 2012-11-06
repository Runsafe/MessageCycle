package no.runsafe.messageCycle;

import no.runsafe.framework.RunsafeConfigurablePlugin;

public class Plugin extends RunsafeConfigurablePlugin
{
	@Override
	protected void PluginSetup()
	{
		this.addComponent(MessageCycler.class);
	}
}
