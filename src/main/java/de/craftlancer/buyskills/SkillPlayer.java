package de.craftlancer.buyskills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import de.craftlancer.currencyhandler.CurrencyHandler;

/**
 * Represents a Player
 */
public class SkillPlayer
{
    private final OfflinePlayer offlinePlayer;
    private final UUID uuid;
    private boolean hasChanged = false;
    private final BuySkills plugin;
    private final String name;
    private List<String> skills = new ArrayList<String>();
    private HashMap<String, Long> rented = new HashMap<String, Long>();
    private int bonuscap = 0;
    private final File file;
    private final FileConfiguration conf;
    
    @SuppressWarnings("deprecation")
    public SkillPlayer(BuySkills plugin, UUID uuid, List<String> skills, HashMap<String, Long> rented, int bonuscap)
    {
        this.plugin = plugin;
        this.offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
        this.uuid = uuid;
        this.skills = skills;
        this.rented = rented;
        this.bonuscap = bonuscap;
        this.name = offlinePlayer.getName();
        this.file = new File(plugin.getDataFolder(), "players" + File.separator + getUUID().toString() + ".yml");
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
     *        the name of the skill
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
     *        the amount of bonusskills
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
    
    public UUID getUUID()
    {
        return uuid;
    }
    
    public OfflinePlayer getPlayer()
    {
        return offlinePlayer;
    }
    
    /**
     * Check if a player has the needed permissions to buy this skill
     * 
     * @param s
     *        the skill
     * @return true if he has the permissions, false if not
     */
    public boolean hasPermNeed(Skill s)
    {
        Player player = plugin.getServer().getPlayer(getUUID());
        
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
     *        the skill
     * @return true if he has the groups, false if not
     */
    public boolean hasGroupNeed(Skill skill)
    {
        Player player = plugin.getServer().getPlayer(getUUID());
        
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
     *        the skill
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
     *        the skill
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
     *        the skill
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
     *        the skill
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
     *        the skill
     * @param time
     *        the time in ms, the skill is granted for
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
     *        the skill
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
     *        the skill
     */
    public void handleSkillGrant(Skill skill)
    {
        List<String> worlds;
        if (skill.getWorlds().isEmpty())
        {
            worlds = new ArrayList<String>();
            // add null world to force use of global permissions
            worlds.add(null);
            
        }
        else
            worlds = skill.getWorlds();
        
        for (String world : worlds)
        {
            for (String permission : skill.getPermEarn())
                plugin.getPermissions().playerAdd(world, getPlayer(), permission);
            
            for (String group : skill.getGroupEarn())
                plugin.getPermissions().playerAddGroup(world, getPlayer(), group);
            
            if (skill.isRevokeGroup())
                for (String group : skill.getGroupNeed())
                    plugin.getPermissions().playerRemoveGroup(world, getPlayer(), group);
            
            if (skill.isRevokePerm())
                for (String perm : skill.getPermNeed())
                    plugin.getPermissions().playerRemove(world, getPlayer(), perm);
        }
    }

    /**
     * Handle the permission side of revoking a skill
     *
     * @param skill
     *        the skill
     */
    private void handleSkillRevoke(Skill skill)
    {
        List<String> worlds;
        if (skill.getWorlds().isEmpty())
        {
            worlds = new ArrayList<String>();
            worlds.add(null);
        }
        else
            worlds = skill.getWorlds();
        
        for (String world : worlds)
        {
            for (String permission : skill.getPermEarn())
                plugin.getPermissions().playerRemove(world, getPlayer(), permission);
            
            for (String group : skill.getGroupEarn())
                plugin.getPermissions().playerRemoveGroup(world, getPlayer(), group);
            
            if (skill.isRegrantGroup())
                for (String group : skill.getGroupNeed())
                    plugin.getPermissions().playerAddGroup(world, getPlayer(), group);
            
            if (skill.isRegrantPerm())
                for (String perm : skill.getPermNeed())
                    plugin.getPermissions().playerAdd(world, getPlayer(), perm);
            
            if (skill.isRegrantCost())
                CurrencyHandler.giveCurrencies(plugin.getServer().getPlayer(getUUID()), skill.getRentCosts());
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
            if (!file.delete())
                plugin.getLogger().severe("Failed to delete player File!");
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
        plugin.getRentedConfig().set(getUUID().toString(), getRented());
    }
}
