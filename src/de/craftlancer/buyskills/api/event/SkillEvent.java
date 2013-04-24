package de.craftlancer.buyskills.api.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import de.craftlancer.buyskills.Skill;

public class SkillEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    
    Skill skill;
    Player player;
    
    public SkillEvent(Skill skill, Player player)
    {
        this.skill = skill;
        this.player = player;
    }
    
    public Skill getSkill()
    {
        return skill;
    }
    
    public Player getPlayer()
    {
        return player;
    }
    
    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }
}
