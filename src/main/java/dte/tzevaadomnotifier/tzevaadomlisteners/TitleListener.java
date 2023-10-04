package dte.tzevaadomnotifier.tzevaadomlisteners;

import java.util.Arrays;
import java.util.function.Function;

import org.bukkit.Bukkit;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class TitleListener implements TzevaAdomListener
{
	private final Function<Alert, String[]> titleFactory;
	
	public TitleListener(Function<Alert, String[]> titleFactory) 
	{
		this.titleFactory = titleFactory;
	}

	public static TitleListener withPlaceholders(Function<Alert, String[]> titleFactory) 
	{
		return new TitleListener(injectPlaceholders(titleFactory));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTzevaAdom(Alert alert)
	{
		String[] titles = this.titleFactory.apply(alert);

		if(titles.length != 2)
			throw new IllegalArgumentException("Exactly 1 title and 1 subtitle must be provided!");
		
		Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(titles[0], titles[1]));
	}
	
	private static Function<Alert, String[]> injectPlaceholders(Function<Alert, String[]> titleFactory)
	{
		return (alert) -> 
		{
			String[] titles = titleFactory.apply(alert);
			
			//inject the alert's placeholders
			return Arrays.stream(titles)
					.map(text -> 
					{
						return text
								.replace("%city%", alert.getCity())
								.replace("%title%", alert.getTitle());
					})
					.toArray(String[]::new);
		};
	}
}