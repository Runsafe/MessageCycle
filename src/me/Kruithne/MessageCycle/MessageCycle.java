package me.Kruithne.MessageCycle;

import java.io.InputStream;

import no.runsafe.framework.RunsafePlugin;
import no.runsafe.framework.configuration.IConfigurationDefaults;
import no.runsafe.framework.configuration.IConfigurationFile;

public class MessageCycle extends RunsafePlugin implements IConfigurationFile, IConfigurationDefaults
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
