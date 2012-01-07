package com.syd.expskills;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Methods;

public class CommandManager implements CommandExecutor
{
    ExpSkills plugin;

    public CommandManager(ExpSkills plugin)
    {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        // Console or Player check - loading player data
        Player p = null;
        if (sender instanceof Player)
        {
            p = (Player) sender;
        }

        if (p == null || PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.use") || PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
        {
            if (cmd.getName().equalsIgnoreCase("exp"))
            {
                if (args.length == 0)
                {
                    return false;
                }
                if (args[0].equalsIgnoreCase("help"))
                {
                    if (args.length == 1)
                    {
                        sender.sendMessage("/exp help [command] - get help for this plugin");
                        sender.sendMessage("/exp stats - show your stats");
                        sender.sendMessage("/exp list <page> <filter> - list avaible skills");
                        sender.sendMessage("/exp info <skill> - get information to a specific skill");
                        sender.sendMessage("/exp buy <skill> - buy a skill");
                        // sender.sendMessage("/exp rent <skill> - rent a skill");
                        // <-- not yet ;)
                        sender.sendMessage("/exp current - show your current skills");
                        if (PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                        {
                            sender.sendMessage("===== Admin Commands =====");
                            sender.sendMessage("/exp stats <player> - get other's stats");
                            sender.sendMessage("/exp <set/add> <player> <xp/level/skill> <amount> - modify player's stats");
                            sender.sendMessage("/exp <grant/revoke> <player> <skill> [charged/payout](true/false) - grant/revoke a skill");
                            sender.sendMessage("/exp current <player> - get current skills of a player");
                        }
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("stats"))
                    {
                        sender.sendMessage("Shows your current Level, XP and your Total XP");
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("list"))
                    {
                        List<String> cats = funcs.getCats();
                        sender.sendMessage("List all avaible skills");
                        sender.sendMessage("Possible filter:");
                        for (int i = 0; i < cats.size(); i++)
                        {
                            sender.sendMessage(cats.get(i));
                        }
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("info"))
                    {
                        sender.sendMessage("Shows you additional information about this skill");
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("buy"))
                    {
                        sender.sendMessage("Buy this skill");
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("rent"))
                    {
                        sender.sendMessage("Rent this skill");
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("current"))
                    {
                        sender.sendMessage("Shows you all bought and rented skills");
                        return true;
                    }
                    else if (args[1].equalsIgnoreCase("skilltree"))
                    {
                        sender.sendMessage("Shows you the skilltree");
                        return true;
                    }
                    if (p == null || PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                    {
                        if (args[1].equalsIgnoreCase("set") && PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                        {
                            sender.sendMessage("Set the Level/exp of a player");
                            return true;
                        }
                        else if (args[1].equalsIgnoreCase("add") && PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                        {
                            sender.sendMessage("Add or substract XP/Level/Skillpoints of a player");
                            sender.sendMessage("NOTE: substract means you add -<amount>");
                            sender.sendMessage("NOTE: avoid to use totalXP and Level and preferer XP");
                            sender.sendMessage("If you do so the Stats are maybe unsynced. (to much/less total XP for this Level)");
                            return true;
                        }
                    }

                    else
                    {
                        sender.sendMessage("/exp help [command] - get help for this plugin");
                        sender.sendMessage("/exp stats - show your stats");
                        sender.sendMessage("/exp list <page> <filter> - list avaible skills");
                        sender.sendMessage("/exp info <skill> - get information to a specific skill");
                        sender.sendMessage("/exp buy <skill> - buy a skill");
                        // sender.sendMessage("/exp rent <skill> - rent a skill");
                        // <-- not yet ;)
                        sender.sendMessage("/exp current - show your current skills");
                        if (PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                        {
                            sender.sendMessage("===== Admin Commands =====");
                            sender.sendMessage("/exp stats <player> - get other's stats");
                            sender.sendMessage("/exp <set/add> <player> <xp/totalxp/level/skill> <amount> - modify player's stats");
                            sender.sendMessage("/exp <grant/revoke> <player> <skill> [charged/payout](true/false) - grant/revoke a skill");
                            sender.sendMessage("/exp current <player> - get current skills of a player");
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("stats"))
                {
                    if (args.length == 2)
                    {
                        Player player = funcs.getPlayer(args[1]);
                        if (player == null)
                        {
                            sender.sendMessage(ChatColor.RED + "player not found! (not existing/offline)");
                            return true;
                        }
                        funcs.updatePlaytime(player);

                        sender.sendMessage(player.getName() + "'s Stats:");
                        sender.sendMessage("Experience: " + player.getTotalExperience() + " (" + funcs.getXpToUp(player) + " XP until LevelUp)");
                        sender.sendMessage("Level: " + funcs.getLevel(player));
                        sender.sendMessage("Skill Points: " + funcs.getSkillPoints(player));
                        sender.sendMessage("Playtime: " + funcs.getPlaytime(player));
                        return true;
                    }
                    else
                    {
                        if (p == null)
                        {
                            sender.sendMessage("Consoles dont have experience!");
                            return true;
                        }

                        funcs.updatePlaytime(p);
                        sender.sendMessage(sender.getName() + "'s Stats:");
                        sender.sendMessage("Experience: " + p.getTotalExperience() + " (" + funcs.getXpToUp(p) + " XP until LevelUp)");
                        sender.sendMessage("Level: " + funcs.getLevel(p));
                        sender.sendMessage("Skill Points: " + funcs.getSkillPoints(p));
                        sender.sendMessage("Playtime: " + funcs.getPlaytime(p));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("list"))
                {
                    int page;
                    if (p != null)
                    {
                        if (args.length == 3)
                        {

                            try
                            {
                                page = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException ex)
                            {
                                sender.sendMessage("No valid argument!");
                                return false;
                            }
                            String filter = args[2];

                            funcs.getList(page, filter, p);
                            return true;
                        }
                        else if (args.length == 2)
                        {
                            try
                            {
                                page = Integer.parseInt(args[1]);
                            }
                            catch (NumberFormatException ex)
                            {
                                funcs.getList(1, args[1], p);
                                return true;
                            }

                            funcs.getList(page, null, p);
                            return true;
                        }
                        else if (args.length == 1)
                        {
                            funcs.getList(1, null, p);
                            return true;
                        }
                        else
                        {
                            sender.sendMessage("Too much/less arguments");
                            return true;
                        }
                    }
                    sender.sendMessage("You are no player!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("info"))
                {
                    if (p == null)
                    {
                        sender.sendMessage("You are not a player!");
                    }
                    else if (args.length == 2)
                    {
                        funcs.getInfo(args[1], p);
                        return true;
                    }
                    else
                    {
                        sender.sendMessage("Skill not found!");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("buy"))
                {
                    if (p != null)
                    {
                        if (args.length == 2)
                        {
                            funcs.buySkill(args[1], p);
                            return true;
                        }
                        else
                        {
                            p.sendMessage("Too much/less arguments!");
                            return true;
                        }
                    }
                    else
                    {
                        sender.sendMessage("The Console cant buy skills!");
                        return true;
                    }
                }
                /*
                 * if (args[0].equalsIgnoreCase("rent")) { return true; }
                 */
                if (args[0].equalsIgnoreCase("current"))
                {
                    if (args.length == 1)
                    {

                        if (p != null)
                        {
                            funcs.getCurrent(p);
                        }
                        else
                        {
                            sender.sendMessage("Consoles dont have skills!");
                        }
                        return true;
                    }
                    else if (args.length == 2 && PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                    {
                        Player player = funcs.getPlayer(args[1]);
                        if (player == null)
                        {
                            sender.sendMessage("Player not found!");
                        }
                        else
                            funcs.getCurrent(player, sender);
                    }

                }
                if (p == null || PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                {
                    if (args[0].equalsIgnoreCase("add") && PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                    {
                        if (args.length == 4)
                        {
                            int amount;
                            Player player = funcs.getPlayer(args[1]);
                            if (player == null)
                            {
                                sender.sendMessage(ChatColor.RED + "player not found!");
                                return true;
                            }
                            try
                            {
                                amount = Integer.parseInt(args[3]);
                            }
                            catch (NumberFormatException ex)
                            {
                                sender.sendMessage("No valid argument!");
                                return false;
                            }
                            if (args[2].equalsIgnoreCase("xp"))
                            {
                                funcs.addXP(player, amount);
                                sender.sendMessage("Done");
                            }
                            else if (args[2].equalsIgnoreCase("level"))
                            {
                                funcs.addLevel(player, amount);
                                sender.sendMessage("Done");
                            }
                            else if (args[2].equalsIgnoreCase("skill"))
                            {
                                funcs.addSkillPoints(player, amount);
                                sender.sendMessage("Done");
                            }
                        }
                        else
                        {
                            sender.sendMessage("Too much/less arguments");
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("set") && PermissionsSystem.hasPermission(p.getWorld().getName(), p.getName(), "expskills.admin"))
                    {
                        if (args.length == 4)
                        {
                            int amount;
                            Player player = funcs.getPlayer(args[1]);
                            try
                            {
                                amount = Integer.parseInt(args[3]);
                            }
                            catch (NumberFormatException ex)
                            {
                                sender.sendMessage("No valid argument!");
                                return false;
                            }
                            if (args[2].equalsIgnoreCase("xp"))
                            {
                                funcs.setXP(player, amount);
                                sender.sendMessage("Done");
                            }
                            else if (args[2].equalsIgnoreCase("level"))
                            {
                                funcs.setLevel(player, amount);
                                sender.sendMessage("Done");
                            }
                            else if (args[2].equalsIgnoreCase("skill"))
                            {
                                funcs.setSkillPoints(player, amount);
                                sender.sendMessage("Done");
                            }
                        }
                        else
                        {
                            sender.sendMessage("Too much/less arguments!");
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("reload"))
                    {
                        Plugin plugin = ExpSkills.server.getPluginManager().getPlugin("ExpSkills");
                        if (ExpSkills.config.getBoolean("general.use_economy", false))
                        {
                            Methods.setMethod(null);
                        }
                        plugin.onEnable();
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("grant"))
                    {
                        boolean charge = false;
                        if (args.length > 2 && args.length < 5)
                        {
                            Player player = funcs.getPlayer(args[1]);
                            if (args.length == 4)
                            {
                                if (args[3].equalsIgnoreCase("true"))
                                {
                                    charge = true;
                                }
                            }
                            if (player != null)
                            {
                                if (funcs.grantSkill(player, charge, args[2]))
                                    sender.sendMessage("Skill granted!");
                                else
                                    sender.sendMessage("Skill is not existing!");
                            }
                            else
                            {
                                ExpSkills.log.info("Player not online!");
                            }
                        }
                        else
                        {
                            sender.sendMessage("Too much/less arguments!");
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("revoke"))
                    {
                        boolean payout = false;
                        if (args.length > 2 && args.length < 5)
                        {
                            Player player = funcs.getPlayer(args[1]);

                            if (args.length == 4)
                            {
                                if (args[3].equalsIgnoreCase("true"))
                                {
                                    payout = true;
                                }
                            }
                            if (player != null)
                            {
                                if (funcs.revokeSkill(player, payout, args[2]))
                                    sender.sendMessage("Skill revoked!");
                                else
                                    sender.sendMessage("Skill is not existing!");
                            }
                            else
                            {
                                ExpSkills.log.info("Player not online!");
                            }
                        }
                        else
                        {
                            sender.sendMessage("Too much/less arguments!");
                        }
                        return true;
                    }
                    if (args[0].equalsIgnoreCase("reset"))
                    {

                        Player player = ExpSkills.server.getPlayerExact(args[1]);
                        if (player != null)
                        {
                            if (args.length == 2)
                            {
                                funcs.reset(player);
                                sender.sendMessage(player.getName() + "'s skills were reset!");
                            }
                            else
                            {
                                sender.sendMessage("Too much/less arguments!");
                            }
                        }
                        else
                            sender.sendMessage("Player not found! You need to type in the EXACT name.");
                    }
                }
                else
                {
                    return false;
                }
            }
            return true;

        }
        sender.sendMessage("You don't have the needed permissions!");
        return true;
    }
}
