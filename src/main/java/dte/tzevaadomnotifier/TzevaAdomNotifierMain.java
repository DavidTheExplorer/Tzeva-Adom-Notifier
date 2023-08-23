package dte.tzevaadomnotifier;

import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomNotifierConfigFactory;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomNotifierFactory;
import dte.tzevaadomnotifier.utils.SchedulerUtils;

public class TzevaAdomNotifierMain extends ModernJavaPlugin
{
	private TzevaAdomNotifierFactory tzevaAdomNotifierFactory;

	private static TzevaAdomNotifierMain INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		saveDefaultConfig();

		this.tzevaAdomNotifierFactory = new TzevaAdomNotifierConfigFactory(getConfig());
		
		createTzevaAdomNotifier().listen();
	}

	public static TzevaAdomNotifierMain getInstance() 
	{
		return INSTANCE;
	}

	private TzevaAdomNotifier createTzevaAdomNotifier() 
	{
		TzevaAdomNotifier.Builder builder = new TzevaAdomNotifier.Builder()
				.every(Duration.ofSeconds(2))
				.onFailedRequest(exception -> logToConsole(RED + exception.getMessage()));
		
		parseTzevaAdomNotifiers().forEach(builder::onTzevaAdom);
		
		return builder.build();
	}

	private List<TzevaAdomListener> parseTzevaAdomNotifiers()
	{
		String serverNotifierName = getConfig().getString("server-notifier");

		return Arrays.stream(serverNotifierName.split(", "))
				.map(this.tzevaAdomNotifierFactory::create)
				.map(SchedulerUtils::runSync)
				.collect(Collectors.toList());
	}
}
