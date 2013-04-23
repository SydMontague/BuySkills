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
import de.craftlancer.buyskills.SkillPlayer;
import de.craftlancer.buyskills.SkillUtils;

public class SkillListCommand extends SkillSubCommand
{
    BuySkills plugin;
    
    public SkillListCommand(String perm, BuySkills plugin)
    {
        super(perm);
        this.plugin = plugin;
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (!(sender instanceof Player))
            sender.sendMessage(SkillLanguage.COMMAND_PLAYERONLY);
        else
        {
            int page = 0;
            boolean rentable = true;
            boolean buyable = true;
            boolean all = false;
            String cat = null;
            
            if (args.length >= 2)
            {
                try
                {
                    page = Integer.parseInt(args[2]) > 1 ? Integer.parseInt(args[2]) - 1 : 0;
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
                
                cat = SkillUtils.arrayContains(args, plugin.categories);
            }
            
            List<Skill> skill = getAvaibleSkills((Player) sender, rentable, buyable, all, cat);
            
            for (int i = 0; i < plugin.skillsperpage; i++)
                sender.sendMessage(SkillUtils.replaceValues(skill.get(page * plugin.skillsperpage + i), SkillLanguage.LIST_DEFAULT_STRING));
        }
    }
    
    private List<Skill> getAvaibleSkills(Player sender, boolean rentable, boolean buyable, boolean all, String cat)
    {
        Collection<Skill> initList = plugin.skills.values();
        List<Skill> returnList = new ArrayList<Skill>();
        
        for (Skill s : initList)
            if (cat == null || s.getCategories().contains(cat))
                if ((all || skillAvaible(sender, s)) || (buyable && s.isBuyable()) || (rentable && s.isRentable()))
                    returnList.add(s);
        
        return returnList;
    }
    
    // TODO marked for outsourcing
    private boolean skillAvaible(Player p, Skill s)
    {
        for (String str : s.getPermNeed())
            if (!p.hasPermission(str))
                return false;
        
        for (String str : s.getGroupNeed())
            if (!plugin.permission.playerInGroup(p, str))
                return false;
        
        for (String str : s.getSkillsIllegal())
            if (SkillPlayer.getSkills(p).contains(str))
                return false;
        
        for (String str : s.getSkillsNeed())
            if (!SkillPlayer.getSkills(p).contains(str))
                return false;
        
        if (SkillPlayer.getSkills(p).contains(s.getName()))
            return false;
        
        return true;
    }
}
