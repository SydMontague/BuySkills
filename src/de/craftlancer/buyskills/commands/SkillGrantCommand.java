package de.craftlancer.buyskills.commands;

import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;
import de.craftlancer.buyskills.event.BuySkillsGrantEvent;

public class SkillGrantCommand extends SkillSubCommand
{
    
    public SkillGrantCommand(String perm, BuySkills plugin)
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
        else if (plugin.getPlayerManager().getSkills(args[1]).contains(args[2]))
            sender.sendMessage(SkillLanguage.GRANT_ALREADY_OWN);
        else
        {
            Player p = plugin.getServer().getPlayerExact(args[1]);
            Skill s = plugin.getSkill(args[2]);
            boolean rent = SkillUtils.arrayContains(args, "rent");
            
            if (SkillUtils.arrayContains(args, "charge"))
                for (Entry<String, Integer> set : ((rent) ? s.getRentCosts().entrySet() : s.getBuyCosts().entrySet()))
                    if (BuySkills.hasHandler(set.getKey()))
                        BuySkills.getHandler(set.getKey()).withdrawCurrency(p, set.getValue());
            
            if (rent)
                plugin.getPlayerManager().grantRented(p, s, s.getRenttime());
            else
                plugin.getPlayerManager().grantSkill(p, s);
            
            plugin.getServer().getPluginManager().callEvent(new BuySkillsGrantEvent(s, p));
            
            sender.sendMessage(SkillLanguage.GRANT_SUCCESS);
            p.sendMessage(SkillLanguage.GRANT_NOTIFY.replace("%skill%", args[2]));
        }
    }
    
}
