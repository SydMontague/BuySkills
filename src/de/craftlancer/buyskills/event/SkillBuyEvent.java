package de.craftlancer.buyskills.event;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.Skill;

public class SkillBuyEvent extends SkillEvent
{
    public SkillBuyEvent(Skill skill, Player player)
    {
        super(skill, player);
    }
    
}
