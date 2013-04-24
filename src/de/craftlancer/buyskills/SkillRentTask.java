package de.craftlancer.buyskills;

import java.util.Map.Entry;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import de.craftlancer.buyskills.event.BuySkillsRentExpireEvent;

public class SkillRentTask extends BukkitRunnable
{
    private BuySkills plugin;
    
    public SkillRentTask(BuySkills plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void run()
    {
        long time = System.currentTimeMillis();
        
        for(Player p : plugin.getServer().getOnlinePlayers())        
            for(Entry<String, Long> set : plugin.getPlayerManager().getRentedSkills(p).entrySet())            
                if(set.getValue() < time)
                {
                    plugin.getPlayerManager().revokeRented(p.getName(), set.getKey());
                    p.sendMessage(SkillLanguage.SKILL_RENT_EXPIRED.replace("%skill%", set.getKey()));
                    plugin.getServer().getPluginManager().callEvent(new BuySkillsRentExpireEvent(plugin.getSkill(set.getKey()), p));
                }
    }
    
}
