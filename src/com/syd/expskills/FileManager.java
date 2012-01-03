package com.syd.expskills;

import java.io.File;
import java.io.IOException;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager
{
    Server server;
    static ExpSkills plugin;

    public FileManager(ExpSkills plugin)
    {
    }

/*
 *     public static FileConfiguration loadConfig()
    {
        final String dir = "plugins" + File.separator + "ExpSkills" + File.separator;
        final File configFile = new File(dir + "config.yml");

        FileConfiguration config = plugin.getConfig();
        
        new File(dir).mkdir();

        if (!configFile.exists())
        {
            try
            {
                configFile.createNewFile();
                config.addDefault("general.skillpoint_modifier", 2.0);
                config.addDefault("general.currency", "Dollar");
                config.addDefault("general.use_skilltree", false);
                config.addDefault("general.preferred_economy", null);
                config.addDefault("skills.skill0.name", "testskill");
                config.addDefault("skills.skill0.description", "Just a example");
                config.addDefault("skills.skill0.info", "This Skill was created to show Admins how to use this configgile!");
                config.addDefault("skills.skill0.cost_type", "both");
                config.addDefault("skills.skill0.skillpoints", 2);
                List<String> need = null;
                need.add("foo.bar");
                config.addDefault("skills.skill0.permissions_need", need);
                List<String> earn = null;
                earn.add("bar.foo");
                earn.add("bar.bar");
                config.addDefault("skills.skill0.permissions_earn", earn);
                List<String> cat = null;
                cat.add("exaple");
                config.addDefault("skills.skill0.categories", cat);
                config.addDefault("version", 0.5);
                config.options().copyDefaults(true);       
                plugin.saveConfig();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        
        return config;
    }*/

    public static void createPlayerFile(Player player)
    {
        final String dir = "plugins" + File.separator + "ExpSkills" + File.separator + "player" + File.separator;
        final File PlayerFile = new File(dir + player.getName() + ".yml");

        new File(dir).mkdir();

        if (!PlayerFile.exists())
        {
            try
            {
                PlayerFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static YamlConfiguration loadPF(Player player)
    {
        final String dir = "plugins" + File.separator + "ExpSkills" + File.separator + "player" + File.separator;
        final File pfile = new File(dir + player.getName() + ".yml");
        YamlConfiguration prconfig = YamlConfiguration.loadConfiguration(pfile);

        return prconfig;
    }

    public static YamlConfiguration loadSkilltree()
    {
        final String dir = "plugins" + File.separator + "ExpSkills" + File.separator;
        final File treefile = new File(dir + "skilltree.yml");

        new File(dir).mkdir();

        if (!treefile.exists())
        {
            try
            {
                treefile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        YamlConfiguration skilltree = YamlConfiguration.loadConfiguration(treefile);

        return skilltree;
    }
}
