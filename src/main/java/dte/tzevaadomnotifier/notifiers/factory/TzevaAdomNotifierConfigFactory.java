package dte.tzevaadomnotifier.notifiers.factory;

import static dte.tzevaadomnotifier.utils.ChatColorUtils.colorize;
import static dte.tzevaadomnotifier.utils.PlaceholderUtils.injectPlaceholders;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.configuration.Configuration;

import com.cryptomorin.xseries.XSound;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomnotifier.notifiers.SoundNotifierListener;
import dte.tzevaadomnotifier.notifiers.TitleNotifierListener;
import dte.tzevaadomnotifier.notifiers.composite.CompositeTzevaAdomListener;

public class TzevaAdomNotifierConfigFactory implements TzevaAdomNotifierFactory
{
	private final Configuration config;

	public TzevaAdomNotifierConfigFactory(Configuration config) 
	{
		this.config = config;
	}

	@Override
	public Consumer<Alert> create(String name)
	{
		List<Consumer<Alert>> combinedNotifiers = Arrays.stream(name.split(", "))
				.map(this::fromName)
				.collect(toList());
		
		CompositeTzevaAdomListener composite = new CompositeTzevaAdomListener();
		combinedNotifiers.forEach(composite::add);
		
		return composite;
	}
	
	private Consumer<Alert> fromName(String notifierName)
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

		return new SoundNotifierListener(sound.parseSound(), amount);
	}
}