package com.syd.expskills;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class FileManager
{
    public FileManager(ExpSkills plugin)
    {
    }

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

    public static void savePF(Player player, YamlConfiguration pconfig)
    {
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

    public static YamlConfiguration loadRented()
    {
        final String dir = "plugins" + File.separator + "ExpSkills" + File.separator;
        final File rentfile = new File(dir + "rented.yml");

        if (!rentfile.exists())
        {
            try
            {
                rentfile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        YamlConfiguration rented = YamlConfiguration.loadConfiguration(rentfile);

        return rented;
    }
}
