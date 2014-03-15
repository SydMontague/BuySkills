package de.craftlancer.buyskills;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 * Manages the load, save and editing of SkillPlayers
 */
public class SkillPlayerManager
{
    private HashMap<String, SkillPlayer> playerMap = new HashMap<String, SkillPlayer>();
    private BuySkills plugin;
    private FileConfiguration rentedConfig;
    private File rentedFile;
    
    public SkillPlayerManager(BuySkills plugin)
    {
        this.plugin = plugin;
        
        rentedFile = new File(plugin.getDataFolder(), "rented.yml");
        if (!rentedFile.exists())
            try
            {
                rentedFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        rentedConfig = YamlConfiguration.loadConfiguration(rentedFile);
    }
    
    /**
     * Get the skills of the given Player
     * 
     * @param p the Player
     * @return a List of all owned Skills
     */
    public List<String> getSkills(Player p)
    {
        return getSkills(p.getName());
    }
    
    /**
     * Get the skills of the given Player
     * 
     * @param p the name of the player
     * @return a List of all owned Skills
     */
    public List<String> getSkills(String p)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        return playerMap.get(p).getSkills();
    }
    
    /**
     * Get all bought skills of the given Player
     * 
     * @param p the Player
     * @return a List of all bought Skills
     */
    public List<String> getBoughtSkills(Player p)
    {
        return getBoughtSkills(p.getName());
    }
    
