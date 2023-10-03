package dte.tzevaadomnotifier;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.exception.ExceptionUtils;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.commands.TzevaAdomTestCommand;
import dte.tzevaadomnotifier.tzevaadomlisteners.composite.CompositeTzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomListenerFactory;

public class TzevaAdomNotifierPlugin extends ModernJavaPlugin
{
	private TzevaAdomListener serverListener;

	private static TzevaAdomNotifierPlugin INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		this.serverListener = parseServerListener();
		
		getCommand("tzevaadomtest").setExecutor(new TzevaAdomTestCommand(this.serverListener));
		
		createTzevaAdomNotifier().listen();
	}

	public static TzevaAdomNotifierPlugin getInstance() 
	{
		return INSTANCE;
	}
	
	private TzevaAdomNotifier createTzevaAdomNotifier() 
	{
		return new TzevaAdomNotifier.Builder()
				.every(Duration.ofSeconds(2))
				.onTzevaAdom(this.serverListener)
				.onFailedRequest(exception -> logToConsole(RED + "Can't check if it's Tzeva Adom - " + ExceptionUtils.getMessage(exception)))
				.build();
	}
	
	private TzevaAdomListener parseServerListener()
	{
		saveDefaultConfig();
		
		TzevaAdomListenerFactory listenerFactory = new TzevaAdomListenerFactory(getConfig());
		
		//users may specify multiple notifiers
		String[] serverNotifierNames = getConfig().getString("server-notifier").split(", ");

		//parse the notifiers
		List<TzevaAdomListener> configNotifiers = Arrays.stream(serverNotifierNames)
				.map(name -> 
				{
					try 
					{
						return listenerFactory.create(name);
					}
					catch(IllegalArgumentException exception) 
					{
						logToConsole(RED + exception.getMessage());
						logToConsole(RED + "Ignoring it.");
						return null;
					}
				})
				.filter(Objects::nonNull)
				.collect(toList());

		//combine them into a single listener(so we have an object to pass to the test command)
		CompositeTzevaAdomListener compositeListener = new CompositeTzevaAdomListener();
		configNotifiers.forEach(compositeListener::add);

		return compositeListener;
	}
}