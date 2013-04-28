package de.craftlancer.buyskills.handlers;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.api.SkillHandler;

public class LevelHandler implements SkillHandler<Integer>
{
    String name = "Level";
    
    public LevelHandler(String name)
    {
        this.name = name;
    }
    
    @Override
    public boolean hasCurrency(Player p, Integer amount)
    {
        return p.getLevel() >= amount;
    }

    @Override
    public void withdrawCurrency(Player p, Integer amount)
    {
        p.setLevel(p.getLevel() - amount);
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
