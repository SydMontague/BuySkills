package de.craftlancer.buyskills.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import de.craftlancer.buyskills.Skill;

public class BuySkillsRentEvent extends BuySkillEvent implements Cancellable
{
    private boolean cancel = false;
    
    public BuySkillsRentEvent(Skill skill, Player player)
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
