package de.craftlancer.buyskills.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillsResetEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private final SkillPlayer player;
    
    public BuySkillsResetEvent(SkillPlayer player)
    {
        this.player = player;
    }
    
    public SkillPlayer getPlayer()
    {
        return player;
    }
    
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
}
