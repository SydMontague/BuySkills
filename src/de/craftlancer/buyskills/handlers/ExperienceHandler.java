package de.craftlancer.buyskills.handlers;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.api.SkillHandler;

public class ExperienceHandler implements SkillHandler<Integer>
{
    String name = "XP";
    
    public ExperienceHandler(String name)
    {
        this.name = name;
    }
    
    @Override
    public boolean hasCurrency(Player p, Integer amount)
    {
        return p.getTotalExperience() >= amount;
    }

    @Override
    public void withdrawCurrency(Player p, Integer amount)
    {
        p.setTotalExperience(p.getTotalExperience() - amount);
    }

    @Override
    public String getCurrencyName()
    {
        return name;
    }

    @Override
    public boolean checkInputClass(Object obj)
    {
        return (obj instanceof Integer);
    }

    @Override
    public String getFormatedString(Object value)
    {
        return value.toString() + " " + getCurrencyName();
    }
}
