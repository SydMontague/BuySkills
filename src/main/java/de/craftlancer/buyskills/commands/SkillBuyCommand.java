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
import de.craftlancer.buyskills.event.BuySkillsBuyEvent;
import de.craftlancer.currencyhandler.CurrencyHandler;

/**
 * Handles the /skill buy command
 */
public class SkillBuyCommand extends SkillSubCommand
{
    public SkillBuyCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (!(sender instanceof Player))
            return SkillLanguage.COMMAND_PLAYERONLY.getString();
        
        Player player = (Player) sender;
        SkillPlayer skillPlayer = getPlugin().getSkillPlayer(player);
        
        if (args.length < 2)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        if (!getPlugin().hasSkill(args[1]))
            return SkillLanguage.COMMAND_SKILL_NOT_EXIST.getString();
        if (!getPlugin().getSkill(args[1]).isBuyable())
            return SkillLanguage.BUY_NOT_BUYABLE.getString();
        if (getPlugin().getSkillCap() != 0 && getPlugin().getSkillCap() <= skillPlayer.getSkills().size() - skillPlayer.getBonusCap())
            return SkillLanguage.BUYRENT_SKILLCAP_REACHED.getString();
        if (getPlugin().getSkillPlayer(player).getSkills().contains(args[1]))
            return SkillLanguage.BUYRENT_ALREADY_OWN.getString();
        
        Skill skill = getPlugin().getSkill(args[1]);
        
        if (!skill.getWorlds().isEmpty() && !skill.getWorlds().contains(player.getWorld().getName()))
            return SkillLanguage.BUYRENT_WRONG_WORLD.getString();
        if (!skillPlayer.hasPermNeed(skill))
            return SkillLanguage.BUYRENT_NOT_PERMISSION.getString();
        if (!skillPlayer.hasGroupNeed(skill))
            return SkillLanguage.BUYRENT_NOT_GROUP.getString();
        if (!skillPlayer.followsSkilltree(skill))
            return SkillLanguage.BUYRENT_NOT_SKILLTREE.getString();
        if (!CurrencyHandler.hasCurrencies(player, skill.getBuyNeed()))
            return SkillLanguage.BUYRENT_NOT_CURRENCYS.getString();
        if (!CurrencyHandler.hasCurrencies(player, skill.getBuyCosts()))
            return SkillLanguage.BUYRENT_NOT_AFFORD.getString();
        
        BuySkillsBuyEvent event = new BuySkillsBuyEvent(skill, skillPlayer);
        getPlugin().getServer().getPluginManager().callEvent(event);
        
        if (event.isCancelled())
            return SkillLanguage.BUYRENT_CANCELLED.getString();
        
        CurrencyHandler.withdrawCurrencies(player, skill.getBuyCosts());
        
        skillPlayer.grantSkill(skill);
        return SkillLanguage.BUY_SUCCESS.getString();
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
        sender.sendMessage(SkillLanguage.HELP_COMMAND_BUY.getString());
    }
}
