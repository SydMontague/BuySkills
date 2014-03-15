package de.craftlancer.buyskills.event;

import org.bukkit.event.Cancellable;

import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillsBuyEvent extends BuySkillEvent implements Cancellable
{
    private boolean cancel = false;
    
    public BuySkillsBuyEvent(Skill skill, SkillPlayer player)
    {
        super(skill, player);
    }
    
    @Override
    public boolean isCancelled()
    {
        return cancel;
    }
    
    @Override
    public void setCancelled(boolean bool)
    {
        cancel = bool;
    }
}
