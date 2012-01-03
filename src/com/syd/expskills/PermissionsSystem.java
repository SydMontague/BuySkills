package com.syd.expskills;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import de.bananaco.permissions.worlds.HasPermission;
import de.bananaco.permissions.worlds.WorldPermissionsManager;

public class PermissionsSystem
{

    Server server;
    ExpSkills plugin;
    public static PermissionHandler perm = null;
    public static PermissionManager permEX = null;
    public static WorldPermissionsManager bPerm = null;
    public static boolean permBukkit = false;
    static CommandSender sender;

    public PermissionsSystem(ExpSkills plugin)
    {
    }

    public void start()
    {
        // check for PEX since it's not emulated and will work fine
        if (ExpSkills.server.getPluginManager().getPlugin("PermissionsEx") != null)
        {
            permEX = PermissionsEx.getPermissionManager();
            ExpSkills.log.info("[ExpSkills] " + "PermissionsEX detected");
        }
        // check for PermissionBukkit since it's not emulated
        else if (ExpSkills.server.getPluginManager().getPlugin("PermissionsBukkit") != null)
        {
            ExpSkills.log.warning("[ExpSkills] PermissionsBukkit is NOT supported (yet)");
            ExpSkills.log.warning("[ExpSkills] Trying to use Bukkit built-in Permissions (not supported yet)");
            permBukkit = true;
            sender = ExpSkills.server.getConsoleSender();
        }
        // if there is no PEX/Permissions bukkit check for Permissions
        else if (ExpSkills.server.getPluginManager().getPlugin("Permissions") != null)
        {
            ExpSkills.log.info("[ExpSkills] " + "Permissions detected");
            
            // Check if SuperpermBridge is used (should never be called)
            if (ExpSkills.server.getPluginManager().getPlugin("PermissionsBukkit") != null)
            {
                ExpSkills.log.warning("[ExpSkills] " + "PermissionsBukkit is NOT supported!");
                ExpSkills.log.warning("[ExpSkills] " + "You can't add/remove Skills");
            }
            Permissions permissions = (Permissions) ExpSkills.server.getPluginManager().getPlugin("Permissions");
            perm = permissions.getHandler();
        }
        else if (ExpSkills.server.getPluginManager().getPlugin("bPermissions") != null)
        {
            ExpSkills.log.info("[ExpSkills] bPermissions detected");
            bPerm = de.bananaco.permissions.Permissions.getWorldPermissionsManager();
        }
        // Use built-in Permissions if no other supported Plugin is avaible
        else
        {
            ExpSkills.log.warning("[ExpSkills] " + "No permissions plugin detectet!");
            ExpSkills.log.warning("[ExpSkills] " + "Trying to use Bukkit build in Permissions");
            permBukkit = true;
        }
    }

    //update Permission Methods for more function
    
    public static void addPermission(String world, String player, String node)
    {
        if (permEX != null)
        {
            permEX.getUser(player).addPermission(node);
        }
        else if (perm != null)
        {
            perm.addUserPermission(world, player, node);
        }
        else if (bPerm != null)
        {
            bPerm.getPermissionSet(world).addPlayerNode(node, player);
        }
        else if (permBukkit == true)
        {            
            ExpSkills.server.dispatchCommand(sender, "permissions setperm " + node);
        }
        else
        {
            Player p = ExpSkills.server.getPlayerExact(player);
            PermissionAttachment attachment = p.addAttachment(ExpSkills.server.getPluginManager().getPlugin("ExpSkills"));
            attachment.setPermission(node, true);
        }
    }

    public static void removePermission(String world, String player, String node)
    {
        if (permEX != null)
        {
            permEX.getUser(player).removePermission(node);
        }
        else if (perm != null)
        {
            perm.removeUserPermission(world, player, node);
        }
        else if (bPerm != null)
        {
            bPerm.getPermissionSet(world).removePlayerNode(node, player);
        }
        else if (permBukkit == true)
        {
            ExpSkills.server.dispatchCommand(sender, "permissions unsetperm " + node);
        }
        else
        {
            Player p = ExpSkills.server.getPlayerExact(player);
            PermissionAttachment attachment = p.addAttachment(ExpSkills.plugin);
            attachment.unsetPermission(node);
        }
    }

    public static boolean hasPermission(String world, String player, String node)
    {
        if (permEX != null)
        {
            return permEX.getUser(player).has(node);
        }
        else if (perm != null)
        {
            return perm.has(world, player, node);
        }
        else if (bPerm != null)
        {
            return HasPermission.has(player, world, node);
        }
        else if (permBukkit == true)
        {
            return ExpSkills.server.getPlayerExact(player).hasPermission(node);
        }
        else
            return ExpSkills.server.getPlayerExact(player).hasPermission(node);
    }

    public static void addGroup(String world, String player, String group)
    {
        if (permEX != null)
        {
            permEX.getUser(player).addGroup(group);
        }
        else if (perm != null)
        {
            ExpSkills.log.info("Can't use groups of Permissions");
        }
        else if (bPerm != null)
        {
            bPerm.getPermissionSet(world).addGroup(player, group);
        }
        else if (permBukkit == true)
        {
            ExpSkills.server.dispatchCommand(sender, "permissions addgroup " + group);
        }
    }

    public static void removeGroup(String world, String player, String group)
    {
        if (permEX != null)
        {
            permEX.getUser(player).removeGroup(group);
        }
        else if (perm != null)
        {
            ExpSkills.log.info("Can't use groups of Permissions");
        }
        else if (bPerm != null)
        {
            bPerm.getPermissionSet(world).removeGroup(player, group);
        }
        else if (permBukkit == true)
        {

            ExpSkills.server.dispatchCommand(sender, "permissions removegroup " + group);
        }
    }

    public static boolean hasGroup(String player, String group, String world)
    {
        if (permEX != null)
        {
            return permEX.getUser(player).inGroup(group);
        }
        else if (perm != null)
        {
            return perm.inGroup(player, group);
        }
        else if (bPerm != null)
        {
            return bPerm.getPermissionSet(world).hasGroup(player, group);
        }
        else if (permBukkit == true)
        {
            ExpSkills.log.info("Can't use groups of PermissionsBukkit");
            return false;
        }
        else
            return false;
    }

}
