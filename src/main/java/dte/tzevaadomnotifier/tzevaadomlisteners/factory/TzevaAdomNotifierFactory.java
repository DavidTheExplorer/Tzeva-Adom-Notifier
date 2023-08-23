package dte.tzevaadomnotifier.tzevaadomlisteners.factory;

import dte.tzevaadomapi.notifier.TzevaAdomListener;

@FunctionalInterface
public interface TzevaAdomNotifierFactory
{
	TzevaAdomListener create(String name);
}