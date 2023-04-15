package dte.tzevaadomnotifier;

import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.tzevaadomlisteners.factory.TzevaAdomListenerConfigFactory;
import dte.tzevaadomnotifier.utils.SchedulerUtils;

public class TzevaAdomNotifierMain extends ModernJavaPlugin
{
	private TzevaAdomListenerConfigFactory tzevaAdomListenerFactory;

	private static TzevaAdomNotifierMain INSTANCE;

	@Override
	public void onEnable() 
	{
		INSTANCE = this;

		saveDefaultConfig();

		this.tzevaAdomListenerFactory = new TzevaAdomListenerConfigFactory(getConfig());
		
		startNotifier();
	}

	public static TzevaAdomNotifierMain getInstance() 
	{
		return INSTANCE;
	}
	
	private void startNotifier() 
	{
		TzevaAdomNotifier.Builder notifierBuilder = TzevaAdomNotifier.requestFromPikudHaoref()
				.every(Duration.ofSeconds(2))
				.onFailedRequest(exception -> logToConsole(RED + exception.getMessage()));
		
		parseTzevaAdomNotifiers().forEach(notifierBuilder::onTzevaAdom);

		notifierBuilder.listen();
	}

	private List<Consumer<Alert>> parseTzevaAdomNotifiers()
	{
		String serverNotifierName = getConfig().getString("server-notifier");

		return Arrays.stream(serverNotifierName.split(", "))
				.map(this.tzevaAdomListenerFactory::create)
				.map(SchedulerUtils::runSync)
				.collect(Collectors.toList());
	}
}
