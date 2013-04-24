package de.craftlancer.buyskills.api.event;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.Skill;

public class BuySkillsGrantEvent extends SkillEvent
{
    public BuySkillsGrantEvent(Skill skill, Player player)
    {
        super(skill, player);
    }
}
