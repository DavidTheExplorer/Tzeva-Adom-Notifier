package dte.tzevaadomnotifier.tzevaadomlisteners;

import java.util.function.Function;

import org.bukkit.Bukkit;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class TitleNotifier implements TzevaAdomListener
{
	private final Function<Alert, String[]> titleFactory;
	
	public TitleNotifier(Function<Alert, String[]> titleFactory) 
	{
		this.titleFactory = titleFactory;
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
}