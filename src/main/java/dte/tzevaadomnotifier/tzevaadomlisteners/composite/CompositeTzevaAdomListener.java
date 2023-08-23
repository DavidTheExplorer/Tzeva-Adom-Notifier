package dte.tzevaadomnotifier.tzevaadomlisteners.composite;

import java.util.ArrayList;
import java.util.List;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class CompositeTzevaAdomListener implements TzevaAdomListener
{
	private final List<TzevaAdomListener> listeners = new ArrayList<>();
	
	public void add(TzevaAdomListener listener) 
	{
		this.listeners.add(listener);
	}
	
	@Override
	public void onTzevaAdom(Alert alert) 
	{
		this.listeners.forEach(listener -> listener.onTzevaAdom(alert));
	}
}