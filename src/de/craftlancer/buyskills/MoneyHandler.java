package de.craftlancer.buyskills;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

public class MoneyHandler implements SkillHandler
{
    Economy economy;
    
    public MoneyHandler(Economy provider)
    {
        economy = provider;
    }

    @Override
    public boolean hasCurrency(Player p, int amount)
    {
        return economy.has(p.getName(), amount);
    }

    @Override
    public void withdrawCurrency(Player p, int amount)
    {        
        economy.withdrawPlayer(p.getName(), amount);
    }
    
}
