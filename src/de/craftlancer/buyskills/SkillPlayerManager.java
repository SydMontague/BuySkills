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

public class SkillPlayerManager
{
    public HashMap<String, SkillPlayer> playerMap = new HashMap<String, SkillPlayer>();
    private BuySkills plugin;
    private FileConfiguration rentedConfig;
    private File rentedFile;
    
    public SkillPlayerManager(BuySkills plugin)
    {
        this.plugin = plugin;
        rentedFile = new File(plugin.getDataFolder(), "rented.yml");
        rentedConfig = YamlConfiguration.loadConfiguration(rentedFile);
    }
    
    private void loadPlayer(String p)
    {
        FileConfiguration conf = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players" + File.separator + p + ".yml"));
        
        List<String> skills = conf.getStringList("skills");
        int bonuscap = conf.getInt("bonuscap", 0);
        HashMap<String, Long> rent = new HashMap<String, Long>();
        
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
        conf.set("skills", p.getSkills());
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
    
    public List<String> getSkills(Player p)
    {
        return getSkills(p.getName());
    }
    
    public List<String> getSkills(String p)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        return playerMap.get(p).getSkills();
    }
    
    public Map<String, Long> getRentedSkills(Player p)
    {
        return getRentedSkills(p.getName());
    }
    
    public Map<String, Long> getRentedSkills(String p)
    {
        if (!playerMap.containsKey(p))
            loadPlayer(p);
        
        return playerMap.get(p).getRented();
    }
    
    public void revokeRented(String p, String key)
    {
        playerMap.get(p).removeRentedSkill(key);
        savePlayer(p);
        handleSkillRevoke(p, key);
    }
    
    public void revokeSkill(String p, String key)
    {
        playerMap.get(p).removeSkill(key);
        savePlayer(p);
        handleSkillRevoke(p, key);
    }
    
    public void grantSkill(Player p, Skill s)
    {
        grantSkill(p.getName(), s.getName());
    }
    
    public void grantSkill(String p, String s)
    {
        playerMap.get(p).addSkill(s);
        savePlayer(p);
        handleSkillGrant(p, s);
    }
    
    public void grantRented(Player p, Skill s, long time)
    {
        grantRented(p.getName(), s.getName(), time);
    }
    
    public void grantRented(String p, String s, long time)
    {
        playerMap.get(p).addRented(s, System.currentTimeMillis() + time);
        savePlayer(p);
        handleSkillGrant(p, s);
    }
    
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
                plugin.permission.playerRemove(world, player, permission);
            
            for (String group : s.getGroupEarn())
                plugin.permission.playerRemoveGroup(world, player, group);
            
            if (s.isRegrantGroup())
                for (String group : s.getGroupNeed())
                    plugin.permission.playerAddGroup(world, player, group);
            
            if (s.isRegrantPerm())
                for (String perm : s.getPermNeed())
                    plugin.permission.playerAdd(world, player, perm);
        }
    }
    
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
                plugin.permission.playerAdd(world, player, permission);
            
            for (String group : s.getGroupEarn())
                plugin.permission.playerAddGroup(world, player, group);
            
            if (s.isRevokeGroup())
                for (String group : s.getGroupNeed())
                    plugin.permission.playerRemoveGroup(world, player, group);
            
            if (s.isRevokePerm())
                for (String perm : s.getPermNeed())
                    plugin.permission.playerRemove(world, player, perm);
        }
    }
    
    public int getBonusCap(Player player)
    {
        return getBonusCap(player.getName());
    }
    
    public int getBonusCap(String name)
    {
        return playerMap.get(name).getBonusCap();
    }
    
    public boolean hasPermNeed(Player p, Skill s)
    {
        for (String str : s.getPermNeed())
            if (!p.hasPermission(str))
                return false;
        
        return true;
    }
    
    public boolean hasGroupNeed(Player p, Skill s)
    {
        for (String str : s.getGroupNeed())
            if (!plugin.permission.playerInGroup(p, str))
                return false;
        
        return true;
    }
    
    public boolean followsSkilltree(Player p, Skill s)
    {
        for (String str : s.getSkillsIllegal())
            if (plugin.getPlayerManager().getSkills(p).contains(str))
                return false;
        
        for (String str : s.getSkillsNeed())
            if (!plugin.getPlayerManager().getSkills(p).contains(str))
                return false;
        
        return true;
    }
    
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
    
    public List<String> getBoughtSkills(String string)
    {
        return playerMap.get(string).getBoughtSkills();
    }
}
