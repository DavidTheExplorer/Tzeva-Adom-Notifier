package dte.tzevaadomnotifier.utils;

import dte.tzevaadomapi.alert.Alert;

public class PlaceholderUtils 
{
	public static String injectPlaceholders(String text, Alert alert) 
	{
		return text
				.replace("%city%", alert.getCity())
				.replace("%title%", alert.getTitle());
	}
}
