package dte.tzevaadomnotifier.utils;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

import dte.tzevaadomapi.notifier.TzevaAdomListener;
import dte.tzevaadomnotifier.TzevaAdomNotifierMain;

public class SchedulerUtils 
{
	/**
	 * Decorates the provided {@code consumer} to run from Bukkit's Server Thread.
	 * 
	 * @param <T> The type of objects accepted by the consumer.
	 * @param consumer The consumer.
	 * @return The provided consumer, just forced to be sync.
	 */
	public static <T> Consumer<T> runSync(Consumer<T> consumer)
	{
		return (object) -> Bukkit.getScheduler().runTask(TzevaAdomNotifierMain.getInstance(), () -> consumer.accept(object));
	}
	

	/**
	 * Decorates the provided {@code Tzeva Adom Listener} to run from Bukkit's Server Thread - to make it usable in game.
	 * 
	 * @param listener The tzeva adom listener to decorate.
	 * @return The provided listener, just forced to be sync.
	 */
	public static TzevaAdomListener runSync(TzevaAdomListener listener)
	{
		return (alert) -> runSync(listener::onTzevaAdom);
	}
}