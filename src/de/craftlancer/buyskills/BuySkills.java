package de.craftlancer.buyskills;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class BuySkills extends JavaPlugin
{
    public static HashMap<String, SkillHandler> handlerList = new HashMap<String, SkillHandler>();
    public static Logger log = Bukkit.getLogger();
    public HashMap<String, Skill> skills = new HashMap<String, Skill>();
    public FileConfiguration sConfig;
    public Permission permission;
    public RentTask task;
    
    private FileConfiguration config;
    public String currency = "Dollar";
    public int skillcap = 0;
    public long updatetime = 300;
    
    @Override
    public void onEnable()
    {
        log = getLogger();
        config = getConfig();
        sConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "skills.yml"));
        
        loadConfig();
        skills = loadSkills();
        
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            if (economyProvider != null)
                registerCurrency("money", new MoneyHandler(economyProvider.getProvider()));
            
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
                permission = permissionProvider.getProvider();
        }
        
        task = new RentTask();
        task.runTaskTimer(this, 300, 300);
    }
    
    @Override
    public void onDisable()
    {
        task.cancel();
    }
    
    /**
     * Register a new currency to be used with this plugin
     * 
     * @param key
     *            the key, used in config for the "currency"
     * @param handler
     *            the handler object
     * @return false when something went wrong, true otherwise
     */
    public static boolean registerCurrency(String key, SkillHandler handler)
    {
        if (handler == null || handlerList.containsKey(key))
            return false;
        
        handlerList.put(key, handler);
        Bukkit.getLogger().info("[BuySkills] Registering handler for key: " + key);
        return true;
    }
    
    private void loadConfig()
    {
        currency = config.getString("currency", "Dollar");
        updatetime = config.getLong("updatetime", 300);
        skillcap = config.getInt("skillcap", 0);
    }
    
    private HashMap<String, Skill> loadSkills()
    {
        HashMap<String, Skill> map = new HashMap<String, Skill>();
        
        for (String key : sConfig.getKeys(false))
        {
            Skill skill = loadSkill(key);
            map.put(skill.getName(), skill);
        }
        
        return map;
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
        skill.setRentDiscount(sConfig.getBoolean(key + ".rentdiscount", false));
        
        skill.setRentTime(sConfig.getLong(key + ".renttime", 0));
        skill.setMaxTime(sConfig.getLong(key + ".maxrenttime", 0));
        skill.setMinTime(sConfig.getLong(key + ".minrenttime", 0));
        skill.setSkillsNeeded(sConfig.getInt(key + ".skills_needed", 0));
        
        HashMap<String, Integer> buyHelpMap = new HashMap<String, Integer>();
        HashMap<String, Integer> rentHelpMap = new HashMap<String, Integer>();
        
        HashMap<String, Integer> buyNeedHelpMap = new HashMap<String, Integer>();
        HashMap<String, Integer> rentNeedHelpMap = new HashMap<String, Integer>();
        
        for (String vkey : sConfig.getConfigurationSection(key + ".buy_costs").getKeys(false))
            buyHelpMap.put(vkey, sConfig.getInt(key + ".buy_costs." + vkey, 0));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".rent_costs").getKeys(false))
            rentHelpMap.put(vkey, sConfig.getInt(key + ".rent_costs." + vkey, 0));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".buy_need").getKeys(false))
            buyNeedHelpMap.put(vkey, sConfig.getInt(key + ".buy_need." + vkey, 0));
        
        for (String vkey : sConfig.getConfigurationSection(key + ".rent_need").getKeys(false))
            rentNeedHelpMap.put(vkey, sConfig.getInt(key + ".rent_need." + vkey, 0));
        
        skill.setBuyCosts(buyHelpMap);
        skill.setRentCosts(rentHelpMap);
        skill.setBuyNeed(buyNeedHelpMap);
        skill.setRentNeed(rentNeedHelpMap);
        
        return skill;
    }
    
    public void saveSkill(String key, Skill s)
    {
        if(s == null)
        {
            sConfig.set(key, null);
            return;
        }
        
        sConfig.set(key + ".name", s.getName());
        sConfig.set(key + ".description", s.getDescription());
        sConfig.set(key + ".info", s.getInfo());
        sConfig.set(key + ".category", s.getCategories());
        sConfig.set(key + ".perm_need", s.getPermNeed());
        sConfig.set(key + ".perm_earn", s.getPermEarn());
        sConfig.set(key + ".group_need", s.getGroupNeed());
        sConfig.set(key + ".group_earn", s.getGroupEarn());
        sConfig.set(key + ".worlds", s.getWorlds());
        sConfig.set(key + ".revoke_group", s.isRevokeGroup());
        sConfig.set(key + ".revoke_perm", s.isRevokePerm());
        sConfig.set(key + ".regrant_group", s.isRegrantGroup());
        sConfig.set(key + ".regrant_perm", s.isRegrantPerm());
        sConfig.set(key + ".buyable", s.isBuyable());
        sConfig.set(key + ".rentable", s.isRentable());
        sConfig.set(key + ".rentdiscount", s.isRentdiscount());
        sConfig.set(key + ".renttime", s.getRenttime());
        sConfig.set(key + ".maxrenttime", s.getMaxtime());
        sConfig.set(key + ".minrenttime", s.getMintime());
        sConfig.set(key + ".skills_needed", s.getSkillsNeeded());
        sConfig.set(key + ".skill_need", s.getSkillsNeed());
        sConfig.set(key + ".skill_illegal", s.getSkillsIllegal());
        
        for (Entry<String, Integer> k : s.getBuyCosts().entrySet())
            sConfig.set(k.getKey(), k.getValue());
                
        for (Entry<String, Integer> k : s.getRentCosts().entrySet())
            sConfig.set(k.getKey(), k.getValue());
                
        for (Entry<String, Integer> k : s.getBuyNeed().entrySet())
            sConfig.set(k.getKey(), k.getValue());
                
        for (Entry<String, Integer> k : s.getRentNeed().entrySet())
            sConfig.set(k.getKey(), k.getValue());             
    }
}
