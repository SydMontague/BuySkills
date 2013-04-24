package com.syd.expskills;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.nijikokun.register.payment.Method.MethodAccount;

public class funcs
{
    static Economy vault = ExpSkills.economy;
    static boolean savexp = ExpSkills.config.getBoolean("general.change_expdrop");
    
    public static Player getPlayer(String string)
    {
        return ExpSkills.server.getPlayer(string);
    }
    
    public static OfflinePlayer getOfflinePlayer(String string)
    {
        return ExpSkills.server.getOfflinePlayer(string);
    }
    
    public static void setXP(Player player, int exp)
    {
        player.setTotalExperience(exp);
        
        if (savexp)
        {
            YamlConfiguration pconfig = FileManager.loadPF(player);
            int newxp = player.getTotalExperience() - getXpatLevel(funcs.getLevel(player) - 1);
            
            pconfig.set("experience", newxp);
            
            FileManager.savePF(player, pconfig);
        }
    }
    
    public static void setLevel(Player player, int level)
    {
        player.setTotalExperience(funcs.getXpatLevel(level));
        
        if (savexp)
        {
            YamlConfiguration pconfig = FileManager.loadPF(player);
            
            int newxp = player.getTotalExperience();
            
            pconfig.set("experience", newxp);
            FileManager.savePF(player, pconfig);
        }
    }
    
    public static int getLevel(Player player)
    {
        int exp = player.getTotalExperience() + 1;
        int formula = ExpSkills.config.getInt("general.formula", 0);
        
        int level = 0;
        
        // approx default Formula 1.0.0
        if (formula == 0)
        {
            int i = 0;
            double value;
            do
            {
                i++;
                value = 1.75 * (i * i) + 4.5 * i;
            }
            while (value < exp);
            
            level = i - 1;
        }
        // default Formula 1.8.1
        if (formula == 1)
        {
            int i = 0;
            double value = 0;
            
            do
            {
                i++;
                value = value + i * 10;
            }
            while (value < exp);
            
            level = i - 1;
        }
        // custum formula
        if (formula == 2)
        {
            double a = ExpSkills.config.getDouble("general.formula_a", 0);
            double b = ExpSkills.config.getDouble("general.formula_b", 0);
            double c = ExpSkills.config.getDouble("general.formula_c", 0);
            double d = ExpSkills.config.getDouble("general.formula_d", 0);
            double e = ExpSkills.config.getDouble("general.formula_e", 0);
            
            int i = 0;
            double value;
            do
            {
                i++;
                value = a * (i * i * i * i) + b * (i * i * i) + c * (i * i) + d * i + e;
            }
            while (value < exp);
            
            level = i - 1;
        }
        
        return level;
    }
    
    public static int getLevel(int exp)
    {
        exp = exp + 1;
        int formula = ExpSkills.config.getInt("general.formula", 0);
        
        int level = 0;
        
        // approx default Formula 1.0.0
        if (formula == 0)
        {
            int i = 0;
            double value;
            do
            {
                i++;
                value = 1.75 * (i * i) + 4.5 * i;
            }
            while (value < exp);
            
            level = i - 1;
        }
        // default Formula 1.8.1
        if (formula == 1)
        {
            int i = 0;
            double value = 0;
            
            do
            {
                i++;
                value = value + i * 10;
            }
            while (value < exp);
            
            level = i - 1;
        }
        // custum formula
        if (formula == 2)
        {
            double a = ExpSkills.config.getDouble("general.formula_a", 0);
            double b = ExpSkills.config.getDouble("general.formula_b", 0);
            double c = ExpSkills.config.getDouble("general.formula_c", 0);
            double d = ExpSkills.config.getDouble("general.formula_d", 0);
            double e = ExpSkills.config.getDouble("general.formula_e", 0);
            
            int i = 0;
            double value;
            do
            {
                i++;
                value = a * (i * i * i * i) + b * (i * i * i) + c * (i * i) + d * i + e;
            }
            while (value < exp);
            
            level = i - 1;
        }
        
        return level;
    }
    
