package de.craftlancer.buyskills.event;

import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillsRentExpireEvent extends BuySkillEvent
{
    public BuySkillsRentExpireEvent(Skill skill, SkillPlayer player)
    {
        super(skill, player);
    }
}
