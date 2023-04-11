package dte.tzevaadomnotifier.notifiers;

import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomnotifier.TzevaAdomNotifierMain;
import dte.tzevaadomnotifier.runnables.RepeatedSoundTask;

public class SoundNotifierListener implements Consumer<Alert>
{
	private final Sound sound;
	private final int amount;
	
	public SoundNotifierListener(Sound sound, int amount)
	{
		this.sound = sound;
		this.amount = amount;
	}

	@Override
	public void accept(Alert alert) 
	{
		new RepeatedSoundTask(this.sound, this.amount, Bukkit.getOnlinePlayers()).runTaskTimer(TzevaAdomNotifierMain.getInstance(), 0, 5);
	}
}
