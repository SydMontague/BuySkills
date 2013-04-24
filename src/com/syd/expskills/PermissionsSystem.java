package com.syd.expskills;

import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class PermissionsSystem
{
    public static Permission permission = null;
    
    public static void start()
    {
        if (ExpSkills.server.getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
                permission = permissionProvider.getProvider();
            
            ExpSkills.log.info("[ExpSkills] " + permission.getName() + " hooked");
        }
    }
    
    public static void addPermission(String world, String player, String node)
    {
        if (permission != null)
            permission.playerAdd(world, player, node);
    }
    
    public static void addPermission(List<String> worlds, String player, String node)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerAdd(ExpSkills.server.getWorld(world), player, node);
    }
    
    public static void removePermission(String world, String player, String node)
    {
        if (permission != null)
            permission.playerRemove(world, player, node);
    }
    
    public static void removePermission(List<String> worlds, String player, String node)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerRemove(ExpSkills.server.getWorld(world), player, node);
    }
    
    public static void addGroup(String player, String group)
    {
        Player p = ExpSkills.server.getPlayerExact(player);
        
        if (permission != null)
            permission.playerAddGroup(p, group);
    }
    
    public static void addGroup(List<String> worlds, String player, String group)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerAddGroup(ExpSkills.server.getWorld(world), player, group);
    }
    
    public static void removeGroup(String player, String group)
    {
        Player p = ExpSkills.server.getPlayerExact(player);
        
        if (permission != null)
            permission.playerRemoveGroup(p, group);
    }
    
    public static void removeGroup(List<String> worlds, String player, String group)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerRemoveGroup(ExpSkills.server.getWorld(world), player, group);
    }
    
    public static boolean hasGroup(String player, String group)
    {
        Player p = ExpSkills.server.getPlayerExact(player);
        
        if (permission != null)
            return permission.playerInGroup(p, group);
        else
            return false;
    }
    
}
