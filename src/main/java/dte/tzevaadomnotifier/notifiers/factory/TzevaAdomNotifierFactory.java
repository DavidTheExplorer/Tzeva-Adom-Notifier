package dte.tzevaadomnotifier.notifiers.factory;

import java.util.function.Consumer;

import dte.tzevaadomapi.alert.Alert;

@FunctionalInterface
public interface TzevaAdomNotifierFactory
{
	Consumer<Alert> create(String name);
}