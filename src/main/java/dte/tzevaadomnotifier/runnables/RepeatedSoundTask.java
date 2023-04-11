package dte.tzevaadomnotifier.runnables;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RepeatedSoundTask extends BukkitRunnable
{
	private final Sound sound;
	private final Iterable<? extends Player> players;
	
	private int amountLeft;
	
	public RepeatedSoundTask(Sound sound, int amount, Iterable<? extends Player> players) 
	{
		this.sound = sound;
		this.players = players;
		this.amountLeft = amount;
	}
	
	@Override
	public void run() 
	{
		this.players.forEach(this::playSound);
		
		if(--this.amountLeft == 0) 
		{
			cancel();
			return;
		}
	}
	
	private void playSound(Player player) 
	{
		player.playSound(player.getLocation(), this.sound, 1, 1);
	}
}
