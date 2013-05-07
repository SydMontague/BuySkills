package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;
import de.craftlancer.buyskills.event.BuySkillsGrantEvent;

/**
 * Handles the /skill grant command
 */
public class SkillGrantCommand extends SkillSubCommand
{
    public SkillGrantCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    protected void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) || !(sender instanceof Player))
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
                SkillUtils.withdraw(p, ((rent) ? s.getRentCosts().entrySet() : s.getBuyCosts().entrySet()));
            
            if (rent)
                plugin.getPlayerManager().grantRented(p, s, s.getRenttime());
            else
                plugin.getPlayerManager().grantSkill(p, s);
            
            plugin.getServer().getPluginManager().callEvent(new BuySkillsGrantEvent(s, p));
            
            sender.sendMessage(SkillLanguage.GRANT_SUCCESS);
            p.sendMessage(SkillLanguage.GRANT_NOTIFY.replace("%skill%", args[2]));
        }
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return null;
            case 3:
                return SkillUtils.getMatches(args[2], plugin.getSkillMap().keySet());
            default:
                return SkillUtils.getMatches(args[args.length - 1], new String[] { "rent", "charge" });
        }
    }
}
