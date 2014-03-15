package de.craftlancer.buyskills.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    
    protected Skill skill;
    protected SkillPlayer player;
    
    public BuySkillEvent(Skill skill, SkillPlayer player)
    {
        this.skill = skill;
        this.player = player;
    }
    
    public Skill getSkill()
    {
        return skill;
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
