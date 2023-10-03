package dte.tzevaadomnotifier;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.commands.TzevaAdomTestCommand;
import dte.tzevaadomnotifier.tzevaadomlisteners.composite.CompositeTzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomNotifierFactory;

public class TzevaAdomNotifierPlugin extends ModernJavaPlugin
{
	private TzevaAdomListener tzevaAdomListener;

	private static TzevaAdomNotifierPlugin INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		this.tzevaAdomListener = parseTzevaAdomNotifier();
		
		getCommand("tzevaadomtest").setExecutor(new TzevaAdomTestCommand(this.tzevaAdomListener));
		
		createTzevaAdomNotifier().listen();
	}

	public static TzevaAdomNotifierPlugin getInstance() 
	{
		return INSTANCE;
	}
	
	private TzevaAdomNotifier createTzevaAdomNotifier() 
	{
		return TzevaAdomNotifier.basedOnPikudHaoref()
				.every(Duration.ofSeconds(2))
				.onTzevaAdom(this.tzevaAdomListener)
				.onFailedRequest(exception -> logToConsole(RED + exception.getMessage()))
				.build();
	}
	
	private TzevaAdomListener parseTzevaAdomNotifier()
	{
		saveDefaultConfig();
		
		TzevaAdomNotifierFactory notifierFactory = new TzevaAdomNotifierFactory(getConfig());
		
		//users may specify multiple notifiers
		String[] serverNotifierNames = getConfig().getString("server-notifier").split(", ");

		//parse the config notifiers to objects
		List<TzevaAdomListener> configNotifiers = Arrays.stream(serverNotifierNames)
				.map(name -> 
				{
					try 
					{
						return notifierFactory.create(name);
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

		//combine the notifiers into a single listener(so we have an object to pass to the test command)
		CompositeTzevaAdomListener compositeListener = new CompositeTzevaAdomListener();
		configNotifiers.forEach(compositeListener::add);

		return compositeListener;
	}
}