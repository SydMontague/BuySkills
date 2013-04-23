package de.craftlancer.buyskills;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

public class SkillUtils
{
    public static String arrayContains(String[] array, Collection<String> col)
    {
        for (String value : col)
            if (arrayContains(array, value))
                return value;
        
        return null;
    }
    
    public static boolean arrayContains(String[] array, String string)
    {
        if (array != null && array.length != 0)
            for (String value : array)
                if (value.equals(string))
                    return true;
        
        return false;
    }
    
    /**
     * Replaces dummy string values with correct values.
     * Possible Values:
     * %name%, %desc%, %info%, %categories%, %buycost%, %rentcost%,
     * %buyneed%, %rentneed%, %worlds%, %rentable%, %buyable%,
     * %rentdiscount%, %renttime%, %mintime%, %maxtime%, %skillsneed%,
     * %skillsillegal%, %skillsneeded%
     * Strings surrounded by ° are only shown when the skill is buyable
     * Strings surrounded by ~ are only shown when the skill is rentable
     * 
     * @param skill
     *            the skill which's values will be used
     * @param string
     *            the original string
     * @return the modified string
     */
    public static String replaceValues(Skill skill, String string)
    {
        if (!skill.isBuyable())
            while (string.contains("°"))
            {
                int i1 = string.indexOf("°");
                int i2 = string.indexOf("°", i1 + 1) + 1;
                
                if (i2 == -1)
                    break;
                
                string = string.replace(string.substring(i1, i2), "");
            }
        else
            string = string.replace("°", "");
        
        if (!skill.isRentable())
            while (string.contains("~"))
            {
                int i1 = string.indexOf("~");
                int i2 = string.indexOf("~", i1 + 1) + 1;
                
                if (i2 == -1)
                    break;
                
                string = string.replace(string.substring(i1, i2), "");
            }
        else
            string = string.replace("~", "");
        
        string = string.replace("%name%", skill.getName());
        string = string.replace("%desc%", skill.getDescription());
        string = string.replace("%info%", skill.getInfo());
        string = string.replace("%categories%", skill.getCategories().toString().replace("[", "").replace("]", ""));
        string = string.replace("%rentneed%", getHandlerValues(skill.getRentNeed()));
        string = string.replace("%buyneed%", getHandlerValues(skill.getBuyNeed()));
        string = string.replace("%rentcost%", getHandlerValues(skill.getRentCosts()));
        string = string.replace("%buycost%", getHandlerValues(skill.getBuyCosts()));
        string = string.replace("%worlds%", skill.getWorlds().toString().replace("[", "").replace("]", ""));
        string = string.replace("%rentable%", Boolean.toString(skill.isRentable()));
        string = string.replace("%buyable%", Boolean.toString(skill.isBuyable()));
        string = string.replace("%rentdiscount%", Boolean.toString(skill.isRentdiscount()));
        string = string.replace("%renttime%", Long.toString(skill.getRenttime()));
        string = string.replace("%minetime%", Long.toString(skill.getMintime()));
        string = string.replace("%maxtime%", Long.toString(skill.getMaxtime()));
        string = string.replace("%skillneed%", skill.getSkillsNeed().toString().replace("[", "").replace("]", ""));
        string = string.replace("%skillillegal%", skill.getSkillsIllegal().toString().replace("[", "").replace("]", ""));
        string = string.replace("%skillsneeded%", Integer.toString(skill.getSkillsNeeded()));
        return string;
    }
    
    private static String getHandlerValues(Map<String, Integer> map)
    {
        String str = "";
        
        for (Entry<String, Integer> set : map.entrySet())
            if (BuySkills.handlerList.containsKey(set.getKey()))
                str = str + set.getValue() + " " + BuySkills.handlerList.get(set.getKey()).getCurrencyName();
        
        return str;
    }
}