    public static int getXpToUp(Player player)
    {
        int level = getLevel(player) + 1;
        int exp = player.getTotalExperience();
        int formula = ExpSkills.config.getInt("general.formula", 0);
        
        double value = 0;
        
        if (formula == 0)
        {
            value = 1.75 * (level * level) + 4.5 * level;
            int xptoup = (int) value - exp;
            return xptoup;
        }
        if (formula == 1)
        {
            for (int i = 0; i <= level; i++)
                value = value + i * 10;
            
            int xptoup = (int) value - exp;
            return xptoup;
        }
        if (formula == 2)
        {
            double a = ExpSkills.config.getDouble("general.formula_a", 0);
            double b = ExpSkills.config.getDouble("general.formula_b", 0);
            double c = ExpSkills.config.getDouble("general.formula_c", 0);
            double d = ExpSkills.config.getDouble("general.formula_d", 0);
            double e = ExpSkills.config.getDouble("general.formula_e", 0);
            
            value = a * (level * level * level * level) + b * (level * level * level) + c * (level * level) + d * level + e;
            
            int xptoup = (int) value - exp;
            return xptoup;
        }
        
        return 0;
    }
    
    public static int getXpatLevel(int level)
    {
        int formula = ExpSkills.config.getInt("general.formula", 0);
        
        double value = 0;
        
        if (formula == 0)
        {
            value = 1.75 * (level * level) + 4.5 * level;
            int xpatlevel = (int) value;
            return xpatlevel;
        }
        if (formula == 1)
        {
            for (int i = 0; i <= level; i++)
                value = value + i * 10;
            
            int xpatlevel = (int) value;
            return xpatlevel;
        }
        if (formula == 2)
        {
            double a = ExpSkills.config.getDouble("general.formula_a", 0);
            double b = ExpSkills.config.getDouble("general.formula_b", 0);
            double c = ExpSkills.config.getDouble("general.formula_c", 0);
            double d = ExpSkills.config.getDouble("general.formula_d", 0);
            double e = ExpSkills.config.getDouble("general.formula_e", 0);
            
            value = a * (level * level * level * level) + b * (level * level * level) + c * (level * level) + d * level + e;
            
            int xpatlevel = (int) value;
            return xpatlevel;
        }
        
        return 0;
    }
    
    public static String getPlaytime(Player player)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        long time = pconfig.getLong("onlinetime", 0) / 1000;
        long h = time / 3600;
        long min = (time - h * 3600) / 60;
        long s = time - h * 3600 - min * 60;
        
