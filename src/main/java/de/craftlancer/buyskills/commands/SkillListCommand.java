package de.craftlancer.buyskills.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;

/**
 * Handles the /skill list command
 */
public class SkillListCommand extends SkillSubCommand
{
    public SkillListCommand(String perm, BuySkills plugin)
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
        
        int page = 0;
        boolean rentable = false;
        boolean buyable = false;
        boolean all = false;
        String cat = null;
        
        if (args.length >= 2)
        {
            try
            {
                page = Integer.parseInt(args[1]) > 1 ? Integer.parseInt(args[1]) - 1 : 0;
            }
            catch (NumberFormatException e)
            {
            }
            
            if (SkillUtils.arrayContains(args, "rent"))
                rentable = true;
            if (SkillUtils.arrayContains(args, "buy"))
                buyable = true;
            if (SkillUtils.arrayContains(args, "all"))
                all = true;
            
            cat = SkillUtils.retainFirstFromArray(args, plugin.getCategories());
        }
        
        if (!buyable && !rentable)
        {
            buyable = true;
            rentable = true;
        }
        
        List<Skill> skill = getAvaibleSkills((Player) sender, rentable, buyable, all, cat);
        
        for (int i = 0; i < plugin.getSkillsPerPage(); i++)
        {
            if (skill.size() <= page * plugin.getSkillsPerPage() + i)
                break;
            
            sender.sendMessage(SkillUtils.replaceValues(skill.get(page * plugin.getSkillsPerPage() + i), SkillLanguage.LIST_DEFAULT_STRING.getString()));
        }
        
        return null;
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 1:
                return null;
            default:
                List<String> a = SkillUtils.getMatches(args[args.length - 1], plugin.getCategories());
                a.addAll(SkillUtils.getMatches(args[args.length - 1], new String[] { "rent", "buy", "all" }));
                return a;
        }
    }
    
    private List<Skill> getAvaibleSkills(Player sender, boolean rentable, boolean buyable, boolean all, String cat)
    {
        Collection<Skill> initList = plugin.getSkillMap().values();
        List<Skill> returnList = new ArrayList<Skill>();
        
        for (Skill s : initList)
            if (cat == null || s.getCategories().contains(cat))
                if ((all || plugin.getSkillPlayer(sender).skillAvaible(s)) || (buyable && s.isBuyable()) || (rentable && s.isRentable()))
                    returnList.add(s);
        
        return returnList;
    }

    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_LIST.getString());
    }
}
