package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;

/**
 * Handles the /skill info command
 */
public class SkillInfoCommand extends SkillSubCommand
{
    public SkillInfoCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    protected void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) || !(sender instanceof Player))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (args.length <= 1)
            sender.sendMessage(SkillLanguage.HELP_COMMAND_INFO);
        else if (!plugin.hasSkill(args[1]))
            sender.sendMessage(SkillLanguage.COMMAND_SKILL_NOT_EXIST);
        else
            sender.sendMessage(SkillUtils.replaceValues(plugin.getSkill(args[1]), SkillLanguage.INFO_DEFAULT_STRING));
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return SkillUtils.getMatches(args[1], plugin.getSkillMap().keySet());
            default:
                return null;
        }
    }
}
