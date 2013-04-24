package de.craftlancer.buyskills.event;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.Skill;

public class BuySkillsRentExpireEvent extends SkillEvent
{
    public BuySkillsRentExpireEvent(Skill skill, Player player)
    {
        super(skill, player);
    }
}
