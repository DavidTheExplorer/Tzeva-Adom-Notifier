package dte.tzevaadomnotifier.tzevaadomlisteners.factory;

import static dte.tzevaadomnotifier.utils.ChatColorUtils.colorize;
import static dte.tzevaadomnotifier.utils.PlaceholderUtils.injectPlaceholders;

import org.bukkit.configuration.Configuration;

import com.cryptomorin.xseries.XSound;

import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.SoundNotifierListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.TitleNotifierListener;

public class TzevaAdomNotifierFactory
{
	private final Configuration config;

	public TzevaAdomNotifierFactory(Configuration config) 
	{
		this.config = config;
	}
	
	public TzevaAdomListener create(String notifierName)
	{
		switch(notifierName.toLowerCase()) 
		{
			case "title":
				return parseTitleNotifier();
				
			case "sound":
				return parseSoundNotifier();
				
			default:
				throw new IllegalArgumentException(String.format("Could not find a notifier named '%s'", notifierName));
		}
	}
	
	private TitleNotifierListener parseTitleNotifier() 
	{
		return new TitleNotifierListener(alert -> 
		{
			String title = injectPlaceholders(colorize(this.config.getString("notifiers.title.title")), alert);
			String subtitle = injectPlaceholders(colorize(this.config.getString("notifiers.title.subtitle")), alert);

			return new String[]{title, subtitle};
		});
	}
	
	private SoundNotifierListener parseSoundNotifier() 
	{
		XSound sound = XSound.parse(this.config.getString("notifiers.sound.value")).sound;
		int amount = this.config.getInt("notifiers.sound.amount");
		
		//use the alternative sound if the sound is not available on the server version
		if(!sound.isSupported()) 
			sound = XSound.parse(this.config.getString("notifiers.sound.alternative")).sound;

		return new SoundNotifierListener(sound, amount);
	}
}