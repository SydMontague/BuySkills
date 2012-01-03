package com.syd.expskills;

import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class ServerEntityListener extends EntityListener
{
    public static ExpSkills plugin;

    public ServerEntityListener(ExpSkills instance)
    {
        plugin = instance;
    }

    //remodify ExpReset
    public void onEntityDeath(EntityDeathEvent event)
    {
        // modify ORB drop on player death
        if (event.getEntity() instanceof Player)
        {
            PlayerDeathEvent death = (PlayerDeathEvent) event;
            Player p = (Player) event.getEntity();
                     
            //int xp = p.getExp();
            int totalxp = p.getTotalExperience();
            int newxp = funcs.getXpatLevel(funcs.getLevel(p));
            int xp = totalxp - newxp;

            YamlConfiguration pconfig = FileManager.loadPF(p);

            //Workaround for Bukkit Bug #1595
            if (newxp < pconfig.getInt("experience", 0))
            {
                newxp = pconfig.getInt("experience", 0);
            }

            pconfig.set("experience", newxp);
            try
            {
                pconfig.save("plugins/ExpSkills/player/"+p.getName()+".yml");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            //default usage of EXP Drop - add dynamic method with config usage
            event.setDroppedExp(xp);
            death.setNewExp(newxp);
            
        }
        else
            return;
    }

}