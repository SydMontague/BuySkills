package com.syd.expskills;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijikokun.register.payment.Method;
import com.nijikokun.register.payment.Methods;

public class ExpSkills extends JavaPlugin
{
    protected static ExpSkills plugin;
    protected static Server server;
    protected static FileConfiguration config;
    protected static YamlConfiguration skilltree;
    protected static YamlConfiguration lang;
    protected static Logger log = Logger.getLogger("Minecraft");
    private final ServerEntityListener entityListener = new ServerEntityListener(this);
    private final ServerPlayerListener playerListener = new ServerPlayerListener(this);
    private CommandManager command = new CommandManager(this);
    protected static Method method;
    protected static Economy economy = null;
    
    @Override
    public void onEnable()
    {
        log = getLogger();
        server = getServer();
        plugin = this;
        
        // start of config.yml
        
        if (!new File(getDataFolder().getPath() + File.separatorChar + "config.yml").exists())
            saveDefaultConfig();
        
        config = getConfig();
        
        String configversion = config.getString("version");
        String version = getDescription().getVersion();
        
        if (configversion.equalsIgnoreCase(version))
            if (configversion.equals("0.7.0_RC2"))
                config.set("general.updatetime", 6000);
            else if (configversion.equals("0.6.4"))
            {
                config.set("general.formula", 0);
                config.set("general.formula_a", 0);
                config.set("general.formula_b", 0);
                config.set("general.formula_c", 0);
                config.set("general.formula_d", 0);
                config.set("general.formula_e", 0);
                config.set("general.skill_cap", 0);
                config.set("general.updatetime", 6000);
            }
        config.set("version", getDescription().getVersion());
        saveConfig();
        // end of config.yml
        
        // start of lang.yml
        File langfile = new File(getDataFolder() + File.separator + "lang.yml");
        
        if (!langfile.exists())
        {
            lang = YamlConfiguration.loadConfiguration(getResource("lang.yml"));
            try
            {
                lang.save(langfile);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
            lang = YamlConfiguration.loadConfiguration(langfile);
        // end of lang.yml
        
        // start skilltree
        if (config.getBoolean("general.use_skilltree", false))
            skilltree = FileManager.loadSkilltree();
        
        // start rented timer
        long delay = config.getLong("general.updatetime", 6000);
        
        server.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                RentingManager.update();
            }
        }, 0, delay);
        
        // initialize events and commands
        getCommand("exp").setExecutor(command);
        final PluginManager pm = server.getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        
        // start permissions section
        PermissionsSystem.start();
        
        // start economy section
        if ((config.getBoolean("general.use_economy", false)))
            if (pm.getPlugin("Vault") != null)
            {
                RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
                if (economyProvider != null)
                {
                    economy = economyProvider.getProvider();
                    log.info("[ExpSkills] " + economy.getName() + " hooked");
                }
            }
            else if (pm.getPlugin("Register") != null)
                if (Methods.setMethod(pm))
                {
                    method = Methods.getMethod();
                    log.info("[ExpSkills] " + method.getName() + " hooked");
                }
                else
                    log.severe("[ExpSkills] Hooking Economy Failed");
    }
    
    @Override
    public void onDisable()
    {
        server.getScheduler().cancelTasks(this);
    }
    
    public static int getExp(double level)
    {
        int exp = 0;
        
        double nlevel = Math.ceil(level);
        
        if (nlevel - 32 > 0)
            exp += (1 - (nlevel - level)) * (65 + (nlevel - 32) * 7);
        else if (nlevel - 16 > 0)
            exp += (1 - (nlevel - level)) * (17 + (level - 16) * 3);
        else
            exp += (1 - (nlevel - level)) * 17;
        
        level = Math.ceil(level - 1);
        
        while (level > 0)
        {
            if (level - 32 > 0)
                exp += 65 + (level - 32) * 7;
            else if (level - 16 > 0)
                exp += 17 + (level - 16) * 3;
            else
                exp += 17;
            
            level--;
        }
        
        return exp;
    }
    
    public static double getLevel(int exp)
    {
        double i = 0;
        boolean stop = true;
        
        while (stop)
        {
            i++;
            
            if (i - 32 > 0)
            {
                if (exp - (65 + (i - 32) * 7) > 0)
                    exp = (int) (exp - (65 + (i - 32) * 7));
                else
                    stop = false;
            }
            else if (i - 16 > 0)
            {
                if (exp - (17 + (i - 16) * 3) > 0)
                    exp = (int) (exp - (17 + (i - 16) * 3));
                else
                    stop = false;
            }
            else if (exp > 17)
                exp -= 17;
            else
                stop = false;
        }
        
        if (exp != 0)
            if (i - 32 > 0)
                i += exp / (65 + (i - 32) * 7);
            else if (i - 16 > 0)
                i += exp / (17 + (i - 16) * 3);
            else
                i += exp / 17D;
        
        if (exp == 0)
            i--;
        
        i--;
        return i;
    }
}
