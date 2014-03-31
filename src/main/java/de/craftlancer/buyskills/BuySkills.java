package de.craftlancer.buyskills;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import de.craftlancer.buyskills.commands.SkillCommandHandler;

/*
 * TODO extend Events + JavaDocs
 */

public class BuySkills extends JavaPlugin
{
    private static BuySkills instance;
    private Permission permission;
    
    private FileConfiguration config;
    private FileConfiguration sConfig;
    private FileConfiguration rentedConfig;
    private File rentedFile;
    private final Map<String, Skill> skills = new HashMap<String, Skill>();
    private final Map<String, Skill> skillsByKey = new HashMap<String, Skill>();
    private final HashMap<UUID, SkillPlayer> playerMap = new HashMap<UUID, SkillPlayer>();
    private final Set<String> categories = new HashSet<String>();
    
    private int skillcap = 0;
    private long updatetime = 6000L;
    private long saveTime = 12000L;
    private int skillsperpage = 5;
    
    {
        instance = this;
    }
    
    @Override
    public void onEnable()
    {
        loadConfigurations();
        getCommand("skill").setExecutor(new SkillCommandHandler(this));
        
        if (getServer().getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
                permission = permissionProvider.getProvider();
        }
        
        new SkillRentTask(this).runTaskTimer(this, updatetime, updatetime);
        new SkillSaveTask(this).runTaskTimer(this, saveTime, saveTime);
        
        try
        {
            Metrics metrics = new Metrics(this);
            metrics.start();
        }
        catch (IOException e)
        {
            getLogger().info("Error while loading Metrics");
        }
    }
    
    @Override
    public void onDisable()
    {
        getServer().getScheduler().cancelTasks(this);
        save();
    }
    
    /**
     * Get a SkillPlayer by his Player object
     * 
     * @param player
     *        the player
     * @return the SkillPlayer
     */
    public SkillPlayer getSkillPlayer(Player player)
    {
        return getSkillPlayer(player.getUniqueId());
    }
    
    /**
     * Get a SkillPlayer by his name
     * 
     * @param name
     *        the name
     * @return the SkillPlayer
     */
    @SuppressWarnings("deprecation")
    public SkillPlayer getSkillPlayer(String name)
    {
        return getSkillPlayer(getServer().getOfflinePlayer(name).getUniqueId());
    }
    
    public SkillPlayer getSkillPlayer(UUID uuid)
    {
        if (!playerMap.containsKey(uuid))
            loadPlayer(uuid);
        
        return playerMap.get(uuid);
    }
    
    /**
     * Get the skill with the given name
     * 
     * @param name
     *        the name of the skill
     * @return the Skill object with the given name, null if no skill was found
     */
    public Skill getSkill(String name)
    {
        return skills.get(name.toLowerCase());
    }
    
    /**
     * Get the skill with the given key
     * 
     * @param key
     *        the key of the skill
     * @return the Skill object with the given key, null if no skill was found
     */
    public Skill getSkillByKey(String key)
    {
        return skillsByKey.get(key);
    }
    
    /**
     * Check if a skill with the given name exists
     * 
     * @param name
     *        the name of the skill
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
    
    /**
     * Get the plugin's instance
     * 
     * @return the instance of the plugin
     */
    public static BuySkills getInstance()
    {
        return instance;
    }
    
    /**
     * Get a Collection of all active SkillPlayers
     * SkillPlayers are loaded lazy into this collection, meaning they are only
     * loaded if they are actually needed.
     * 
     * @return a Collection of SkillPlayers
     */
    public Collection<SkillPlayer> getSkillPlayers()
    {
        return playerMap.values();
    }
    
    /**
     * (Re)Load the config files
     */
    public void loadConfigurations()
    {
        if (!new File(getDataFolder(), "config.yml").exists())
            saveDefaultConfig();
        
        if (!new File(getDataFolder(), "skills.yml").exists())
            saveResource("skills.yml", false);
        
        reloadConfig();
        sConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "skills.yml"));
        config = getConfig();
        
        if (!config.getBoolean("general.uuidUpdated", false))
            updateToUUID();
        
        loadConfig();
        loadSkills();
        
        rentedFile = new File(getDataFolder(), "rented.yml");
        if (!rentedFile.exists())
            try
            {
                if (!rentedFile.createNewFile())
                    getLogger().info("Failed to load rentedFile");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        rentedConfig = YamlConfiguration.loadConfiguration(rentedFile);
    }
    
    private void updateToUUID()
    {
        for (File file : new File(getDataFolder(), "players").listFiles())
        {
            String name = file.getName();
            name = name.substring(0, name.lastIndexOf("."));
            
            try
            {
                UUID.fromString(name);
            }
            catch (IllegalArgumentException e)
            {
                file.renameTo(new File(getDataFolder(), "players" + File.separator + getServer().getOfflinePlayer(name).getUniqueId().toString() + ".yml"));
            }
        }
        
        config.set("general.uuidUpdated", true);
        saveConfig();
    }
    
    protected FileConfiguration getRentedConfig()
    {
        return rentedConfig;
    }
    
    protected File getRentedFile()
    {
        return rentedFile;
    }
    
    protected void save()
    {
        for (SkillPlayer skillPlayer : getSkillPlayers())
        {
            skillPlayer.save();
            skillPlayer.saveRented();
        }
        
        try
        {
            getRentedConfig().save(getRentedFile());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    protected Permission getPermissions()
    {
        return permission;
    }
    
    private void loadSkills()
    {
        skills.clear();
        
        for (String key : sConfig.getKeys(false))
        {
            Skill skill = new Skill(key, sConfig);
            skills.put(skill.getName().toLowerCase(), skill);
            skillsByKey.put(key, skill);
            for (String cat : skill.getCategories())
                categories.add(cat);
        }
    }
    
    private void loadConfig()
    {
        updatetime = Math.max(1, config.getLong("general.updatetime", 300)) * 20;
        saveTime = Math.max(1, config.getLong("general.updatetime", 600)) * 20;
        skillcap = config.getInt("general.skillcap", 0);
        skillsperpage = Math.max(1, config.getInt("general.skillsperpage", 5));
    }
    
    private void loadPlayer(UUID uuid)
    {
        File file = new File(getDataFolder(), "players" + File.separator + uuid.toString() + ".yml");
        FileConfiguration conf = YamlConfiguration.loadConfiguration(file);
        
        List<String> playerSkills = conf.getStringList("skills");
        int bonuscap = conf.getInt("bonuscap", 0);
        HashMap<String, Long> rent = new HashMap<String, Long>();
        
        if (rentedConfig.getConfigurationSection(uuid.toString()) != null)
            for (String key : rentedConfig.getConfigurationSection(uuid.toString()).getKeys(false))
                rent.put(key, rentedConfig.getLong(uuid.toString() + "." + key));
        
        playerMap.put(uuid, new SkillPlayer(this, uuid, playerSkills, rent, bonuscap));
    }
}
