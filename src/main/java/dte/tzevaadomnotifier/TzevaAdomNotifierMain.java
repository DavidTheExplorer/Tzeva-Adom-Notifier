package dte.tzevaadomnotifier;

import static dte.tzevaadomnotifier.utils.SchedulerUtils.runSync;
import static org.bukkit.ChatColor.RED;

import java.time.Duration;
import java.util.function.Consumer;

import dte.modernjavaplugin.ModernJavaPlugin;
import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomNotifier;
import dte.tzevaadomnotifier.notifiers.factory.TzevaAdomNotifierConfigFactory;
import dte.tzevaadomnotifier.notifiers.factory.TzevaAdomNotifierFactory;

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
		
		TzevaAdomNotifier
		.requestFromPikudHaoref()
		.every(Duration.ofSeconds(2))
		.onFailedRequest(exception -> logToConsole(RED + exception.getMessage()))
		.onTzevaAdom(runSync(parseTzevaAdomNotifier()))
		.listen();
	}

	public static TzevaAdomNotifierMain getInstance() 
	{
		return INSTANCE;
	}

	private Consumer<Alert> parseTzevaAdomNotifier()
	{
		String serverNotifierName = getConfig().getString("server-notifier");

		return this.tzevaAdomNotifierFactory.create(serverNotifierName);
	}
}
