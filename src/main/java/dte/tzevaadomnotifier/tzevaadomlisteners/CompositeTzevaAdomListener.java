package dte.tzevaadomnotifier.tzevaadomlisteners;

import java.util.ArrayList;
import java.util.List;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class CompositeTzevaAdomListener implements TzevaAdomListener
{
	private final List<TzevaAdomListener> listeners = new ArrayList<>();
	
	@Override
	public void onTzevaAdom(Alert alert)
	{
		this.listeners.forEach(listener -> listener.onTzevaAdom(alert));
	}
	
	public void add(TzevaAdomListener listener) 
	{
		this.listeners.add(listener);
	}
}