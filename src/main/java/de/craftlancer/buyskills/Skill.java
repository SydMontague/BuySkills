package de.craftlancer.buyskills;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents a buyable and/or rentable skill
 */
public class Skill
{
    private final String key;
    private String name;
    private String desc;
    private String info;
    private List<String> categories;
    
    private Map<String, Object> buyCosts;
    private Map<String, Object> rentCosts;
    private Map<String, Object> buyNeed;
    private Map<String, Object> rentNeed;
    
    private List<String> permNeed;
    private List<String> permEarn;
    private List<String> groupNeed;
    private List<String> groupEarn;
    private List<String> worlds;
    private boolean revokeGroup;
    private boolean revokePerm;
    private boolean regrantGroup;
    private boolean regrantPerm;
    private boolean regrantCost;
    
    private boolean buyable;
    private boolean rentable;
    private long renttime;
    
    private List<String> skillsNeed;
    private List<String> skillsIllegal;
    private int skillsNeeded;
    
    public Skill(String key, FileConfiguration config)
    {
        this.key = key;
        
        setName(config.getString(key + ".name", ""));
        setDescription(config.getString(key + ".description", ""));
        setInfo(config.getString(key + ".info", ""));
        
        setCategories(config.getStringList(key + ".category"));
        setPermEarn(config.getStringList(key + ".perm_earn"));
        setPermNeed(config.getStringList(key + ".perm_need"));
        setGroupEarn(config.getStringList(key + ".group_earn"));
        setGroupNeed(config.getStringList(key + ".group_need"));
        setSkillsNeed(config.getStringList(key + ".skill_need"));
        setSkillsIllegal(config.getStringList(key + ".skill_earn"));
        setWorlds(config.getStringList(key + ".worlds"));
        
        setRevokeGroup(config.getBoolean(key + ".revoke_group", false));
        setRevokePerm(config.getBoolean(key + ".revoke_perm", false));
        setRegrantGroup(config.getBoolean(key + ".regrant_group", true));
        setRegrantPerm(config.getBoolean(key + ".regrant_perm", true));
        setRegrantCost(config.getBoolean(key + ".regrant_cost", false));
        setBuyable(config.getBoolean(key + ".buyable", false));
        setRentable(config.getBoolean(key + ".rentable", false));
        
        setRentTime(config.getLong(key + ".renttime", 0) * 1000);
        setSkillsNeeded(config.getInt(key + ".skills_needed", 0));
        
        HashMap<String, Object> buyHelpMap = new HashMap<String, Object>();
        HashMap<String, Object> rentHelpMap = new HashMap<String, Object>();
        
        HashMap<String, Object> buyNeedHelpMap = new HashMap<String, Object>();
        HashMap<String, Object> rentNeedHelpMap = new HashMap<String, Object>();
        
        if (config.isConfigurationSection(key + ".buy_costs"))
            for (String vkey : config.getConfigurationSection(key + ".buy_costs").getKeys(false))
                buyHelpMap.put(vkey, config.get(key + ".buy_costs." + vkey));
        
        if (config.isConfigurationSection(key + ".rent_costs"))
            for (String vkey : config.getConfigurationSection(key + ".rent_costs").getKeys(false))
                rentHelpMap.put(vkey, config.get(key + ".rent_costs." + vkey));
        
        if (config.isConfigurationSection(key + ".buy_need"))
            for (String vkey : config.getConfigurationSection(key + ".buy_need").getKeys(false))
                buyNeedHelpMap.put(vkey, config.get(key + ".buy_need." + vkey));
        
        if (config.isConfigurationSection(key + ".rent_need"))
            for (String vkey : config.getConfigurationSection(key + ".rent_need").getKeys(false))
                rentNeedHelpMap.put(vkey, config.get(key + ".rent_need." + vkey));
        
        setBuyCosts(buyHelpMap);
        setRentCosts(rentHelpMap);
        setBuyNeed(buyNeedHelpMap);
        setRentNeed(rentNeedHelpMap);
    }
    
    /**
     * Get the config key of the skill
     * 
     * @return the config key of the skill
     */
    public String getKey()
    {
        return key;
    }
    
    /**
     * Get the name of the skill
     * 
     * @return the name of the skill
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Set the name of the skill
     * 
     * @param name
     *            the new name of the skill
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Get the description of the skill
     * 
     * @return the description of the skill
     */
    public String getDescription()
    {
        return desc;
    }
    
    /**
     * Set the description of the skill.
     * 
     * @param desc
     *            the new description
     */
    public void setDescription(String desc)
    {
        this.desc = desc;
    }
    
    /**
     * Get the info of the skill
     * 
     * @return the info of the skill
     */
    public String getInfo()
    {
        return info;
    }
    
