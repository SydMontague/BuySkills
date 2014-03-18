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
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (args.length <= 1)
            return SkillLanguage.HELP_COMMAND_INFO.getString();
        if (!getPlugin().hasSkill(args[1]))
            return SkillLanguage.COMMAND_SKILL_NOT_EXIST.getString();
        
        return SkillUtils.replaceValues(getPlugin().getSkill(args[1]), SkillLanguage.INFO_DEFAULT_STRING.getString());
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return SkillUtils.getMatches(args[1], getPlugin().getSkillMap().keySet());
            default:
                return null;
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_INFO.getString());
    }
}
