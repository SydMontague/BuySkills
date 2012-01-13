package com.syd.expskills;

import java.io.IOException;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class RentingManager
{

    public static ExpSkills plugin;

    public static void update()
    {
        YamlConfiguration rented = FileManager.loadRented();
        Set<String> players = rented.getKeys(false);

        for (String player : players)
        {
            Player p = ExpSkills.server.getOfflinePlayer(player).getPlayer();
            Set<String> skills = rented.getConfigurationSection(player).getKeys(false);

            for (String skill : skills)
            {
                if (rented.getLong(player + "." + skill + ".time") <= System.currentTimeMillis())
                {
                    funcs.revokeSkill(p, funcs.getSkillName(skill));
                    
                    rented.set(player + "." + skill, null);
                    
                    p.sendMessage("You rental of " + funcs.getSkillName(skill) + " has expired!");
                    try
                    {
                        rented.save("plugins/ExpSkills/rented.yml");
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //update buySkill to not use addSkill();
    public static boolean rentSkill(String skill, Player player)
    {
        YamlConfiguration rented = FileManager.loadRented();
        String key = funcs.getSkillKey(skill);
        if (funcs.buySkill(skill, player))
        {
            rented.set(player.getName() + "." + key + ".time", System.currentTimeMillis() + ExpSkills.config.getLong("skills." + key + ".renttime", 0)*1000);
            
            try
            {
                rented.save("plugins/ExpSkills/rented.yml");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
                                    
            return true;
        }
        return false;
    }

}
