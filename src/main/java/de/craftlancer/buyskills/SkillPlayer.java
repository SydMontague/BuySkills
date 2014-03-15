package de.craftlancer.buyskills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.craftlancer.currencyhandler.CurrencyHandler;

/**
 * Represents a Player
 */
public class SkillPlayer
{
    private boolean hasChanged = false;
    private BuySkills plugin;
    private String name;
    private List<String> skills = new ArrayList<String>();
    private HashMap<String, Long> rented = new HashMap<String, Long>();
    private int bonuscap = 0;
    private File file;
    private FileConfiguration conf;
    
    public SkillPlayer(BuySkills plugin, String name, List<String> skills, HashMap<String, Long> rented, int bonuscap)
    {
        this.plugin = plugin;
        this.name = name;
        this.skills = skills;
        this.rented = rented;
        this.bonuscap = bonuscap;
        this.file = new File(plugin.getDataFolder(), "players" + File.separator + getName() + ".yml");
        this.conf = YamlConfiguration.loadConfiguration(file);
    }
    
    /**
     * Get all skills (rented and bought) of this player
     * 
     * @return all the player's owned skills
     */
    public List<String> getSkills()
    {
        List<String> fin = new ArrayList<String>();
        fin.addAll(skills);
        fin.addAll(rented.keySet());
        
        return fin;
    }
    
    /**
     * Check if a player has a certain skill
     * 
     * @param string
     *            the name of the skill
     * @return true when he has the skill, false when not
     */
    public boolean hasSkill(String string)
    {
        return getSkills().contains(string.toLowerCase());
    }
    
    /**
     * Get all rented skills of this player
     * 
     * @return all rented skill of this player
     */
    public HashMap<String, Long> getRented()
    {
        return rented;
    }
    
    /**
     * Get all bought skills of this player
     * 
     * @return all bought skills of this player
     */
    public List<String> getBoughtSkills()
    {
        return skills;
    }
    
    /**
     * Get the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @return the bonuscap
     */
    public int getBonusCap()
    {
        return bonuscap;
    }
    
    /**
     * Set the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @param bonuscap
     *            the amount of bonusskills
     */
    public void setBonusCap(int bonuscap)
    {
        if (this.bonuscap != bonuscap)
            hasChanged = true;
        
        this.bonuscap = bonuscap;
    }
    
    /**
     * Get the name of the player
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * Check if a player has the needed permissions to buy this skill
     * 
     * @param s
     *            the skill
     * @return true if he has the permissions, false if not
     */
    public boolean hasPermNeed(Skill s)
    {
        Player player = plugin.getServer().getPlayerExact(getName());
        
        if (player == null)
            return false;
        
        for (String str : s.getPermNeed())
            if (!player.hasPermission(str))
                return false;
        
        return true;
    }
    
    /**
     * Check if a player has the needed groups to buy this skill
     * 
     * @param skill
     *            the skill
     * @return true if he has the groups, false if not
     */
    public boolean hasGroupNeed(Skill skill)
    {
        Player player = plugin.getServer().getPlayerExact(getName());
        
        if (player == null)
            return false;
        
        for (String str : skill.getGroupNeed())
            if (!plugin.getPermissions().playerInGroup(player, str))
                return false;
        
        return true;
    }
    
    /**
     * Check if a player has the needed skilltree requirements to buy this skill
     * 
     * @param skill
     *            the skill
     * @return true if he has the requirements, false if not
     */
    public boolean followsSkilltree(Skill skill)
    {
        for (String str : skill.getSkillsIllegal())
            if (getSkills().contains(str))
                return false;
        
        int i = 0;
        
        for (String str : skill.getSkillsNeed())
            if (getSkills().contains(str))
                i++;
        
        if (i < skill.getSkillsNeeded())
            return false;
        
        return true;
    }
    
    /**
     * Checks if a skill is available for this player
     * 
     * @param skill
     *            the skill
     * @return true if it is available, false if not
     */
    public boolean skillAvaible(Skill skill)
    {
        if (!hasPermNeed(skill))
            return false;
        
        if (!hasGroupNeed(skill))
            return false;
        
        if (!followsSkilltree(skill))
            return false;
        
        if (getSkills().contains(skill.getName()))
            return false;
        
        return true;
    }
    
