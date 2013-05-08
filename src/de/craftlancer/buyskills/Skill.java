package de.craftlancer.buyskills;

import java.util.List;
import java.util.Map;

// TODO set() JavaDocs

/**
 * Represents a buyable and/or rentable skill
 */
public class Skill
{
    private String key;
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
    
    public Skill(String key)
    {
        this.key = key;
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
     * @param name the new name of the skill
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
     * @param bool true if they are regranted, false if not 
     */
    public void setRegrantCost(boolean bool)
    {
        this.regrantCost = bool;
    }
}
