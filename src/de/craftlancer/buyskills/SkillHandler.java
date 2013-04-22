package de.craftlancer.buyskills;

import org.bukkit.entity.Player;

public interface SkillHandler
{
    /**
     * Check if a player owns enough of the handled currency
     * Called in need checks
     * 
     * @param p
     *            the checked player
     * @param amount
     *            the amount of the handled currency
     * @return true if the amount of the handled currency is greater or equal
     *         the amount
     */
    public abstract boolean hasCurrency(Player p, int amount);
    
    /**
     * Withdraw an amount of the handled currency from a player
     * Called when buying or renting a skill
     * 
     * This function will only called when hasCurrency(p, amount) returns true 
     * 
     * @param p
     *            the checked player
     * @param amount
     *            the amount of the handled currency
     */
    public abstract void withdrawCurrency(Player p, int amount);
}
