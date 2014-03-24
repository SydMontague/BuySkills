package de.craftlancer.buyskills;

import org.bukkit.scheduler.BukkitRunnable;

public class SkillSaveTask extends BukkitRunnable
{
    private final BuySkills plugin;
    
    public SkillSaveTask(BuySkills plugin)
    {
        this.plugin = plugin;
    }
    
    @Override
    public void run()
    {
        plugin.save();
    }
    
}
