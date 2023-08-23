package dte.tzevaadomnotifier;

import static java.util.stream.Collectors.toList;
import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.commands.TzevaAdomTestCommand;
import dte.tzevaadomnotifier.tzevaadomlisteners.composite.CompositeTzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomNotifierFactory;
import dte.tzevaadomnotifier.utils.SchedulerUtils;

public class TzevaAdomNotifierPlugin extends ModernJavaPlugin
{
	private TzevaAdomNotifierFactory tzevaAdomNotifierFactory;
	private TzevaAdomListener tzevaAdomListener;

	private static TzevaAdomNotifierPlugin INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		saveDefaultConfig();
		registerCommands();
		

		this.tzevaAdomNotifierFactory = new TzevaAdomNotifierFactory(getConfig());
		this.tzevaAdomListener = parseTzevaAdomNotifier();
		
		createTzevaAdomNotifier().listen();
	}

	public static TzevaAdomNotifierPlugin getInstance() 
	{
		return INSTANCE;
	}
	
	private void registerCommands() 
	{
		getCommand("tzevaadomtest").setExecutor(new TzevaAdomTestCommand(this.tzevaAdomListener));
	}

	private TzevaAdomNotifier createTzevaAdomNotifier() 
	{
		return new TzevaAdomNotifier.Builder()
				.every(Duration.ofSeconds(2))
				.onTzevaAdom(this.tzevaAdomListener)
				.onFailedRequest(exception -> logToConsole(RED + exception.getMessage()))
				.build();
	}
	
	private TzevaAdomListener parseTzevaAdomNotifier()
	{
		//users may specify multiple notifiers
		String[] serverNotifierNames = getConfig().getString("server-notifier").split(", ");

		//parse and force the config notifiers to be sync
		List<TzevaAdomListener> syncListeners = Arrays.stream(serverNotifierNames)
				.map(this.tzevaAdomNotifierFactory::create)
				.map(SchedulerUtils::runSync)
				.collect(toList());

		//combine the notifiers into a single listener(so we have an object to pass to the test command)
		CompositeTzevaAdomListener compositeListener = new CompositeTzevaAdomListener();
		syncListeners.forEach(compositeListener::add);

		return compositeListener;
	}
}