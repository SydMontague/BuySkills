package de.craftlancer.buyskills;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.craftlancer.currencyhandler.CurrencyHandler;

/**
 * Supports a lot of static utility methods
 */
public class SkillUtils
{
    /**
     * Checks if a String array contains a value of a String Collection
     * 
     * @param array
     *        the array
     * @param col
     *        the Collection
     * @return the first value which is in both, the array and the Collection
     */
    public static <T> T retainFirstFromArray(T[] array, Collection<T> col)
    {
        for (T value : col)
            if (arrayContains(array, value))
                return value;
        
        return null;
    }
    
    /**
     * Check if a String array contains a String
     * The check is case sensitive
     * 
     * @param array
     *        the array
     * @param string
     *        the String
     * @return true if the array contains the string, false if not
     */
    public static <T> boolean arrayContains(T[] array, T string)
    {
        if (array != null && array.length != 0)
            for (T value : array)
                if (value.equals(string))
                    return true;
        
        return false;
    }
    
    /**
     * Replaces dummy string values with correct values.
     * Possible Values:
     * %name%, %desc%, %info%, %categories%, %buycost%, %rentcost%, %buyneed%,
     * %rentneed%, %worlds%, %rentable%, %buyable%, %renttime%, %skillsneed%,
     * %skillsillegal%, %skillsneeded%
     * Strings surrounded by ^ are only shown when the skill is buyable
     * Strings surrounded by ~ are only shown when the skill is rentable
     * 
     * @param skill
     *        the skill which's values will be used
     * @param string
     *        the original string
     * @return the modified string
     */
    public static String replaceValues(Skill skill, String string)
    {
        if (!skill.isBuyable())
            while (string.contains("^"))
            {
                int i1 = string.indexOf("^");
                int i2 = string.indexOf("^", i1 + 1) + 1;
                
                if (i2 == -1)
                    break;
                
                string = string.replace(string.substring(i1, i2), "");
            }
        else
            string = string.replace("^", "");
        
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
        string = string.replace("%renttime%", SkillUtils.getTimeDiffString(skill.getRenttime() / 1000));
        string = string.replace("%skillneed%", skill.getSkillsNeed().toString().replace("[", "").replace("]", ""));
        string = string.replace("%skillillegal%", skill.getSkillsIllegal().toString().replace("[", "").replace("]", ""));
        string = string.replace("%skillsneeded%", Integer.toString(skill.getSkillsNeeded()));
        
        return string;
    }
    
    /**
     * Get the string of the difference to the given time
     * Format: <HOURS> <MINUTES> <SECONDS>
     * 
     * @param value
     *        a time in the future which's differnce string you want
     * @return the difference string
     */
    public static String getTimeDiffString(long value)
    {
        long time = (value - System.currentTimeMillis()) / 1000;
        
        long h = time / 3600;
        long min = (time - h * 3600) / 60;
        long s = time - h * 3600 - min * 60;
        
        return h + "h " + min + "min " + s + "s";
    }
    
    /**
     * Get all values of a String Collection which start with a given String
     * 
     * @param value
     *        the given String
     * @param list
     *        the Collection
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, Collection<String> list)
    {
        List<String> result = new LinkedList<String>();
        
        for (String str : list)
            if (str.startsWith(value))
                result.add(str);
        
        return result;
    }
    
    /**
     * Get all values of a String array which start with a given String
     * 
     * @param value
     *        the given String
     * @param list
     *        the array
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, String[] list)
    {
        List<String> result = new LinkedList<String>();
        
        for (String str : list)
            if (str.startsWith(value))
                result.add(str);
        
        return result;
    }
    
    @SuppressWarnings("unchecked")
    private static String getHandlerValues(Map<String, Object> map)
    {
        StringBuilder str = new StringBuilder();
        
        for (Entry<String, Object> set : map.entrySet())
            if (CurrencyHandler.hasHandler(set.getKey()))
                if (CurrencyHandler.getHandler(set.getKey()).checkInputObject(set.getValue()))
                    str.append(CurrencyHandler.getHandler(set.getKey()).getFormatedString(set.getValue())).append(", ");
        
        if (str.length() != 0)
            str.deleteCharAt(str.length() - 2);
        
        return str.toString();
    }
}
