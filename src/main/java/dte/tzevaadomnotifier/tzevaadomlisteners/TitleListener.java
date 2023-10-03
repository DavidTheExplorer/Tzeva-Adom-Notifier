package dte.tzevaadomnotifier.tzevaadomlisteners;

import java.util.Arrays;
import java.util.function.Function;

import org.bukkit.Bukkit;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class TitleListener implements TzevaAdomListener
{
	private final Function<Alert, String[]> titleFactory;
	
	private TitleListener(Function<Alert, String[]> titleFactory) 
	{
		this.titleFactory = titleFactory;
	}

	public static TitleListener withPlaceholders(Function<Alert, String[]> titleFactory) 
	{
		//wrap the factory to support Alert placeholders
		Function<Alert, String[]> newFactory = (alert) -> 
		{
			return Arrays.stream(titleFactory.apply(alert))
					.map(text -> injectPlaceholders(text, alert))
					.toArray(String[]::new);
		};
		
		return new TitleListener(newFactory);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onTzevaAdom(Alert alert)
	{
		String[] titleData = fetchTitleData(alert);
		
		Bukkit.getOnlinePlayers().forEach(player -> player.sendTitle(titleData[0], titleData[1]));
	}
	
	private String[] fetchTitleData(Alert alert) 
	{
		String[] data = this.titleFactory.apply(alert);
		
		if(data.length != 2)
			throw new IllegalArgumentException("Exactly 1 title and 1 subtitle must be provided!");
		
		return data;
	}

	private static String injectPlaceholders(String text, Alert alert) 
	{
		return text
				.replace("%city%", alert.getCity())
				.replace("%title%", alert.getTitle());
	}
}