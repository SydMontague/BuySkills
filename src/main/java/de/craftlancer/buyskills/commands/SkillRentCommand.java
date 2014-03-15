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
import de.craftlancer.buyskills.event.BuySkillsRentEvent;

/**
 * Handles the /skill rent command
 */
public class SkillRentCommand extends SkillSubCommand
{
    public SkillRentCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (!(sender instanceof Player))
            return SkillLanguage.COMMAND_PLAYERONLY.getString();
        else if (args.length < 2)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        else if (!plugin.hasSkill(args[1]))
            return SkillLanguage.COMMAND_SKILL_NOT_EXIST.getString();
        else if (!plugin.getSkill(args[1]).isRentable())
            return SkillLanguage.RENT_NOT_RENTABLE.getString();
        
        Player player = (Player) sender;
        SkillPlayer skillPlayer = plugin.getSkillPlayer(player);
        Skill skill = plugin.getSkill(args[1]);
        
        if (plugin.getSkillCap() != 0 && plugin.getSkillCap() <= skillPlayer.getSkills().size() - skillPlayer.getBonusCap())
            return SkillLanguage.BUYRENT_SKILLCAP_REACHED.getString();
        if (skillPlayer.hasSkill(args[1]))
            return SkillLanguage.BUYRENT_ALREADY_OWN.getString();
        if (!skill.getWorlds().isEmpty() && !skill.getWorlds().contains(player.getWorld().getName()))
            return SkillLanguage.BUYRENT_WRONG_WORLD.getString();
        if (!skillPlayer.hasPermNeed(skill))
            return SkillLanguage.BUYRENT_NOT_PERMISSION.getString();
        if (!skillPlayer.hasGroupNeed(skill))
            return SkillLanguage.BUYRENT_NOT_GROUP.getString();
        if (!skillPlayer.followsSkilltree(skill))
            return SkillLanguage.BUYRENT_NOT_SKILLTREE.getString();
        if (!SkillUtils.hasCurrency(player, skill.getRentNeed()))
            return SkillLanguage.BUYRENT_NOT_CURRENCYS.getString();
        if (!SkillUtils.hasCurrency(player, skill.getRentCosts()))
            return SkillLanguage.BUYRENT_NOT_AFFORD.getString();
        
        BuySkillsRentEvent event = new BuySkillsRentEvent(skill, skillPlayer);
        plugin.getServer().getPluginManager().callEvent(event);
        
        if (event.isCancelled())
            return SkillLanguage.BUYRENT_CANCELLED.getString();
        
        SkillUtils.withdraw(player, skill.getRentCosts());
        
        skillPlayer.grantRented(skill, skill.getRenttime());
        return SkillLanguage.RENT_SUCCESS.getString();
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 2:
                return SkillUtils.getMatches(args[1], plugin.getSkillMap().keySet());
            default:
                return null;
        }
    }

    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_RENT.getString());
    }
}
