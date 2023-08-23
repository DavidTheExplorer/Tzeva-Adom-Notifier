package dte.tzevaadomnotifier.notifiers.factory;

import dte.tzevaadomapi.notifier.TzevaAdomListener;

@FunctionalInterface
public interface TzevaAdomNotifierFactory
{
	TzevaAdomListener create(String name);
}