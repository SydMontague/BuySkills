package de.craftlancer.buyskills;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.craftlancer.buyskills.api.SkillHandler;

public class ItemHandler implements SkillHandler<List<?>>
{
    private String name = "Item";
    
    @Override
    public boolean hasCurrency(Player p, List<?> amount)
    {
        for (Object s : amount)
            if (!p.getInventory().containsAtLeast(getItemStack(s.toString()), 1))
                return false;
        
        return true;
    }
    
    @Override
    public void withdrawCurrency(Player p, List<?> amount)
    {
        for (Object s : amount)
            p.getInventory().removeItem(getItemStack(s.toString()));
    }
    
    @Override
    public String getCurrencyName()
    {
        return name;
    }
    
    @Override
    public boolean checkInputClass(Object obj)
    {
        if (!(obj instanceof List<?>))
            return false;
        
        for (Object s : (List<?>) obj)
            if (isItemString(s.toString()))
                return false;
        
        return true;
    }
    
    @Override
    public String getFormatedString(Object value)
    {
        String output = "";
        
        for (Object s : (List<?>) value)
            output += getItemString(s.toString()) + " ";
        
        return output;
    }
    
    private static String getItemString(String str)
    {
        int id;
        int data = -1;
        int amount;
        
        String value[] = str.split(" ");
        
        if (value[0].contains(":"))
        {
            id = Integer.parseInt(value[0].split(":")[0]);
            data = Integer.parseInt(value[0].split(":")[1]);
        }
        else
            id = Integer.parseInt(value[0]);
        
        amount = Integer.parseInt(value[1]);
        
        return amount + " " + Material.getMaterial(id).name() + (data >= 0 ? ":" + data : "");
    }
    
    private static ItemStack getItemStack(String str)
    {
        int id;
        short data = -1;
        int amount;
        
        String value[] = str.split(" ");
        
        if (value[0].contains(":"))
        {
            id = Integer.parseInt(value[0].split(":")[0]);
            data = Short.parseShort(value[0].split(":")[1]);
        }
        else
            id = Integer.parseInt(value[0]);
        
        amount = Integer.parseInt(value[1]);
        
        return data == -1 ? new ItemStack(id, amount) : new ItemStack(id, amount, data);
    }
    
    private static boolean isItemString(String str)
    {
        if (str == null)
            return false;
        
        String value[] = str.split(" ");
        
        if (value.length != 2)
            return false;
        
        if (!(value[0].contains(":") && value[0].split(":")[0].matches("[0-9]") && value[0].split(":")[1].matches("[0-9]")))
            return false;
        else if (!value[0].matches("[0-9]"))
            return false;
        
        if (!value[1].matches("[0-9]"))
            return false;
        
        return true;
    }
}
