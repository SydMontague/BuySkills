package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;
import de.craftlancer.buyskills.api.event.BuySkillsRevokeEvent;

public class SkillRevokeCommand extends SkillSubCommand
{
    
    public SkillRevokeCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (args.length < 3)
            sender.sendMessage(SkillLanguage.COMMAND_ARGUMENTS);
        else if (plugin.getServer().getPlayerExact(args[1]) == null)
            sender.sendMessage(SkillLanguage.COMMAND_PLAYER_NOT_EXIST);
        else if (!plugin.hasSkill(args[2]))
            sender.sendMessage(SkillLanguage.COMMAND_SKILL_NOT_EXIST);
        else if (!plugin.getPlayerManager().getSkills(args[1]).contains(args[2]))
            sender.sendMessage(SkillLanguage.REVOKE_NOT_OWN);
        else
        {
            if (plugin.getPlayerManager().getRentedSkills(args[1]).containsKey(args[2]))
                plugin.getPlayerManager().revokeRented(args[1], args[2]);
            else
                plugin.getPlayerManager().revokeSkill(args[1], args[2]);
            
            plugin.getServer().getPluginManager().callEvent(new BuySkillsRevokeEvent(plugin.getSkill(args[2]), plugin.getServer().getPlayerExact(args[1])));
            
            sender.sendMessage(SkillLanguage.REVOKE_SUCCESS);
            plugin.getServer().getPlayerExact(args[1]).sendMessage(SkillLanguage.REVOKE_NOTIFY.replace("%skill%", args[2]));
            
        }
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 3:
                return SkillUtils.getMatches(args[2], plugin.skills.keySet());
            default:
                return null;
        }
    }
    
}
