package dte.tzevaadomnotifier.tzevaadomlisteners.factory;

import static dte.tzevaadomnotifier.utils.ChatColorUtils.colorize;

import org.bukkit.configuration.Configuration;

import com.cryptomorin.xseries.XSound;

import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.SoundListener;
import dte.tzevaadomnotifier.tzevaadomlisteners.TitleListener;

public class TzevaAdomListenerFactory
{
	private final Configuration config;

	public TzevaAdomListenerFactory(Configuration config) 
	{
		this.config = config;
	}
	
	public TzevaAdomListener create(String notifierName)
	{
		switch(notifierName.toLowerCase()) 
		{
			case "title":
				return parseTitleListener();
				
			case "sound":
				return parseSoundListener();
			
			default: 
				throw new IllegalArgumentException(String.format("Could not find a notifier named '%s'", notifierName));
		}
	}
	
	private TitleListener parseTitleListener() 
	{
		return TitleListener.withPlaceholders(alert -> 
		{
			String title = colorize(this.config.getString("notifiers.title.title"));
			String subtitle = colorize(this.config.getString("notifiers.title.subtitle"));

			return new String[]{title, subtitle};
		});
	}
	
	private SoundListener parseSoundListener() 
	{
		XSound sound = XSound.parse(this.config.getString("notifiers.sound.value")).sound;
		int amount = this.config.getInt("notifiers.sound.amount");
		
		//use the alternative sound if the sound is not available on the server version
		if(!sound.isSupported()) 
			sound = XSound.parse(this.config.getString("notifiers.sound.alternative")).sound;

		return new SoundListener(sound, amount);
	}
}