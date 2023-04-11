package dte.tzevaadomnotifier.notifiers.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import dte.tzevaadomapi.alert.Alert;

public class CompositeTzevaAdomListener implements Consumer<Alert>
{
	private final List<Consumer<Alert>> notifiers = new ArrayList<>();
	
	public void add(Consumer<Alert> notifiers) 
	{
		this.notifiers.add(notifiers);
	}
	
	@Override
	public void accept(Alert alert) 
	{
		this.notifiers.forEach(notifier -> notifier.accept(alert));
	}
}