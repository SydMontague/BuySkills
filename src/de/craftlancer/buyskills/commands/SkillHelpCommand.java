package de.craftlancer.buyskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.SkillLanguage;

public class SkillHelpCommand extends SkillSubCommand
{
    public SkillHelpCommand(String perm)
    {
        super(perm);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (args.length >= 1 || args[1].equalsIgnoreCase("help"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_HELP);
        else if (args[1].equalsIgnoreCase("buy"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_BUY);
        else if (args[1].equalsIgnoreCase("current"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_CURRENT);
        else if (args[1].equalsIgnoreCase("grant"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_GRANT);
        else if (args[1].equalsIgnoreCase("info"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_INFO);
        else if (args[1].equalsIgnoreCase("list"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_LIST);
        else if (args[1].equalsIgnoreCase("recalculate"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_RECALCULATE);
        else if (args[1].equalsIgnoreCase("reload"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_RELOAD);
        else if (args[1].equalsIgnoreCase("rent"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_RENT);
        else if (args[1].equalsIgnoreCase("rented"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_RENTED);
        else if (args[1].equalsIgnoreCase("reset"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_RESET);
        else if (args[1].equalsIgnoreCase("revoke"))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_REVOKE);
    }
    
}
