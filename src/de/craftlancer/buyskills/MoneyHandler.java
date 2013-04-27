package de.craftlancer.buyskills;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.entity.Player;

import de.craftlancer.buyskills.api.SkillHandler;

public class MoneyHandler implements SkillHandler<Number>
{
    Economy economy;
    String currency;
    
    public MoneyHandler(Economy economy, String currency)
    {
        this.economy = economy;
        this.currency = currency;
    }
    
    @Override
    public boolean hasCurrency(Player p, Number amount)
    {
        BuySkills.debug("has" + (economy.has(p.getName(), amount.doubleValue())));
        return economy.has(p.getName(), amount.doubleValue());
    }
    
    @Override
    public void withdrawCurrency(Player p, Number amount)
    {
        economy.withdrawPlayer(p.getName(), amount.doubleValue());
    }
    
    @Override
    public String getCurrencyName()
    {
        return currency;
    }
    
    @Override
    public boolean checkInputClass(Object obj)
    {
        BuySkills.debug("check" + (obj instanceof Number));
        return obj instanceof Number;
    }
    
    @Override
    public String getFormatedString(Object value)
    {
        return ((Number) value).toString() + " " + getCurrencyName();
    }
}
