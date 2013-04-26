package de.craftlancer.buyskills;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.api.SkillHandler;

public class MoneyHandler implements SkillHandler
{
    Economy economy;
    String currency;
    
    public MoneyHandler(Economy economy, String currency)
    {
        this.economy = economy;
        this.currency = currency;
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
    
    @Override
    public String getCurrencyName()
    {
        return currency;
    }
}
