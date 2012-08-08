package com.syd.expskills;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.permissions.PermissionAttachment;

public class ServerPlayerListener implements Listener
{
    protected static ExpSkills plugin;
    
    public ServerPlayerListener(ExpSkills instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void onPlayerEXPChance(PlayerExpChangeEvent event)
    {
        int exp = event.getPlayer().getTotalExperience() + event.getAmount();
        Player player = event.getPlayer();
        
        int levelold = funcs.getLevel(player);
        int levelnew = funcs.getLevel(exp);
        String msg = ExpSkills.lang.getString("general.levelup", "You are now level %level!");
        msg = msg.replace("%level", String.valueOf(levelnew));
        
        if (levelnew != levelold)
            player.sendMessage(msg);
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        FileManager.createPlayerFile(player);
        
        YamlConfiguration pfile = FileManager.loadPF(player);
        
        if (player.getTotalExperience() < pfile.getInt("experience", 0) && ExpSkills.config.getBoolean("general.change_expdrop", false))
            player.setTotalExperience(pfile.getInt("experience", 0));
        
        if (PermissionsSystem.GM == false && PermissionsSystem.permBukkit == false && PermissionsSystem.bPerm == null && PermissionsSystem.perm == null && PermissionsSystem.permEX == null)
        {
            List<String> skills = pfile.getStringList("skills");
            List<String> perms = new ArrayList<String>();
            
            if (skills != null)
                for (String skill : skills)
                {
                    List<String> perm = ExpSkills.config.getStringList("skills." + skill + ".permissions_earn");
                    for (String node : perm)
                        perms.add(node);
                }
            
            PermissionAttachment attachment = player.addAttachment(plugin);
            
            for (int s = 0; s < perms.size(); s++)
                attachment.setPermission(perms.get(s), true);
        }
        
        pfile.set("donotchange", System.currentTimeMillis());
        
        FileManager.savePF(player, pfile);
    }
    
    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        if (ExpSkills.config.getBoolean("general.change_expdrop", false))
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
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        funcs.updatePlaytime(player);
        
        FileManager.savePF(player, pconfig);
    }
}
