package de.craftlancer.buyskills.api.event;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.Skill;

public class BuySkillsRevokeEvent extends SkillEvent
{
    public BuySkillsRevokeEvent(Skill skill, Player player)
    {
        super(skill, player);
    }
}