        return h + "h " + min + "min " + s + "s";
    }
    
    /**
     * Only for internal use
     * 
     * @param player
     * @return online time
     */
    public static long getPlayTimeLong(Player player)
    {
        return FileManager.loadPF(player).getLong("onlinetime", 0);
    }
    
    public static void updatePlaytime(Player player)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        long online = pconfig.getLong("onlinetime", 0);
        long time = pconfig.getLong("donotchange", 0);
        online = online + (System.currentTimeMillis() - time);
        
        pconfig.set("onlinetime", online);
        pconfig.set("donotchange", System.currentTimeMillis());
        
        FileManager.savePF(player, pconfig);
    }
    
    public static double getSkillPoints(Player p)
    {
        return (funcs.getLevel(p) * ExpSkills.config.getDouble("general.skillpoint_modifier", 3.0) - getUsedSkillpoints(p));
    }
    
    public static double getUsedSkillpoints(Player player)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        List<String> skills = pconfig.getStringList("skills");
        
        double points = 0;
        if (skills != null)
        {
            points = points - pconfig.getInt("skillpoints", 0);
            
            for (String skill : skills)
                points = points + ExpSkills.config.getInt("skills." + skill + ".skillpoints", 0);
        }
        return points;
    }
    
    public static void addSkillPoints(Player player, int amount)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        pconfig.set("skillpoints", pconfig.getInt("skillpoints", 0) + amount);
        FileManager.savePF(player, pconfig);
    }
    
    public static void setSkillPoints(Player player, int amount)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        pconfig.set("skillpoints", amount);
        FileManager.savePF(player, pconfig);
    }
    
    public static Set<String> getSkills()
    {
        return ExpSkills.config.getConfigurationSection("skills").getKeys(false);
    }
    
    public static String getSkillKey(String name)
    {
        Set<String> keys = ExpSkills.config.getConfigurationSection("skills").getKeys(false);
        
        for (String skill : keys)
            if (ExpSkills.config.getString("skills." + skill + ".name").toLowerCase().contains(name.toLowerCase()))
                return skill;
        
        return null;
    }
    
    public static String getSkillName(String skill)
    {
        return ExpSkills.config.getString("skills." + skill + ".name");
    }
    
    public static void getInfo(String name, Player player)
    {
        String key = getSkillKey(name);
        if (key == null)
        {
            player.sendMessage(ExpSkills.lang.getString("error.skillnotfound", "Skill not found"));
            return;
        }
        
        String costtype = ExpSkills.config.getString("skills." + key + ".cost_type", "both");
        String costs = ExpSkills.lang.getString("general.costs", "Costs");
        String skillpoints = ExpSkills.lang.getString("general.skillpoints", "Skillpoints");
        String Name = ExpSkills.lang.getString("general.name", "Name");
        String skilllevel = ExpSkills.lang.getString("general.skilllevel", "Skilllevel");
        String neededlevel = ExpSkills.lang.getString("general.neededlevel", "Needed Level");
        String blockedskills = ExpSkills.lang.getString("general.blockedskills", "Blocked Skills");
        String neededskills = ExpSkills.lang.getString("general.neededskills", "Needed Skills");
        String possibleskills = ExpSkills.lang.getString("general.possibleskills", "Possible Skills");
        int money = ExpSkills.config.getInt("skills." + key + ".money", 0);
        int skillp = ExpSkills.config.getInt("skills." + key + ".skillpoints", 0);
        String currency = ExpSkills.config.getString("general.currency", "$");
        
        String skillname = ExpSkills.config.getString("skills." + key + ".name", null);
        
        if (costtype.equalsIgnoreCase("skillpoints"))
            player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + skillp + " " + skillpoints);
        else if (costtype.equalsIgnoreCase("money"))
            player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + money + " " + currency);
        else if (costtype.equalsIgnoreCase("both"))
            player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + money + " " + currency + " " + skillp + " " + skillpoints);
        else
            player.sendMessage(ChatColor.RED + ExpSkills.lang.getString("error.error", "Error! Please contact Admin!"));
        
        player.sendMessage(ChatColor.GOLD + ExpSkills.config.getString("skills." + key + ".info"));
        player.sendMessage(ChatColor.GOLD + neededlevel + ": " + ExpSkills.config.getInt("skills." + key + ".level_need", 0) + " || " + skilllevel + ": " + ExpSkills.config.getInt("skills." + key + ".skill_level", 0));
        
        if (ExpSkills.config.getBoolean("general.use_skilltree", false) && ExpSkills.skilltree.getConfigurationSection("skilltree") != null)
        {
            YamlConfiguration skilltree = FileManager.loadSkilltree();
            List<String> illegal = skilltree.getStringList("skilltree." + key + ".skill_illegal");
            List<String> possible = skilltree.getStringList("skilltree." + key + ".skill_possible");
            List<String> need = skilltree.getStringList("skilltree." + key + ".skill_need");
            String need_type = skilltree.getString("skilltree." + key + ".skill_need_type", "all");
            
            if (illegal != null)
                for (int a = 0; a < illegal.size(); a++)
                {
                    String string = getSkillName(illegal.get(a));
                    illegal.set(a, string);
                }
            
            if (possible != null)
                for (int a = 0; a < possible.size(); a++)
                {
                    String string = getSkillName(possible.get(a));
                    possible.set(a, string);
                }
            
            if (need != null)
                for (int a = 0; a < need.size(); a++)
                {
                    String string = getSkillName(need.get(a));
                    need.set(a, string);
                }
            
            player.sendMessage(ChatColor.GOLD + blockedskills + ": " + illegal);
            player.sendMessage(ChatColor.GOLD + possibleskills + ": " + possible);
            player.sendMessage(ChatColor.GOLD + neededskills + ": " + need);
            
            if (need_type.equalsIgnoreCase("all"))
                player.sendMessage(ChatColor.GOLD + ExpSkills.lang.getString("general.allneed", "You need all of this Skills"));
            else if (need_type.equalsIgnoreCase("or"))
                player.sendMessage(ChatColor.GOLD + ExpSkills.lang.getString("general.oneneeded", "You need only one of this Skills"));
        }
    }
    
    public static boolean rentSkill(String name, Player player, double time)
    {
        String key = getSkillKey(name);
        
        if (key != null)
        {
            if (ExpSkills.config.getBoolean("skills." + key + ".rentable", true))
            {
                if (buyable(name, player, true))
                {
                    List<String> earn = ExpSkills.config.getStringList("skills." + key + ".permissions_earn");
                    List<String> earngrp = ExpSkills.config.getStringList("skills." + key + ".groups_earn");
                    List<String> needgrp = ExpSkills.config.getStringList("skills." + key + ".groups_need");
                    List<String> worlds = ExpSkills.config.getStringList("skills." + key + ".worlds");
                    
                    int skill = ExpSkills.config.getInt("skills." + key + ".skillpoints", 0);
                    double costs = ExpSkills.config.getInt("skills." + key + ".rentcost", 0);
                    double mincosts = ExpSkills.config.getInt("skills." + key + ".rentcost_min", 0);
                    double renttime = ExpSkills.config.getInt("skills." + key + ".renttime", 0);
                    boolean discount = ExpSkills.config.getBoolean("skills." + key + ".rentdiscount", false);
                    
                    boolean money = true;
                    
                    String world;
                    
                    if (ExpSkills.config.getBoolean("general.useGlobalPermissions", false))
                        world = null;
                    else
                        world = player.getWorld().getName();
                    
                    if (discount)
                    {
                        costs = costs * (time / renttime);
                        
                        if (costs < mincosts)
                            costs = mincosts;
                    }
                    
                    if (ExpSkills.method != null)
                    {
                        MethodAccount account = ExpSkills.method.getAccount(player.getName());
                        if (!account.hasEnough(costs))
                            money = false;
                    }
                    else if (vault != null)
                        if (!vault.has(player.getName(), costs))
                            money = false;
                    
                    if (getSkillPoints(player) >= skill && money)
                    {
                        if (ExpSkills.method != null)
                        {
                            MethodAccount account = ExpSkills.method.getAccount(player.getName());
                            account.subtract(costs);
                        }
                        else if (vault != null)
                            vault.withdrawPlayer(player.getName(), costs);
                        
                        if (!worlds.isEmpty())
                        {
                            if (earn != null)
                                for (String node : earn)
                                    PermissionsSystem.addPermission(worlds, player.getName(), node);
                            
                            if (earngrp != null)
                                for (String group : earngrp)
                                    PermissionsSystem.addGroup(worlds, player.getName(), group);
                        }
                        else
                        {
                            // World world = player.getWorld().getName();
                            if (earn != null)
                                for (String node : earn)
                                    PermissionsSystem.addPermission(world, player.getName(), node);
                            
                            if (earngrp != null)
                                for (String group : earngrp)
                                    PermissionsSystem.addGroup(player.getName(), group);
                        }
                        
                        if (needgrp != null && ExpSkills.config.getBoolean("skills." + key + ".revoke_need_groups", false))
                            for (String group : needgrp)
                                PermissionsSystem.removeGroup(player.getName(), group);
                        
                        return true;
                    }
                    
                    if (getSkillPoints(player) <= skill)
                        player.sendMessage(ExpSkills.lang.getString("error.notenoughtskillpoints", "You dont have enought skillpoints"));
                    if (!money)
                        player.sendMessage(ExpSkills.lang.getString("error.notenoughtmoney", "You dont have enought money"));
                    
                    return false;
                }
                return false;
            }
            player.sendMessage(ExpSkills.lang.getString("error.skillnotrentable", "This skill can not be rented."));
            return false;
        }
        player.sendMessage(ExpSkills.lang.getString("error.skillnotfound", "Skill not found"));
        return false;
    }
    
    public static boolean buySkill(String name, Player player)
    {
        String key = getSkillKey(name);
        
        if (key != null)
        {
            if (ExpSkills.config.getBoolean("skills." + key + ".buyable", true))
                if (buyable(name, player, true))
                {
                    List<String> earn = ExpSkills.config.getStringList("skills." + key + ".permissions_earn");
                    List<String> earngrp = ExpSkills.config.getStringList("skills." + key + ".groups_earn");
                    List<String> needgrp = ExpSkills.config.getStringList("skills." + key + ".groups_need");
                    List<String> worlds = ExpSkills.config.getStringList("skills." + key + ".worlds");
                    
                    int skill = ExpSkills.config.getInt("skills." + key + ".skillpoints", 0);
                    int costs = ExpSkills.config.getInt("skills." + key + ".money", 0);
                    
                    String world;
                    
                    if (ExpSkills.config.getBoolean("general.useGlobalPermissions", false))
                        world = null;
                    else
                        world = player.getWorld().getName();
                    
                    boolean money = true;
                    if (ExpSkills.method != null)
                    {
                        MethodAccount account = ExpSkills.method.getAccount(player.getName());
                        if (!account.hasEnough(costs))
                            money = false;
                    }
                    else if (vault != null)
                        if (!vault.has(player.getName(), costs))
                            money = false;
                    
                    if (getSkillPoints(player) >= skill && money)
                    {
                        if (ExpSkills.method != null)
                        {
                            MethodAccount account = ExpSkills.method.getAccount(player.getName());
                            account.subtract(costs);
                        }
                        else if (vault != null)
                            vault.withdrawPlayer(player.getName(), costs);
                        
                        if (!worlds.isEmpty())
                        {
                            if (earn != null)
                                for (String node : earn)
                                    PermissionsSystem.addPermission(worlds, player.getName(), node);
                            
                            if (earngrp != null)
                                for (String group : earngrp)
                                    PermissionsSystem.addGroup(worlds, player.getName(), group);
                        }
                        else
                        {
                            if (earn != null)
                                for (String node : earn)
                                    PermissionsSystem.addPermission(world, player.getName(), node);
                            
                            if (earngrp != null)
                                for (String group : earngrp)
                                    PermissionsSystem.addGroup(player.getName(), group);
                        }
                        
                        if (needgrp != null && ExpSkills.config.getBoolean("skills." + key + ".revoke_need_groups", false))
                            for (String group : needgrp)
                                PermissionsSystem.removeGroup(player.getName(), group);
                        
                        return true;
                    }
                    
                    if (getSkillPoints(player) <= skill)
                        player.sendMessage(ExpSkills.lang.getString("error.notenoughtskillpoints", "You dont have enought skillpoints"));
                    if (!money)
                        player.sendMessage(ExpSkills.lang.getString("error.notenoughtmoney", "You dont have enought money"));
                    
                    return false;
                }
                else
                    return false;
            player.sendMessage(ExpSkills.lang.getString("error.skillnotbuyable", "This skill can not be bought."));
            return false;
        }
        player.sendMessage(ExpSkills.lang.getString("error.skillnotfound", "Skill not found"));
        return false;
    }
    
    public static boolean buyable(String name, Player player, boolean msg)
    {
        String skill = getSkillKey(name);
        
        List<String> need = ExpSkills.config.getStringList("skills." + skill + ".permissions_need");
        List<String> needgrp = ExpSkills.config.getStringList("skills." + skill + ".groups_need");
        
        Set<String> rented = getRented(player);
        
        int neededtime = ExpSkills.config.getInt("skills." + skill + ".time", 0) * 1000;
        
        YamlConfiguration skilltree = FileManager.loadSkilltree();
        YamlConfiguration pconfig = FileManager.loadPF(player);
        List<String> current = pconfig.getStringList("skills");
        boolean skills = true;
        
        // check skilltree
        if (ExpSkills.config.getBoolean("general.use_skilltree", false) && skilltree.getConfigurationSection("skilltree") != null)
            if (skilltree.getConfigurationSection("skilltree").getKeys(false).contains(skill))
            {
                int w = 0;
                List<String> needs = skilltree.getStringList("skilltree." + skill + ".skill_need");
                List<String> illegal = skilltree.getStringList("skilltree." + skill + ".skill_illegal");
                String type = skilltree.getString("skilltree." + skill + ".skill_need_type", "all");
                
                skills = false;
                
                // check if you own a illegal skill
                if (illegal != null && current != null)
                    for (String a : illegal)
                        if (current.contains(a))
                            w++;
                
                // check if you own the needed skills
                if (w == 0 && needs != null)
                {
                    if (type.equalsIgnoreCase("or"))
                    {
                        if (current != null)
                            for (String a : needs)
                                if (current.contains(a))
                                    skills = true;
                    }
                    else if (type.equalsIgnoreCase("all") && current != null)
                        if (current.containsAll(needs))
                            skills = true;
                    
                }
                else if (w == 0)
                    skills = true;
            }
            else
                skills = true;
        
        // perm check
        if (need != null)
            for (String node : need)
                if (!player.hasPermission(node))
                {
                    if (msg)
                        player.sendMessage(ExpSkills.lang.getString("error.permissions", "You don't have the needed permissions"));
                    return false;
                }
        
        if (needgrp != null)
            for (String group : needgrp)
                if (!PermissionsSystem.hasGroup(player.getName(), group))
                {
                    if (msg)
                        player.sendMessage(ExpSkills.lang.getString("error.group", "You don't have the needed group"));
                    return false;
                }
        
        if (skills == false)
        {
            if (msg)
                player.sendMessage(ExpSkills.lang.getString("error.skilltree", "You dont follow the skilltree!"));
            return false;
        }
        // playtime check
        if (neededtime > pconfig.getInt("onlinetime", 0))
        {
            if (msg)
                player.sendMessage(ExpSkills.lang.getString("error.playtime", "You have not enought playtime"));
            return false;
        }
        // level check
        if (ExpSkills.config.getInt("skills." + skill + ".level_need") > getLevel(player))
        {
            if (msg)
                player.sendMessage(ExpSkills.lang.getString("error.level", "You need a higher level"));
            return false;
        }
        // skillcap check
        if (ExpSkills.config.getInt("general.skill_cap", 0) != 0 && current != null)
            if (ExpSkills.config.getInt("general.skill_cap", 0) < (current.size() - pconfig.getInt("extra_skills", 0) + 1))
            {
                if (msg)
                    player.sendMessage(ExpSkills.lang.getString("error.skillcap", "You have reached the skillcap"));
                return false;
            }
        // own check
        if ((rented != null && rented.contains(skill)) || (current != null && current.contains(skill)))
        {
            if (msg)
                player.sendMessage(ExpSkills.lang.getString("error.alreadyown", "You already own this skill!"));
            return false;
        }
        
        return true;
    }
    
    public static void getList(int page, String filter, Player player, boolean all)
    {
        int a = 0;
        int b = 0;
        
        String costs = ExpSkills.lang.getString("general.costs", "Costs");
        String skillpoints = ExpSkills.lang.getString("general.skillpoints", "Skillpoints");
        String Name = ExpSkills.lang.getString("general.name", "Name");
        String desc = ExpSkills.lang.getString("general.description", "Description");
        String currency = ExpSkills.config.getString("general.currency", "$");
        
        player.sendMessage(ChatColor.AQUA + "====================================");
        
        Set<String> skills = getSkills();
        
        if (skills != null)
            for (String skill : skills)
            {
                List<String> list = ExpSkills.config.getStringList("skills." + skill + ".categories");
                
                if ((list != null && ((list.contains(filter)) || filter == null || filter.equalsIgnoreCase("all"))))
                    if (buyable(getSkillName(skill), player, false) || all || (filter != null && filter.equalsIgnoreCase("all")))
                    {
                        if (b >= (page - 1) * 5 && a < 5)
                        {
                            String costtype = ExpSkills.config.getString("skills." + skill + ".cost_type", "both");
                            String skillname = ExpSkills.config.getString("skills." + skill + ".name", null);
                            int skillp = ExpSkills.config.getInt("skills." + skill + ".skillpoints", 0);
                            int money = ExpSkills.config.getInt("skills." + skill + ".money", 0);
                            
                            if (costtype.equalsIgnoreCase("skillpoints"))
                                player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + skillp + " " + skillpoints);
                            else if (costtype.equalsIgnoreCase("money"))
                                player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + money + " " + currency);
                            else if (costtype.equalsIgnoreCase("both"))
                                player.sendMessage(ChatColor.GOLD + Name + ": " + skillname + " || " + costs + ": " + money + " " + currency + " " + skillp + " " + skillpoints);
                            else
                            {
                                player.sendMessage(ChatColor.RED + ExpSkills.lang.getString("error.error", "Error! Please contact Admin!"));
                                player.sendMessage(ChatColor.AQUA + "====================================");
                            }
                            
                            player.sendMessage(ChatColor.GOLD + desc + ": " + ExpSkills.config.getString("skills." + skill + ".description", null));
                            player.sendMessage(ChatColor.AQUA + "====================================");
                            a++;
                        }
                        b++;
                    }
            }
    }
    
    public static Set<String> getRented(Player player)
    {
        if (FileManager.loadRented().getKeys(false).contains(player.getName()))
            return FileManager.loadRented().getConfigurationSection(player.getName()).getKeys(false);
        else
            return null;
    }
    
    public static List<String> getCurrent(Player player)
    {
        return FileManager.loadPF(player).getStringList("skills");
    }
    
    public static List<String> getCats()
    {
        Set<String> list = ExpSkills.config.getConfigurationSection("skills").getKeys(false);
        List<String> cats = new ArrayList<String>();
        
        for (String skill : list)
        {
            List<String> lists = ExpSkills.config.getStringList("skills." + skill + ".categories");
            
            if (lists != null)
                for (String cat : lists)
                    if (!cats.contains(cat))
                        cats.add(cat);
        }
        return cats;
    }
    
    public static boolean grantSkill(Player player, String name)
    {
        String key = getSkillKey(name);
        if (key == null)
        {
            ExpSkills.log.info(ExpSkills.lang.getString("error.skillnotfound", "Skill not found"));
            return false;
        }
        
        List<String> earn = ExpSkills.config.getStringList("skills." + key + ".permissions_earn");
        List<String> earngrp = ExpSkills.config.getStringList("skills." + key + ".groups_earn");
        List<String> needgrp = ExpSkills.config.getStringList("skills." + key + ".groups_need");
        List<String> worlds = ExpSkills.config.getStringList("skills." + key + ".worlds");
        
        String world;
        
        if (ExpSkills.config.getBoolean("general.useGlobalPermissions", false))
            world = null;
        else
            world = player.getWorld().getName();
        
        if (!worlds.isEmpty())
        {
            if (earn != null)
                for (String node : earn)
                    PermissionsSystem.addPermission(worlds, player.getName(), node);
            
            if (earngrp != null)
                for (String group : earngrp)
                    PermissionsSystem.addGroup(worlds, player.getName(), group);
        }
        else
        {
            if (earn != null)
                for (String node : earn)
                    PermissionsSystem.addPermission(world, player.getName(), node);
            
            if (earngrp != null)
                for (String group : earngrp)
                    PermissionsSystem.addGroup(player.getName(), group);
        }
        
        if (needgrp != null && ExpSkills.config.getBoolean("skills." + key + ".revoke_need_groups", false))
            for (String group : needgrp)
                PermissionsSystem.removeGroup(player.getName(), group);
        
        addSkill(player, key);
        return true;
    }
    
    public static boolean revokeSkill(Player player, String skill, boolean exact)
    {
        String key = skill;
        
        if (!exact)
            key = getSkillKey(skill);
        
        if (key == null)
        {
            ExpSkills.log.info(ExpSkills.lang.getString("error.skillnotfound", "Skill not found"));
            return false;
        }
        
        List<String> earn = ExpSkills.config.getStringList("skills." + key + ".permissions_earn");
        List<String> earngrp = ExpSkills.config.getStringList("skills." + key + ".groups_earn");
        List<String> worlds = ExpSkills.config.getStringList("skills." + key + ".worlds");
        
        String world;
        
        if (ExpSkills.config.getBoolean("general.useGlobalPermissions", false))
            world = null;
        else
            world = player.getWorld().getName();
        
        if (!worlds.isEmpty())
        {
            if (earn != null)
                for (String node : earn)
                    PermissionsSystem.removePermission(worlds, player.getName(), node);
            
            if (earngrp != null)
                for (String node : earngrp)
                    PermissionsSystem.removeGroup(worlds, player.getName(), node);
        }
        else
        {
            if (earn != null)
                for (String node : earn)
                    PermissionsSystem.removePermission(world, player.getName(), node);
            
            if (earngrp != null)
                for (String group : earngrp)
                    PermissionsSystem.removeGroup(player.getName(), group);
        }
        removeSkill(player, skill);
        return true;
    }
    
    public static void reset(Player p, String mode)
    {
        YamlConfiguration pconfig = FileManager.loadPF(p);
        
        List<String> skills = pconfig.getStringList("skills");
        
        if (skills != null && !mode.equalsIgnoreCase("level"))
        {
            for (String skill : skills)
                revokeSkill(p, skill, true);
            
            p.sendMessage(ExpSkills.lang.getString("success.skillreset", "Your skills were reset"));
            
            pconfig.set("skills", null);
            
            FileManager.savePF(p, pconfig);
        }
        
        if (mode.equalsIgnoreCase("total") || mode.equalsIgnoreCase("level"))
        {
            setLevel(p, 0);
            setSkillPoints(p, 0);
            setXP(p, 0);
        }
    }
    
    public static void addSkill(Player player, String skill)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        
        List<String> skills = pconfig.getStringList("skills");
        if (skills != null)
            skills.add(skill);
        else
        {
            skills = new ArrayList<String>();
            skills.add(skill);
        }
        
        pconfig.set("skills", skills);
        FileManager.savePF(player, pconfig);
    }
    
    public static void removeSkill(Player player, String skill)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        String key = getSkillKey(skill);
        
        List<String> skills = pconfig.getStringList("skills");
        skills.remove(key);
        
        pconfig.set("skills", skills);
        FileManager.savePF(player, pconfig);
    }
}
