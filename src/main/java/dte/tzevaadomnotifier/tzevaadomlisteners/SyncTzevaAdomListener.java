package dte.tzevaadomnotifier.tzevaadomlisteners;

import org.bukkit.Bukkit;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomnotifier.TzevaAdomNotifierPlugin;

public class SyncTzevaAdomListener implements TzevaAdomListener
{
	private final TzevaAdomListener listener;
	
	public SyncTzevaAdomListener(TzevaAdomListener listener) 
	{
		this.listener = listener;
	}

	@Override
	public void onTzevaAdom(Alert alert) 
	{
		Bukkit.getScheduler().runTask(TzevaAdomNotifierPlugin.getInstance(), () -> this.listener.onTzevaAdom(alert));
	}
}