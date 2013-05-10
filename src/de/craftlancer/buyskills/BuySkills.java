package de.craftlancer.buyskills;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftlancer.buyskills.commands.SkillCommandHandler;

/*
 * TODO try to extend OO
 * TODO extend Events + JavaDocs
 */

public class BuySkills extends JavaPlugin
{
    public static Logger log = Bukkit.getLogger();
    public Permission permission;
    
    private SkillRentTask task;
    private SkillPlayerManager pmanager;
    private FileConfiguration config;
    private FileConfiguration sConfig;
    private Map<String, Skill> skills = new HashMap<String, Skill>();
    private Set<String> categories = new HashSet<String>();
    
    private int skillcap = 0;
    private long updatetime = 6000;
    private int skillsperpage = 5;
    
    @Override
    public void onEnable()
    {
        log = getLogger();
        
        loadConfigurations();
        pmanager = new SkillPlayerManager(this);
        getCommand("skill").setExecutor(new SkillCommandHandler(this));
        
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
                permission = permissionProvider.getProvider();
        }
        
        task = new SkillRentTask(this);
        task.runTaskTimer(this, updatetime, updatetime);
    }
    
    @Override
    public void onDisable()
    {
        task.cancel();
    }
    
    /**
     * Get the player manager
     * 
     * @return the SkillPlayerManager object
     */
    public SkillPlayerManager getPlayerManager()
    {
        return pmanager;
    }
    
    /**
     * (Re)Load the config files
     */
    public void loadConfigurations()
    {
        if (!new File(getDataFolder().getPath() + File.separatorChar + "config.yml").exists())
            saveDefaultConfig();
        
        reloadConfig();
        sConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "skills.yml"));
        config = getConfig();
        
        SkillLanguage.loadStrings(config);
        loadConfig();
        loadSkills();
    }
    
    /**
     * Get the skill with the given name
     * 
     * @param name the name of the skill
     * @return the Skill object with the given name, null if no skill was found
     */
    public Skill getSkill(String name)
    {
        return skills.get(name.toLowerCase());
    }
    
    /**
     * Check if a skill with the given name exists
     * 
     * @param name the name of the skill
     * @return true when the skill exists, false if not
     */
    public boolean hasSkill(String name)
    {
        return skills.containsKey(name.toLowerCase());
    }
    
    /**
     * Get the Map of all registered skills
     * 
     * @return the Map of all registered Skills with their name as key
     */
    public Map<String, Skill> getSkillMap()
    {
        return skills;
    }
    
    /**
     * Get the Set of all registered Categories
     * 
     * @return the Set of all registered Skills
     */
    public Set<String> getCategories()
    {
        return categories;
    }
    
    /**
     * Get the maximum number of skills per player, 0 means unlimited skills
     * allowed
     * Note: the value can differ from player to player, depending on their
     * bonuscap
     * 
     * @return the maximum number of skills per player
     */
    public int getSkillCap()
    {
        return skillcap;
    }
    
    /**
     * Get the number of skills per /list page
     * 
     * @return the number of skills per /list page
     */
    public int getSkillsPerPage()
    {
        return skillsperpage;
    }
    
    private void loadConfig()
    {
        updatetime = Math.max(1, config.getLong("general.updatetime", 300)) * 20;
        skillcap = config.getInt("general.skillcap", 0);
        skillsperpage = Math.max(1, config.getInt("general.skillsperpage", 5));
    }
    
    private void loadSkills()
    {
        skills.clear();
        
        for (String key : sConfig.getKeys(false))
        {
            Skill skill = loadSkill(key);
            skills.put(skill.getName().toLowerCase(), skill);
            for (String cat : skill.getCategories())
                categories.add(cat);
        }
    }
    
    private Skill loadSkill(String key)
    {
        Skill skill = new Skill(key);
        
        skill.setName(sConfig.getString(key + ".name", ""));
        skill.setDescription(sConfig.getString(key + ".description", ""));
        skill.setInfo(sConfig.getString(key + ".info", ""));
        
        skill.setCategories(sConfig.getStringList(key + ".category"));
        skill.setPermEarn(sConfig.getStringList(key + ".perm_earn"));
        skill.setPermNeed(sConfig.getStringList(key + ".perm_need"));
        skill.setGroupEarn(sConfig.getStringList(key + ".group_earn"));
        skill.setGroupNeed(sConfig.getStringList(key + ".group_need"));
        skill.setSkillsNeed(sConfig.getStringList(key + ".skill_need"));
        skill.setSkillsIllegal(sConfig.getStringList(key + ".skill_earn"));
        skill.setWorlds(sConfig.getStringList(key + ".worlds"));
        
        skill.setRevokeGroup(sConfig.getBoolean(key + ".revoke_group", false));
        skill.setRevokePerm(sConfig.getBoolean(key + ".revoke_perm", false));
        skill.setRegrantGroup(sConfig.getBoolean(key + ".regrant_group", true));
        skill.setRegrantPerm(sConfig.getBoolean(key + ".regrant_perm", true));
        skill.setBuyable(sConfig.getBoolean(key + ".buyable", false));
        skill.setRentable(sConfig.getBoolean(key + ".rentable", false));
        
        skill.setRentTime(sConfig.getLong(key + ".renttime", 0) * 1000);
        skill.setSkillsNeeded(sConfig.getInt(key + ".skills_needed", 0));
        
        HashMap<String, Object> buyHelpMap = new HashMap<String, Object>();
        HashMap<String, Object> rentHelpMap = new HashMap<String, Object>();
        
        HashMap<String, Object> buyNeedHelpMap = new HashMap<String, Object>();
        HashMap<String, Object> rentNeedHelpMap = new HashMap<String, Object>();
        
        for (String vkey : sConfig.getConfigurationSection(key + ".buy_costs").getKeys(false))
            buyHelpMap.put(vkey, sConfig.get(key + ".buy_costs." + vkey));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".rent_costs").getKeys(false))
            rentHelpMap.put(vkey, sConfig.get(key + ".rent_costs." + vkey));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".buy_need").getKeys(false))
            buyNeedHelpMap.put(vkey, sConfig.get(key + ".buy_need." + vkey));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".rent_need").getKeys(false))
            rentNeedHelpMap.put(vkey, sConfig.get(key + ".rent_need." + vkey));
        
        skill.setBuyCosts(buyHelpMap);
        skill.setRentCosts(rentHelpMap);
        skill.setBuyNeed(buyNeedHelpMap);
        skill.setRentNeed(rentNeedHelpMap);
        
        return skill;
    }
}
