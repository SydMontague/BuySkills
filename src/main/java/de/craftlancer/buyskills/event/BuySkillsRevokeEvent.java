package de.craftlancer.buyskills.event;

import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillsRevokeEvent extends BuySkillEvent
{
    public BuySkillsRevokeEvent(Skill skill, SkillPlayer player)
    {
        super(skill, player);
    }
}
