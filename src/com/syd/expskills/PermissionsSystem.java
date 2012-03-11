package com.syd.expskills;

import java.util.List;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.RegisteredServiceProvider;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import de.bananaco.permissions.worlds.WorldPermissionsManager;

//REPLACE/EXTEND WITH VAULT

public class PermissionsSystem
{
    protected static PermissionHandler perm = null;
    protected static PermissionManager permEX = null;
    protected static WorldPermissionsManager bPerm = null;
    protected static boolean permBukkit = false;
    protected static boolean GM = false;
    static CommandSender sender;
    public static Permission permission = null;

    public void start()
    {

        if (ExpSkills.server.getPluginManager().getPlugin("Vault") != null)
        {
            RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
            if (permissionProvider != null)
            {
                permission = permissionProvider.getProvider();
            }

            ExpSkills.log.info("[ExpSkills] " + permission.getName() + " hooked");
        }

        // add GroupManager
        // check for PEX since it's not emulated and will work fine
        else if (ExpSkills.server.getPluginManager().getPlugin("PermissionsEx") != null)
        {
            permEX = PermissionsEx.getPermissionManager();
            ExpSkills.log.info("[ExpSkills] " + "PermissionsEX detected");
        }
        // check for PermissionBukkit
        else if (ExpSkills.server.getPluginManager().getPlugin("PermissionsBukkit") != null)
        {
            ExpSkills.log.warning("[ExpSkills] PermissionsBukkit detected");
            ExpSkills.log.warning("[ExpSkills] usage of groups_need node is not possible!");
            permBukkit = true;
            sender = ExpSkills.server.getConsoleSender();
        }
        // if there is no PEX/PermissionsBukkit check for Permissions
        else if (ExpSkills.server.getPluginManager().getPlugin("Permissions") != null)
        {
            ExpSkills.log.info("[ExpSkills] " + "Permissions detected");

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

    public static void addPermission(String world, String player, String node)
    {
        Player p = ExpSkills.server.getPlayerExact(player);

        if (permission != null)
            permission.playerAdd(p, node);
        else if (permEX != null)
            permEX.getUser(player).addPermission(node);
        else if (perm != null)
            perm.addUserPermission(world, player, node);
        else if (bPerm != null)
            bPerm.getPermissionSet(world).addPlayerNode(node, player);
        else if (permBukkit == true)
            ExpSkills.server.dispatchCommand(sender, "permissions player setperm " + player + " " + node);
        else
        {
            PermissionAttachment attachment = p.addAttachment(ExpSkills.server.getPluginManager().getPlugin("ExpSkills"));
            attachment.setPermission(node, true);
        }
    }

    public static void addPermission(List<String> worlds, String player, String node)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerAdd(ExpSkills.server.getWorld(world), player, node);
        else
            ExpSkills.log.info("This operation is only supported with Vault");
    }
    
    public static void removePermission(String world, String player, String node)
    {
        Player p = ExpSkills.server.getPlayerExact(player);
        if (permission != null)
            permission.playerRemove(p, node);
        else if (permEX != null)
            permEX.getUser(player).removePermission(node);
        else if (perm != null)
            perm.removeUserPermission(world, player, node);
        else if (bPerm != null)
            bPerm.getPermissionSet(world).removePlayerNode(node, player);
        else if (permBukkit == true)
            ExpSkills.server.dispatchCommand(sender, "permissions player unsetperm " + player + " " + node);
        else
        {
            PermissionAttachment attachment = p.addAttachment(ExpSkills.plugin);
            attachment.unsetPermission(node);
        }
    }

    public static void addGroup(String world, String player, String group)
    {
        Player p = ExpSkills.server.getPlayerExact(player);
        
        if (permission != null)
            permission.playerAddGroup(p, group);
        else if (permEX != null)
            permEX.getUser(player).addGroup(group);
        else if (perm != null)
            ExpSkills.log.info("Can't use groups of Permissions");
        else if (bPerm != null)
            bPerm.getPermissionSet(world).addGroup(player, group);
        else if (permBukkit == true)
            ExpSkills.server.dispatchCommand(sender, "permissions player addgroup " + player + " " + group);
        else
            ExpSkills.log.info("You don't use a group supported permissions plugin!");
    }

    public static void addGroup(List<String> worlds, String player, String group)
    {
        if (permission != null)
            for (String world : worlds)
                permission.playerAddGroup(ExpSkills.server.getWorld(world), player, group);
        else
            ExpSkills.log.info("This operation is only supported with Vault");
    }

    public static void removeGroup(String world, String player, String group)
    {
        Player p = ExpSkills.server.getPlayerExact(player);

        if (permission != null)
            permission.playerRemoveGroup(p, group);
        else if (permEX != null)
            permEX.getUser(player).removeGroup(group);
        else if (perm != null)
            ExpSkills.log.info("Can't use groups of Permissions");
        else if (bPerm != null)
            bPerm.getPermissionSet(world).removeGroup(player, group);
        else if (permBukkit == true)
            ExpSkills.server.dispatchCommand(sender, "permissions player removegroup " + player + " " + group);
        else
            ExpSkills.log.info("You don't use a group supported permissions plugin!");
    }

    public static boolean hasGroup(String player, String group, String world)
    {
        Player p = ExpSkills.server.getPlayerExact(player);

        if (permission != null)
            return permission.playerInGroup(p, group);
        else if (permEX != null)
            return permEX.getUser(player).inGroup(group);
        else if (perm != null)
            return perm.inGroup(player, group);
        else if (bPerm != null)
            return bPerm.getPermissionSet(world).hasGroup(player, group);
        else if (permBukkit == true)
        {
            ExpSkills.log.info("Can't use groups of PermissionsBukkit");
            return false;
        }
        else
            return false;
    }

}
