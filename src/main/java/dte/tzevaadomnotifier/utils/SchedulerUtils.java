package dte.tzevaadomnotifier.utils;

import java.util.function.Consumer;

import org.bukkit.Bukkit;

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
}