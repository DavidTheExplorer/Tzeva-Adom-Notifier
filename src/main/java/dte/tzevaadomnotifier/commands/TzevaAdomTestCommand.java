package dte.tzevaadomnotifier.commands;

import java.time.LocalDateTime;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class TzevaAdomTestCommand implements CommandExecutor
{
	private final TzevaAdomListener serverListener;
	
	public TzevaAdomTestCommand(TzevaAdomListener serverListener) 
	{
		this.serverListener = serverListener;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		this.serverListener.onTzevaAdom(new Alert("תל אביב", "חדירת מחבלים - בדיקה", LocalDateTime.now()));
		return true;
	}
}