    /**
     * Grant a bought skill
     * 
     * @param skill
     *            the skill
     */
    public void grantSkill(Skill skill)
    {
        addSkill(skill.getKey().toLowerCase());
        handleSkillGrant(skill);
    }
    
    /**
     * Revoke a bought skill
     * 
     * @param skill
     *            the skill
     */
    public void revokeSkill(Skill skill)
    {
        removeSkill(skill.getKey().toLowerCase());
        handleSkillRevoke(skill);
    }
    
    /**
     * Grant a rented skill
     * 
     * @param skill
     *            the skill
     * @param time
     *            the time in ms, the skill is granted for
     */
    public void grantRented(Skill skill, long time)
    {
        addRentedSkill(skill.getKey().toLowerCase(), System.currentTimeMillis() + time);
        handleSkillGrant(skill);
    }
    
    /**
     * Revoke a rented skill
     * 
     * @param skill
     *            the skill
     */
    public void revokeRented(Skill skill)
    {
        removeRentedSkill(skill.getKey().toLowerCase());
        handleSkillRevoke(skill);
    }
    
    /**
     * Handle the permission side of granting a skill
     * 
     * @param skill
     *            the skill
     */
    public void handleSkillGrant(Skill skill)
    {
        List<String> worlds;
        if (skill.getWorlds().isEmpty())
        {
            worlds = new ArrayList<String>();
            // add null world to force use of global permissions
            worlds.add((String) null);
            
        }
        else
            worlds = skill.getWorlds();
        
        for (String world : worlds)
        {
            for (String permission : skill.getPermEarn())
                plugin.getPermissions().playerAdd(world, getName(), permission);
            
            for (String group : skill.getGroupEarn())
                plugin.getPermissions().playerAddGroup(world, getName(), group);
            
            if (skill.isRevokeGroup())
                for (String group : skill.getGroupNeed())
                    plugin.getPermissions().playerRemoveGroup(world, getName(), group);
            
            if (skill.isRevokePerm())
                for (String perm : skill.getPermNeed())
                    plugin.getPermissions().playerRemove(world, getName(), perm);
        }
    }
    
    /**
     * Handle the permission side of revoking a skill
     * 
     * @param player
     *            the player
     * @param key
     *            the skill
     */
    private void handleSkillRevoke(Skill s)
    {
        List<String> worlds;
        if (s.getWorlds().isEmpty())
        {
            worlds = new ArrayList<String>();
            worlds.add((String) null);
        }
        else
            worlds = s.getWorlds();
        
        for (String world : worlds)
        {
            for (String permission : s.getPermEarn())
                plugin.getPermissions().playerRemove(world, getName(), permission);
            
            for (String group : s.getGroupEarn())
                plugin.getPermissions().playerRemoveGroup(world, getName(), group);
            
            if (s.isRegrantGroup())
                for (String group : s.getGroupNeed())
                    plugin.getPermissions().playerAddGroup(world, getName(), group);
            
            if (s.isRegrantPerm())
                for (String perm : s.getPermNeed())
                    plugin.getPermissions().playerAdd(world, getName(), perm);
            
            if (s.isRegrantCost())
                CurrencyHandler.giveCurrencies(plugin.getServer().getPlayerExact(getName()), s.getRentCosts());
        }
    }
    
    private void removeRentedSkill(String s)
    {
        rented.remove(s);
    }
    
    private void addSkill(String s)
    {
        hasChanged = true;
        skills.add(s);
    }
    
    private void removeSkill(String s)
    {
        hasChanged = true;
        skills.remove(s);
    }
    
    private void addRentedSkill(String s, long l)
    {
        rented.put(s, l);
    }
    
    protected void save()
    {
        if (!hasChanged)
            return;
        
        if (getBoughtSkills().isEmpty() && getBonusCap() == 0 && file.exists())
        {
            file.delete();
            return;
        }
        
        conf.set("skills", getBoughtSkills());
        conf.set("bonuscap", getBonusCap());
        
        try
        {
            conf.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        hasChanged = false;
    }
    
    protected void saveRented()
    {
        plugin.getRentedConfig().set(getName(), getRented());
    }
}
