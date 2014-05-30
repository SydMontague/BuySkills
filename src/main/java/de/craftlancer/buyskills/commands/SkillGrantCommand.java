package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillPlayer;
import de.craftlancer.buyskills.SkillUtils;
import de.craftlancer.buyskills.event.BuySkillsGrantEvent;
import de.craftlancer.currencyhandler.CurrencyHandler;

/**
 * Handles the /skill grant command
 */
public class SkillGrantCommand extends SkillSubCommand
{
    public SkillGrantCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (args.length < 3)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        
        SkillPlayer skillPlayer = getPlugin().getSkillPlayer(args[1]);
        
        if (skillPlayer == null)
            return SkillLanguage.COMMAND_PLAYER_NOT_EXIST.getString();
        if (!getPlugin().hasSkill(args[2]))
            return SkillLanguage.COMMAND_SKILL_NOT_EXIST.getString();
        if (skillPlayer.hasSkill(args[2]))
            return SkillLanguage.GRANT_ALREADY_OWN.getString();
        
        Player p = getPlugin().getServer().getPlayerExact(args[1]);
        Skill skill = getPlugin().getSkill(args[2]);
        boolean rent = SkillUtils.arrayContains(args, "rent");
        
        if (SkillUtils.arrayContains(args, "charge"))
            if (p == null || !CurrencyHandler.hasCurrencies(p, (rent) ? skill.getRentCosts() : skill.getBuyCosts()))
                return SkillLanguage.GRANT_NOT_AFFORD.getString();
            else
                CurrencyHandler.withdrawCurrencies(p, (rent) ? skill.getRentCosts() : skill.getBuyCosts());
        
        if (rent)
            skillPlayer.grantRented(skill, skill.getRenttime());
        else
            skillPlayer.grantSkill(skill);
        
        getPlugin().getServer().getPluginManager().callEvent(new BuySkillsGrantEvent(skill, skillPlayer));
        
        if (p != null)
            p.sendMessage(SkillLanguage.GRANT_NOTIFY.getString().replace("%skill%", args[2]));
        
        return SkillLanguage.GRANT_SUCCESS.getString();
        
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return null;
            case 3:
                return SkillUtils.getMatches(args[2], getPlugin().getSkillMap().keySet());
            default:
                return SkillUtils.getMatches(args[args.length - 1], new String[] { "rent", "charge" });
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_GRANT.getString());
    }
}
