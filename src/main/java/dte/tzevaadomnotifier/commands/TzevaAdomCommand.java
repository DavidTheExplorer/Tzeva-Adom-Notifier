package dte.tzevaadomnotifier.commands;

import static org.bukkit.ChatColor.DARK_RED;
import static org.bukkit.ChatColor.RED;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import dte.tzevaadomapi.alert.Alert;
import dte.tzevaadomapi.notifier.TzevaAdomListener;

public class TzevaAdomCommand implements CommandExecutor, TabCompleter
{
	private final TzevaAdomListener serverListener;

	private static final List<String> SUBCOMMANDS = Arrays.asList("test");
	
	private static final Alert DUMMY_ALERT = new Alert("תל אביב", "חדירת מחבלים - בדיקה", LocalDateTime.now());

	public TzevaAdomCommand(TzevaAdomListener serverListener) 
	{
		this.serverListener = serverListener;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(args.length != 1) 
		{
			sendHelp(sender);
			return true;
		}
		
		switch(args[0].toLowerCase()) 
		{
		case "test":
			testSubcommand();
			break;
			
		default:
			sendHelp(sender);
			break;
		}
		
		return true;
	}
	
	private void testSubcommand() 
	{
		this.serverListener.onTzevaAdom(DUMMY_ALERT);
	}

	private void sendHelp(CommandSender sender) 
	{
		sender.sendMessage(RED + "# " + DARK_RED + "TzevaAdomNotifier Help" + RED + " #");

		for(String sub : SUBCOMMANDS) 
			sender.sendMessage(RED + "/tzevaadom " + DARK_RED + sub);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) 
	{
		List<String> completions = new ArrayList<>();
		StringUtil.copyPartialMatches(args[0], SUBCOMMANDS, completions);
		
		return completions;
	}
}
