package de.craftlancer.buyskills.event;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.Skill;

public class SkillRentEvent extends SkillEvent
{
    public SkillRentEvent(Skill skill, Player player)
    {
        super(skill, player);
    }
    
}
