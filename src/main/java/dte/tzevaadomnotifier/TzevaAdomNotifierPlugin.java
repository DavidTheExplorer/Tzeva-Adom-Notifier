package dte.tzevaadomnotifier;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.RED;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bstats.bukkit.Metrics;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.commands.TzevaAdomCommand;
import dte.tzevaadomnotifier.tzevaadomlisteners.CompositeTzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.SyncTzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomListenerFactory;

public class TzevaAdomNotifierPlugin extends ModernJavaPlugin
{
	private TzevaAdomListener serverNotifier;

	private static TzevaAdomNotifierPlugin INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		this.serverNotifier = parseServerNotifier();
		
		createTzevaAdomNotifier().listen();
		registerCommands();
		initMetrics();
	}

	public static TzevaAdomNotifierPlugin getInstance() 
	{
		return INSTANCE;
	}
	
	private TzevaAdomNotifier createTzevaAdomNotifier() 
	{
		return new TzevaAdomNotifier.Builder()
				.onTzevaAdom(this.serverNotifier)
				.onFailedRequest(exception -> logToConsole(RED + "Can't check if it's Tzeva Adom - " + ExceptionUtils.getMessage(exception)))
				.build();
	}
	
	private TzevaAdomListener parseServerNotifier()
	{
		saveDefaultConfig();
		
		TzevaAdomListenerFactory listenerFactory = new TzevaAdomListenerFactory(getConfig());
		
		//users may specify multiple notifiers
		String[] notifiersNames = getConfig().getString("server-notifier").split(", ");

		//parse the notifiers
		List<TzevaAdomListener> parsedNotifiers = Arrays.stream(notifiersNames)
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

		//combine them into a single listener because it's irrelevant if one or multiple are used
		CompositeTzevaAdomListener compositeNotifier = new CompositeTzevaAdomListener();
		parsedNotifiers.forEach(compositeNotifier::add);

		//lastly, decorate it to operate on the Server Thread - so the inner notifiers can safely access the Bukkit API
		return new SyncTzevaAdomListener(compositeNotifier);
	}
	
	private void registerCommands() 
	{
		TzevaAdomCommand tzevaAdomCommand = new TzevaAdomCommand(this.serverNotifier);
		
		getCommand("tzevaadom").setExecutor(tzevaAdomCommand);
		getCommand("tzevaadom").setTabCompleter(tzevaAdomCommand);
	}
	
	private void initMetrics() 
	{
		new Metrics(this, 20158);
	}
}