    /**
     * Get all bought of the given Player
     * 
     * @param p the Player
     * @return a List of all bought Skills
     */
    public List<String> getBoughtSkills(String p)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        return playerMap.get(p).getBoughtSkills();
    }
    
    /**
     * Get all rented of the given Player
     * 
     * @param p the Player
     * @return a Map of all rented Skills and the time, when the rent ends
     */
    public Map<String, Long> getRentedSkills(Player p)
    {
        return getRentedSkills(p.getName());
    }
    
    /**
     * Get all rented of the given Player
     * 
     * @param p the Player
     * @return a Map of all rented Skills and the time, when the rent ends
     */
    public Map<String, Long> getRentedSkills(String p)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        return playerMap.get(p).getRented();
    }
    
    /**
     * Revoke a rented skill
     * 
     * @param p the player
     * @param key the skill
     */
    public void revokeRented(String p, String key)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        playerMap.get(p).removeRentedSkill(key.toLowerCase());
        savePlayer(p);
        handleSkillRevoke(p, key);
    }
    
    /**
     * Revoke a bought skill
     * 
     * @param p the player
     * @param key the skill
     */
    public void revokeSkill(String p, String key)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        playerMap.get(p).removeSkill(key.toLowerCase());
        savePlayer(p);
        handleSkillRevoke(p, key);
    }
    
    /**
     * Grant a bought skill
     * 
     * @param p the player
     * @param s the skill
     */
    public void grantSkill(Player p, Skill s)
    {
        grantSkill(p.getName(), s.getName());
    }
    
    /**
     * Grant a bought skill
     * 
     * @param p the player
     * @param s the skill
     */
    public void grantSkill(String p, String s)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        playerMap.get(p).addSkill(s.toLowerCase());
        savePlayer(p);
        handleSkillGrant(p, s);
    }
    
    /**
     * Grant a rented skill
     * 
     * @param p the player
     * @param s the skill
     */
    public void grantRented(Player p, Skill s, long time)
    {
        grantRented(p.getName(), s.getName(), time);
    }
    
    /**
     * Grant a rented skill
     * 
     * @param p the player
     * @param s the skill
     */
    public void grantRented(String p, String s, long time)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        playerMap.get(p).addRented(s.toLowerCase(), System.currentTimeMillis() + time);
        savePlayer(p);
        handleSkillGrant(p, s);
    }
    
    /**
     * Handle the permission side of revoking a skill
     * 
     * @param player the player
     * @param key the skill
     */
    public void handleSkillRevoke(String player, String key)
    {
        Skill s = plugin.getSkill(key);
        
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
                plugin.getPermissions().playerRemove(world, player, permission);
            
            for (String group : s.getGroupEarn())
                plugin.getPermissions().playerRemoveGroup(world, player, group);
            
            if (s.isRegrantGroup())
                for (String group : s.getGroupNeed())
                    plugin.getPermissions().playerAddGroup(world, player, group);
            
            if (s.isRegrantPerm())
                for (String perm : s.getPermNeed())
                    plugin.getPermissions().playerAdd(world, player, perm);
            
            if (s.isRegrantCost())
                SkillUtils.give(plugin.getServer().getPlayerExact(player), s.getRentCosts().entrySet());
        }
    }
    
    /**
     * Handle the permission side of granting a skill
     * 
     * @param player the player
     * @param key the skill
     */
    public void handleSkillGrant(String player, String key)
    {
        Skill s = plugin.getSkill(key);
        
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
                plugin.getPermissions().playerAdd(world, player, permission);
            
            for (String group : s.getGroupEarn())
                plugin.getPermissions().playerAddGroup(world, player, group);
            
            if (s.isRevokeGroup())
                for (String group : s.getGroupNeed())
                    plugin.getPermissions().playerRemoveGroup(world, player, group);
            
            if (s.isRevokePerm())
                for (String perm : s.getPermNeed())
                    plugin.getPermissions().playerRemove(world, player, perm);
        }
    }
    
    /**
     * Get the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @param player the player
     * @return the bonuscap
     */
    public int getBonusCap(Player player)
    {
        return getBonusCap(player.getName());
    }
    
    /**
     * Get the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @param player the player
     * @return the bonuscap
     */
    public int getBonusCap(String player)
    {
        if (!playerMap.containsKey(player))
            loadPlayer(player);
        
        return playerMap.get(player).getBonusCap();
    }
    
    /**
     * Check if a player has the needed permissions to buy this skill
     * 
     * @param p the player
     * @param s the skill
     * @return true if he has the permissions, false if not
     */
    public boolean hasPermNeed(Player p, Skill s)
    {
        for (String str : s.getPermNeed())
            if (!p.hasPermission(str))
                return false;
        
        return true;
    }
    
    /**
     * Check if a player has the needed groups to buy this skill
     * 
     * @param p the player
     * @param s the skill
     * @return true if he has the groups, false if not
     */
    public boolean hasGroupNeed(Player p, Skill s)
    {
        for (String str : s.getGroupNeed())
            if (!plugin.getPermissions().playerInGroup(p, str))
                return false;
        
        return true;
    }
    
    /**
     * Check if a player has the needed skilltree requirements to buy this skill
     * 
     * @param p the player
     * @param s the skill
     * @return true if he has the requirements, false if not
     */
    public boolean followsSkilltree(Player p, Skill s)
    {
        for (String str : s.getSkillsIllegal())
            if (plugin.getPlayerManager().getSkills(p).contains(str))
                return false;
        
        int i = 0;
        
        for (String str : s.getSkillsNeed())
            if (plugin.getPlayerManager().getSkills(p).contains(str))
                i++;
        
        if (i < s.getSkillsNeeded())
            return false;
        
        return true;
    }
    
    /**
     * Checks if a skill is available for this player
     * 
     * @param p the player
     * @param s the skill
     * @return true if it is available, false if not
     */
    public boolean skillAvaible(Player p, Skill s)
    {
        if (!hasPermNeed(p, s))
            return false;
        
        if (!hasGroupNeed(p, s))
            return false;
        
        if (!followsSkilltree(p, s))
            return false;
        
        if (plugin.getPlayerManager().getSkills(p).contains(s.getName()))
            return false;
        
        return true;
    }
    
    private void loadPlayer(String p)
    {
        File file = new File(plugin.getDataFolder(), "players" + File.separator + p + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
        
        List<String> skills = conf.getStringList("skills");
        int bonuscap = conf.getInt("bonuscap", 0);
        HashMap<String, Long> rent = new HashMap<String, Long>();
        
        if (rentedConfig.getConfigurationSection(p) != null)
            for (String key : rentedConfig.getConfigurationSection(p).getKeys(false))
                rent.put(key, rentedConfig.getLong(p + "." + key));
        
        playerMap.put(p, new SkillPlayer(p, skills, rent, bonuscap));
    }
    
    private void savePlayer(String p)
    {
        savePlayer(playerMap.get(p));
    }
    
    private void savePlayer(SkillPlayer p)
    {
        File file = new File(plugin.getDataFolder(), "players" + File.separator + p.getName() + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
        conf.set("skills", p.getBoughtSkills());
        conf.set("bonuscap", p.getBonusCap());
        
        rentedConfig.set(p.getName(), p.getRented());
        
        try
        {
            conf.save(file);
            rentedConfig.save(rentedFile);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
