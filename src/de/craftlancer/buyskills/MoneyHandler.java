package de.craftlancer.buyskills;

import net.milkbowl.vault.economy.Economy;

public class MoneyHandler implements SkillHandler
{
    Economy economy;
    
    public MoneyHandler(Economy provider)
    {
        economy = provider;
    }
    
}
