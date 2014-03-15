package de.craftlancer.buyskills.commands;

import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;

/**
 * Handles the /skill help command
 */
public class SkillHelpCommand extends SkillSubCommand
{
    private Map<String, SkillSubCommand> commands;
    
    public SkillHelpCommand(String perm, BuySkills plugin, Map<String, SkillSubCommand> map)
    {
        super(perm, plugin);
        commands = map;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (sender instanceof Player && !sender.hasPermission(getPermission()))
            return cmd.getPermissionMessage();
        
        if (args.length >= 2 && commands.containsKey(args[1]))
            commands.get(args[1]).help(sender);
        else
            help(sender);
        
        return null;
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return SkillUtils.getMatches(args[1], commands.keySet());
            default:
                return null;
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_HELP.getString());
        if (sender.hasPermission("buyskills.admin") || !(sender instanceof Player))
            sender.sendMessage(SkillLanguage.HELP_COMMAND_HELP_ADMIN.getString());
    }
}
