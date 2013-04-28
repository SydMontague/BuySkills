package de.craftlancer.buyskills.handlers;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.api.SkillHandler;

public class HealthHandler implements SkillHandler<Integer>
{
    String name = "Health";
    
    public HealthHandler(String name)
    {
        this.name = name;
    }
    
    @Override
    public boolean hasCurrency(Player p, Integer amount)
    {
        return p.getHealth() >= amount;
    }

    @Override
    public void withdrawCurrency(Player p, Integer amount)
    {
        p.setHealth(p.getHealth() - amount);
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
