package dte.tzevaadomnotifier.tzevaadomlisteners.factory;

import java.util.function.Consumer;

import dte.tzevaadomapi.alert.Alert;

@FunctionalInterface
public interface TzevaAdomListenerFactory
{
	Consumer<Alert> create(String name);
}