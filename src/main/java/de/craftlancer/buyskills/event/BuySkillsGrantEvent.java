package de.craftlancer.buyskills.event;

import de.craftlancer.buyskills.Skill;
import de.craftlancer.buyskills.SkillPlayer;

public class BuySkillsGrantEvent extends BuySkillEvent
{
    public BuySkillsGrantEvent(Skill skill, SkillPlayer player)
    {
        super(skill, player);
    }
}
