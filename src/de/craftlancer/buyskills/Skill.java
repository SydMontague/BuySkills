package de.craftlancer.buyskills;

import java.util.List;
import java.util.Map;

public class Skill
{
    // general
    private String key;
    private String name;
    private String desc;
    private String info;
    private List<String> categories;
    // buy
    private Map<String, Integer> buyCosts;
    private Map<String, Integer> rentCosts;
    private Map<String, Integer> buyNeed;
    private Map<String, Integer> rentNeed;
    // permission
    private List<String> permNeed;
    private List<String> permEarn;
    private List<String> groupNeed;
    private List<String> groupEarn;
    private List<String> worlds;
    private boolean revokeGroup;
    private boolean revokePerm;
    private boolean regrantGroup;
    private boolean regrantPerm;
    // rent
    private boolean buyable;
    private boolean rentable;
    private long renttime;
    // Skilltree
    private List<String> skillsNeed;
    private List<String> skillsIllegal;
    private int skillsNeeded;
    
    public Skill(String key)
    {
        setKey(key);
    }
    
    public String getKey()
    {
        return key;
    }
    
    private void setKey(String key)
    {
        this.key = key;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getDescription()
    {
        return desc;
    }
    
    public void setDescription(String desc)
    {
        this.desc = desc;
    }
    
    public String getInfo()
    {
        return info;
    }
    
    public void setInfo(String info)
    {
        this.info = info;
    }
    
    public List<String> getCategories()
    {
        return categories;
    }
    
    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }
    
    public Map<String, Integer> getBuyCosts()
    {
        return buyCosts;
    }
    
    public void setBuyCosts(Map<String, Integer> buy_costs)
    {
        buyCosts = buy_costs;
    }
    
    public Map<String, Integer> getRentCosts()
    {
        return rentCosts;
    }
    
    public void setRentCosts(Map<String, Integer> rent_costs)
    {
        rentCosts = rent_costs;
    }
    
    public List<String> getPermNeed()
    {
        return permNeed;
    }
    
    public void setPermNeed(List<String> perm_need)
    {
        permNeed = perm_need;
    }
    
    public List<String> getPermEarn()
    {
        return permEarn;
    }
    
    public void setPermEarn(List<String> permEarn)
    {
        this.permEarn = permEarn;
    }
    
    public List<String> getGroupNeed()
    {
        return groupNeed;
    }
    
    public void setGroupNeed(List<String> groupNeed)
    {
        this.groupNeed = groupNeed;
    }
    
    public List<String> getGroupEarn()
    {
        return groupEarn;
    }
    
    public void setGroupEarn(List<String> groupEarn)
    {
        this.groupEarn = groupEarn;
    }
    
    public boolean isRevokeGroup()
    {
        return revokeGroup;
    }
    
    public void setRevokeGroup(boolean revokeGroup)
    {
        this.revokeGroup = revokeGroup;
    }
    
    public boolean isRevokePerm()
    {
        return revokePerm;
    }
    
    public void setRevokePerm(boolean revokePerm)
    {
        this.revokePerm = revokePerm;
    }
    
    public boolean isRegrantGroup()
    {
        return regrantGroup;
    }
    
    public void setRegrantGroup(boolean regrantGroup)
    {
        this.regrantGroup = regrantGroup;
    }
    
    public boolean isRegrantPerm()
    {
        return regrantPerm;
    }
    
    public void setRegrantPerm(boolean regrantPerm)
    {
        this.regrantPerm = regrantPerm;
    }
    
    public boolean isBuyable()
    {
        return buyable;
    }
    
    public void setBuyable(boolean buyable)
    {
        this.buyable = buyable;
    }
    
    public boolean isRentable()
    {
        return rentable;
    }
    
    public void setRentable(boolean rentable)
    {
        this.rentable = rentable;
    }
    
    public long getRenttime()
    {
        return renttime;
    }
    
    public void setRentTime(long renttime)
    {
        this.renttime = renttime;
    }
    
    public List<String> getSkillsNeed()
    {
        return skillsNeed;
    }
    
    public void setSkillsNeed(List<String> skillsNeed)
    {
        this.skillsNeed = skillsNeed;
    }
    
    public List<String> getSkillsIllegal()
    {
        return skillsIllegal;
    }
    
    public void setSkillsIllegal(List<String> skillsIllegal)
    {
        this.skillsIllegal = skillsIllegal;
    }
    
    public int getSkillsNeeded()
    {
        return skillsNeeded;
    }
    
    public void setSkillsNeeded(int skillsNeeded)
    {
        this.skillsNeeded = skillsNeeded;
    }
    
    public Map<String, Integer> getBuyNeed()
    {
        return buyNeed;
    }
    
    public void setBuyNeed(Map<String, Integer> buyNeed)
    {
        this.buyNeed = buyNeed;
    }
    
    public Map<String, Integer> getRentNeed()
    {
        return rentNeed;
    }
    
    public void setRentNeed(Map<String, Integer> rentNeed)
    {
        this.rentNeed = rentNeed;
    }
    
    public List<String> getWorlds()
    {
        return worlds;
    }
    
    public void setWorlds(List<String> worlds)
    {
        this.worlds = worlds;
    }
    
}
