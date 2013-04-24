package com.syd.expskills;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager
{
    public static void createPlayerFile(Player player)
    {
        final File file = new File(ExpSkills.plugin.getDataFolder(), "player" + File.separator + player.getName() + ".yml");
        
        if (!file.exists())
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }
    
    public static YamlConfiguration loadPF(Player player)
    {
        final File pfile = new File(ExpSkills.plugin.getDataFolder(), "player" + File.separator + player.getName() + ".yml");
        YamlConfiguration prconfig = YamlConfiguration.loadConfiguration(pfile);
        
        return prconfig;
    }
    
    public static void savePF(Player player, YamlConfiguration pconfig)
    {
        try
        {
            pconfig.save(new File(ExpSkills.plugin.getDataFolder(), "player" + File.separator + player.getName() + ".yml"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static YamlConfiguration loadSkilltree()
    {
        final File treefile = new File(ExpSkills.plugin.getDataFolder(), "skilltree.yml");
        
        if (!treefile.exists())
            try
            {
                treefile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        YamlConfiguration skilltree = YamlConfiguration.loadConfiguration(treefile);
        
        return skilltree;
    }
    
    public static YamlConfiguration loadRented()
    {
        final File rentfile = new File(ExpSkills.plugin.getDataFolder(), "rented.yml");
        
        if (!rentfile.exists())
            try
            {
                rentfile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        
        YamlConfiguration rented = YamlConfiguration.loadConfiguration(rentfile);
        
        return rented;
    }
}
