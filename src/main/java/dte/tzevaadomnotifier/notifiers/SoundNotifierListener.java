package dte.tzevaadomnotifier.notifiers;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import com.cryptomorin.xseries.XSound;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomnotifier.TzevaAdomNotifierMain;

public class SoundNotifierListener implements Consumer<Alert>
{
	private final XSound sound;
	private final int amount;
	
	public SoundNotifierListener(XSound sound, int amount)
	{
		this.sound = sound;
		this.amount = amount;
	}

	@Override
	public void accept(Alert alert) 
	{
		this.sound.playRepeatedly(TzevaAdomNotifierMain.getInstance(), Bukkit.getOnlinePlayers(), 1, 1, this.amount, 5);
	}
}
