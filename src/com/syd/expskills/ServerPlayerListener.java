package com.syd.expskills;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.permissions.PermissionAttachment;

public class ServerPlayerListener extends PlayerListener
{
    public static ExpSkills plugin;

    public ServerPlayerListener(ExpSkills instance)
    {
        plugin = instance;
    }

    // remodify ExpReset
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        FileManager.createPlayerFile(player);

        YamlConfiguration pfile = FileManager.loadPF(player);

        // Workaround for Bukkit Bug #1595
        if (player.getTotalExperience() < pfile.getInt("experience", 0))
        {
            player.setTotalExperience(pfile.getInt("experience", 0));
        }

        if (PermissionsSystem.permBukkit == true)
        {
            @SuppressWarnings("unchecked")
            List<String> skills = pfile.getList("skills", null);
            List<String> perms = new ArrayList<String>();

            if (skills != null)
            {
                for (int i = 0; i < skills.size(); i++)
                {
                    @SuppressWarnings("unchecked")
                    List<String> perm = ExpSkills.config.getList("skills." + skills.get(i) + ".permissions_earn", null);
                    for (int a = 0; a < perm.size(); a++)
                    {
                        perms.add(perm.get(a));
                    }
                }
                PermissionAttachment attachment = player.addAttachment(plugin);
                for (int s = 0; s < perms.size(); s++)
                {
                    attachment.setPermission(perms.get(s), true);
                }
            }
        }

        pfile.set("donotchange", System.currentTimeMillis());
        try
        {
            pfile.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // Workaround Bukkit-155
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        final Player p = event.getPlayer();
        final YamlConfiguration pfile = FileManager.loadPF(p);
        final int xp = pfile.getInt("experience");

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
        {
            public void run()
            {
                p.setTotalExperience(xp);
                p.setLevel(0);
            }
        }, 1L);
    }

    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        funcs.updatePlaytime(player);

        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}