    /**
     * Set the infostring of the skill
     * 
     * @param info
     *            the new infostring
     */
    public void setInfo(String info)
    {
        this.info = info;
    }
    
    /**
     * Get the categories of the skill
     * 
     * @return the categories of the skill
     */
    public List<String> getCategories()
    {
        return categories;
    }
    
    /**
     * Set the categories of the skill
     * 
     * @param categories
     *            the new List of categories
     */
    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }
    
    /**
     * Get the costs to buy this skill
     * 
     * @return the costs to buy this skill
     */
    public Map<String, Object> getBuyCosts()
    {
        return buyCosts;
    }
    
    /**
     * Set the costs to buy this skill
     * 
     * @param buy_costs
     *            new new costs
     */
    public void setBuyCosts(Map<String, Object> buy_costs)
    {
        buyCosts = buy_costs;
    }
    
    /**
     * Get the cost to rent this skill
     * 
     * @return the costs to rent this skill
     */
    public Map<String, Object> getRentCosts()
    {
        return rentCosts;
    }
    
    /**
     * Set the costs to rent this skill
     * 
     * @param rent_costs
     *            the new costs
     */
    public void setRentCosts(Map<String, Object> rent_costs)
    {
        rentCosts = rent_costs;
    }
    
    /**
     * Get the needed permission of this skill
     * 
     * @return the needed permission of this skill
     */
    public List<String> getPermNeed()
    {
        return permNeed;
    }
    
    /**
     * Set the needed permissions for this skill
     * 
     * @param perm_need
     *            a list of all needed permissions
     */
    public void setPermNeed(List<String> perm_need)
    {
        permNeed = perm_need;
    }
    
    /**
     * Get the earned permission for this skill
     * 
     * @return the earned permission for this skill
     */
    public List<String> getPermEarn()
    {
        return permEarn;
    }
    
    /**
     * Set the earned permissions for this skill
     * 
     * @param permEarn
     *            a list of all earned permissions
     */
    public void setPermEarn(List<String> permEarn)
    {
        this.permEarn = permEarn;
    }
    
    /**
     * Get the needed groups for this skill
     * 
     * @return the needed groups for this skill
     */
    public List<String> getGroupNeed()
    {
        return groupNeed;
    }
    
    /**
     * Set the needed groups of this skill
     * 
     * @param groupNeed
     *            a list of needed groups
     */
    public void setGroupNeed(List<String> groupNeed)
    {
        this.groupNeed = groupNeed;
    }
    
    /**
     * Get the earned groups for this skill
     * 
     * @return the earned groups for this skill
     */
    public List<String> getGroupEarn()
    {
        return groupEarn;
    }
    
    /**
     * Set the earned groups of this skill
     * 
     * @param groupEarn
     *            a list of all earned groups
     */
    public void setGroupEarn(List<String> groupEarn)
    {
        this.groupEarn = groupEarn;
    }
    
    /**
     * Get whether the needed groups are revoked when this skill is granted
     * 
     * @return true when the groups are revoked, false if not
     */
    public boolean isRevokeGroup()
    {
        return revokeGroup;
    }
    
    /**
     * Set whether the needed groups should be revoked when this skill is
     * granted
     * 
     * @param revokeGroup
     *            true if yes, false if not
     */
    public void setRevokeGroup(boolean revokeGroup)
    {
        this.revokeGroup = revokeGroup;
    }
    
    /**
     * Get whether the needed permissions are revoked when this skill is granted
     * 
     * @return true when the permissions are revoked, false if not
     */
    public boolean isRevokePerm()
    {
        return revokePerm;
    }
    
    /**
     * Set whether the needed permissions should be revoked when this skill is
     * granted
     * 
     * @param revokePerm
     *            true if yes, false if not
     */
    public void setRevokePerm(boolean revokePerm)
    {
        this.revokePerm = revokePerm;
    }
    
    /**
     * Get whether the needed groups are regranted when this skill is revoked
     * 
     * @return true when the groups are regranted, false if not
     */
    public boolean isRegrantGroup()
    {
        return regrantGroup;
    }
    
    /**
     * Set whether the needed groups are regranted when this skill is revoked
     * 
     * @param regrantGroup
     *            true if yes, false if not
     */
    public void setRegrantGroup(boolean regrantGroup)
    {
        this.regrantGroup = regrantGroup;
    }
    
    /**
     * Get whether the needed permissions are regranted when this skill is
     * revoked
     * 
     * @return true when the permissions are regranted, false if not
     */
    public boolean isRegrantPerm()
    {
        return regrantPerm;
    }
    
    /**
     * Set whether the needed permissions are regranted when this skill is
     * revoked
     * 
     * @param regrantPerm
     *            true if yes, false if not
     */
    public void setRegrantPerm(boolean regrantPerm)
    {
        this.regrantPerm = regrantPerm;
    }
    
    /**
     * Get if this skill is available via /skill buy
     * 
     * @return true when the skill is available, false if not
     */
    public boolean isBuyable()
    {
        return buyable;
    }
    
    /**
     * Set if this skill is avaible via /skill buy
     * 
     * @param buyable
     *            true if yes, false if not
     */
    public void setBuyable(boolean buyable)
    {
        this.buyable = buyable;
    }
    
    /**
     * Get if this skill is available via /skill rent
     * 
     * @return true when the skill is available, false if not
     */
    public boolean isRentable()
    {
        return rentable;
    }
    
    /**
     * Set if this skill is avaible via /skill rent
     * 
     * @param rentable
     *            true if yes, false if not
     */
    public void setRentable(boolean rentable)
    {
        this.rentable = rentable;
    }
    
    /**
     * Get the amount of time a skill is rented for in ms
     * 
     * @return the time in ms
     */
    public long getRenttime()
    {
        return renttime;
    }
    
    /**
     * Set the amount of time the skill is rented for
     * 
     * @param renttime
     *            the time in ms
     */
    public void setRentTime(long renttime)
    {
        this.renttime = renttime;
    }
    
    /**
     * Get the needed skills to get this skill
     * 
     * @return a list of needed skills
     */
    public List<String> getSkillsNeed()
    {
        return skillsNeed;
    }
    
    /**
     * Set the skills, needed to buy this skill
     * 
     * @param skillsNeed
     *            a list of needed skills
     */
    public void setSkillsNeed(List<String> skillsNeed)
    {
        this.skillsNeed = skillsNeed;
    }
    
    /**
     * Get the forbidden skills to get this skill
     * 
     * @return a list of forbidden skills
     */
    public List<String> getSkillsIllegal()
    {
        return skillsIllegal;
    }
    
    /**
     * Set the forbidden skills to get this skill
     * 
     * @param skillsIllegal
     *            a list of forbidden skills
     */
    public void setSkillsIllegal(List<String> skillsIllegal)
    {
        this.skillsIllegal = skillsIllegal;
    }
    
    /**
     * Get the number of skills of the SkillsNeed List which are needed to buy
     * this skill
     * 
     * @return the number of needed skills
     */
    public int getSkillsNeeded()
    {
        return skillsNeeded;
    }
    
    /**
     * Set the number of skills of the SkillsNeed List which are needed to buy
     * this skill
     * 
     * @param skillsNeeded
     *            the number of needed skills
     */
    public void setSkillsNeeded(int skillsNeeded)
    {
        this.skillsNeeded = skillsNeeded;
    }
    
    /**
     * Get the extra requirement to buy this skill
     * Used with CustomCurrency
     * 
     * @return a map of all requirements
     */
    public Map<String, Object> getBuyNeed()
    {
        return buyNeed;
    }
    
    /**
     * Set the extra requirements to buy this skill
     * Used with CustomCurrency
     * 
     * @param buyNeed
     *            a map of all requirements
     */
    public void setBuyNeed(Map<String, Object> buyNeed)
    {
        this.buyNeed = buyNeed;
    }
    
    /**
     * Get the extra requirement to rent this skill
     * Used with CustomCurrency
     * 
     * @return a map of all requirements
     */
    public Map<String, Object> getRentNeed()
    {
        return rentNeed;
    }
    
    /**
     * Set the extra requirement to rent this skill
     * Used with CustomCurrency
     * 
     * @param rentNeed
     *            a map of all requirements
     */
    public void setRentNeed(Map<String, Object> rentNeed)
    {
        this.rentNeed = rentNeed;
    }
    
    /**
     * Get a list of worlds the skill is valid for.
     * The skill can only bought/rented in this worlds.
     * 
     * @return a list of worlds, null/empty for global skills
     */
    public List<String> getWorlds()
    {
        return worlds;
    }
    
    /**
     * Set a the worlds the skill is valid for
     * 
     * @param worlds
     *            a list of worlds, null/empty for global
     */
    public void setWorlds(List<String> worlds)
    {
        this.worlds = worlds;
    }
    
    /**
     * Get if costs are regranted when this skill is revoked
     * 
     * @return true if they are regranted, false if not
     */
    public boolean isRegrantCost()
    {
        return regrantCost;
    }
    
    /**
     * Set whether costs are regranted when this skill is revoked
     * 
     * @param bool
     *            true if they are regranted, false if not
     */
    public void setRegrantCost(boolean bool)
    {
        regrantCost = bool;
    }
}
