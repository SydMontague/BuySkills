package de.craftlancer.buyskills;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftlancer.buyskills.api.SkillHandler;
import de.craftlancer.buyskills.commands.SkillCommandHandler;
import de.craftlancer.buyskills.handlers.FoodHandler;
import de.craftlancer.buyskills.handlers.HealthHandler;
import de.craftlancer.buyskills.handlers.ItemHandler;
import de.craftlancer.buyskills.handlers.LevelHandler;
import de.craftlancer.buyskills.handlers.MoneyHandler;

/*
 * TODO JavaDocs
 * TOTEST the whole plugin (minor cases)
 * TODO extend OO, especially in Command classes
 * TODO commands from console
 */

@SuppressWarnings("rawtypes")
public class BuySkills extends JavaPlugin
{
    public static HashMap<String, SkillHandler> handlerList = new HashMap<String, SkillHandler>();
    public static Logger log = Bukkit.getLogger();
    public Permission permission;
    public HashMap<String, Skill> skills = new HashMap<String, Skill>();
    public HashSet<String> categories = new HashSet<String>();
    public FileConfiguration sConfig;
    public SkillRentTask task;
    public SkillPlayerManager pmanager;
    
    private FileConfiguration config;
    public int skillcap = 0;
    public long updatetime = 6000;
    public int skillsperpage = 5;
    
    @Override
    public void onEnable()
    {
        log = getLogger();
        
        loadConfigurations();
        pmanager = new SkillPlayerManager(this);
        SkillLanguage.loadStrings(config);
        getCommand("skill").setExecutor(new SkillCommandHandler(this));
        
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            if (config.getBoolean("general.handler.economy.activate", true))
            {
                RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null)
                    registerCurrency("money", new MoneyHandler(economyProvider.getProvider(), config.getString("general.handler.money.name", "Dollar")));
            }
            
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
                permission = permissionProvider.getProvider();
        }
        
        if (config.getBoolean("general.handler.item.activate", true))
            registerCurrency("item", new ItemHandler());
        if (config.getBoolean("general.handler.food.activate", true))
            registerCurrency("food", new FoodHandler(config.getString("general.handler.food.name", "Food")));
        if (config.getBoolean("general.handler.health.activate", true))
            registerCurrency("health", new HealthHandler(config.getString("general.handler.health.name", "Health")));
        if (config.getBoolean("general.handler.level.activate", true))
            registerCurrency("level", new LevelHandler(config.getString("general.handler.level.name", "Level")));
        if (config.getBoolean("general.handler.xp.activate", true))
            registerCurrency("xp", new LevelHandler(config.getString("general.handler.xp.name", "XP")));
        
        task = new SkillRentTask(this);
        task.runTaskTimer(this, updatetime, updatetime);
    }
    
    @Override
    public void onDisable()
    {
        task.cancel();
    }
    
    /**
     * Register a new currency to be used with this plugin
     * 
     * @param key the key, used in config for the "currency"
     * @param handler the handler object
     * @return false when something went wrong, true otherwise
     */
    public static boolean registerCurrency(String key, SkillHandler handler)
    {
        if (handler == null || handlerList.containsKey(key))
            return false;
        
        handlerList.put(key, handler);
        log.info("Registering handler for key: " + key);
        return true;
    }
    
    public SkillPlayerManager getPlayerManager()
    {
        return pmanager;
    }
    
    public void loadConfigurations()
    {
        if (!new File(getDataFolder().getPath() + File.separatorChar + "config.yml").exists())
            saveDefaultConfig();
        
        reloadConfig();
        sConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "skills.yml"));
        config = getConfig();
        
        loadConfig();
        loadSkills();
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
        
        skill.setRentTime(sConfig.getLong(key + ".renttime", 0));
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
    
    public void removeSkill(String key)
    {
        sConfig.set(key, null);
    }
    
    public Skill getSkill(String name)
    {
        return skills.get(name.toLowerCase());
    }
    
    public boolean hasSkill(String name)
    {
        return skills.containsKey(name.toLowerCase());
    }
    
    public static SkillHandler getHandler(String key)
    {
        return handlerList.get(key);
    }
    
    public static boolean hasHandler(String key)
    {
        return handlerList.containsKey(key);
    }
    
    @SuppressWarnings("unchecked")
    public static boolean hasCurrency(Player p, Map<String, Object> s)
    {
        for (Entry<String, Object> set : s.entrySet())
            if (hasHandler(set.getKey()))
                if (getHandler(set.getKey()).checkInputClass(set.getValue()))
                    if (!getHandler(set.getKey()).hasCurrency(p, set.getValue()))
                        return false;
        
        return true;
    }
    
    public static void debug(String string)
    {
        log.info(string);
    }
}
