package me.Kruithne.MessageCycle;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationFile;

import java.io.InputStream;

public class MessageCycle extends RunsafePlugin implements IConfigurationFile
{

	public MessageCycle()
	{
		super();
	}

	@Override
	protected void PluginSetup()
	{
		this.addComponent(MessageCycler.class);
	}

	@Override
	public InputStream getDefaultConfiguration()
	{
		return getResource(Constants.defaultConfigurationFile);
	}

	@Override
	public String getConfigurationPath()
	{
		return Constants.configurationFile;
	}
}
