package com.syd.expskills;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.nijikokun.register.payment.Method.MethodAccount;

public class funcs
{
    static ExpSkills plugin;
    static Economy vault = ExpSkills.economy;

    public static Player getPlayer(String string)
    {
        Player player = ExpSkills.server.getPlayer(string);
        return player;
    }

    public static void addXP(Player player, int exp)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        int i = player.getTotalExperience();
        player.setTotalExperience(i + exp);

        int newxp = player.getTotalExperience() - getXpatLevel(funcs.getLevel(player) - 1);

        pconfig.set("experience", newxp);

        FileManager.savePF(player, pconfig);
    }

    public static void setXP(Player player, int exp)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        player.setTotalExperience(exp);

        int newxp = player.getTotalExperience() - getXpatLevel(funcs.getLevel(player) - 1);

        pconfig.set("experience", newxp);

        FileManager.savePF(player, pconfig);
    }

    public static void setLevel(Player player, int level)
    {
        player.setTotalExperience(funcs.getXpatLevel(level));

        YamlConfiguration pconfig = FileManager.loadPF(player);

        int newxp = player.getTotalExperience();

        pconfig.set("experience", newxp);
        FileManager.savePF(player, pconfig);
    }

    public static void addLevel(Player player, int level)
    {
        player.setTotalExperience(funcs.getXpatLevel(funcs.getLevel(player) + level));

        YamlConfiguration pconfig = FileManager.loadPF(player);

        int newxp = player.getTotalExperience();

        pconfig.set("experience", newxp);
        FileManager.savePF(player, pconfig);
    }

    public static int getLevel(Player player)
    {
        int exp = player.getTotalExperience() + 1;
        int formula = ExpSkills.config.getInt("general.formula", 2);

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
        int formula = ExpSkills.config.getInt("general.formula", 2);

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
            {
                value = value + i * 10;
            }

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
        int formula = ExpSkills.config.getInt("general.formula", 2);

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
            {
                value = value + i * 10;
            }

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
        double skillpoints = 0;
        skillpoints = (funcs.getLevel(p) * ExpSkills.config.getDouble("general.skillpoint_modifier", 3.0) - getUsedSkillpoints(p));
        return skillpoints;
    }

    public static double getUsedSkillpoints(Player player)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);

        @SuppressWarnings("unchecked")
        List<String> skills = pconfig.getList("skills", null);

        double points = 0;
        if (skills != null)
        {
            int num = skills.size();
            points = points - pconfig.getInt("skillpoints", 0);
            if (num != 0)
            {
                for (int i = 0; i <= num - 1; i++)
                {
                    points = points + ExpSkills.config.getInt("skills." + skills.get(i) + ".skillpoints", 0);
                }
            }
        }
        return points;
    }

    public static void addSkillPoints(Player player, double amount)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        double atm = pconfig.getInt("skillpoints", 0);

        pconfig.set("skillpoints", atm + amount);
        FileManager.savePF(player, pconfig);
    }

    public static void setSkillPoints(Player player, int amount)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        pconfig.set("skillpoints", amount);
        FileManager.savePF(player, pconfig);
    }

    public static int getNumSkills()
    {
        Set<String> list = ExpSkills.config.getConfigurationSection("skills").getKeys(false);
        int b = list.size() - 1;
        return b;
    }

    public static List<String> getSkills()
    {
        List<String> list = new ArrayList<String>(ExpSkills.config.getConfigurationSection("skills").getKeys(false));
        return list;
    }

    @Deprecated
    public static int getSkillID(String name)
    {
        List<String> array = getSkills();
        int a = array.size();

        for (int i = 0; i < a; i++)
        {
            if (ExpSkills.config.getString("skills." + array.get(i) + ".name").contains(name))
            {
                return i;
            }
        }
        return -1;
    }

    public static String getSkillKey(String name)
    {
        Set<String> keys = ExpSkills.config.getConfigurationSection("skills").getKeys(false);

        for (String skill : keys)
        {
            if (ExpSkills.config.getString("skills." + skill + ".name").contains(name))
            {
                return skill;
            }
        }
        return null;
    }

    public static String getSkillName(int id)
    {
        return ExpSkills.config.getString("skills.skill" + id + ".name");
    }

    public static String getSkillName(String skill)
    {
        return ExpSkills.config.getString("skills." + skill + ".name");
    }

    @SuppressWarnings("unchecked")
    public static void getInfo(String name, Player player)
    {
        int i = getSkillID(name);
        if (i == -1)
        {
            player.sendMessage(plugin.lang.getString("error.skillnotfound", "Skill not found"));
            return;
        }
        String costtype = ExpSkills.config.getString("skills.skill" + i + ".cost_type", "both");
        String costs = plugin.lang.getString("general.costs", "Costs");
        String skillpoints = plugin.lang.getString("general.skillpoints", "Skillpoints");
        String Name = plugin.lang.getString("general.name", "Name");
        String skilllevel = plugin.lang.getString("general.skilllevel", "Skilllevel");
        String neededlevel = plugin.lang.getString("general.neededlevel", "Needed Level");
        String blockedskills = plugin.lang.getString("general.blockedskills", "Blocked Skills");
        String neededskills = plugin.lang.getString("general.neededskills", "Needed Skills");
        String possibleskills = plugin.lang.getString("general.possibleskills", "Possible Skills");

        if (costtype.equalsIgnoreCase("skillpoints"))
        {
            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + skillpoints);
        }
        else if (costtype.equalsIgnoreCase("money"))
        {
            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$"));
        }
        else if (costtype.equalsIgnoreCase("both"))
        {
            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$") + " " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + skillpoints);
        }
        else
        {
            player.sendMessage(ChatColor.RED + plugin.lang.getString("error.error", "Error! Please contact Admin!"));
        }
        player.sendMessage(ChatColor.GOLD + ExpSkills.config.getString("skills.skill" + i + ".info"));
        player.sendMessage(ChatColor.GOLD + neededlevel + ": " + ExpSkills.config.getInt("skills.skill" + i + ".level_need", 0) + " || " + skilllevel + ": " + ExpSkills.config.getInt("skills.skill" + i + ".skill_level", 0));

        if (ExpSkills.config.getBoolean("general.use_skilltree", false) == true)
        {
            YamlConfiguration skilltree = FileManager.loadSkilltree();
            List<String> illegal = skilltree.getList("skilltree.skill" + i + ".skill_illegal", null);
            List<String> possible = skilltree.getList("skilltree.skill" + i + ".skill_possible", null);
            List<String> need = skilltree.getList("skilltree.skill" + i + ".skill_need", null);
            String need_type = skilltree.getString("skilltree.skill" + i + ".skill_need_type", "all");

            if (illegal != null)
            {
                for (int a = 0; a < illegal.size(); a++)
                {
                    String string = getSkillName(illegal.get(a));
                    illegal.set(a, string);
                }
            }
            if (possible != null)
            {
                for (int a = 0; a < possible.size(); a++)
                {
                    String string = getSkillName(possible.get(a));
                    possible.set(a, string);
                }
            }
            if (need != null)
            {
                for (int a = 0; a < need.size(); a++)
                {
                    String string = getSkillName(need.get(a));
                    need.set(a, string);
                }
            }

            player.sendMessage(ChatColor.GOLD + blockedskills + ": " + illegal);
            player.sendMessage(ChatColor.GOLD + possibleskills + ": " + possible);
            player.sendMessage(ChatColor.GOLD + neededskills + ": " + need);
            if (need_type.equalsIgnoreCase("all"))
            {
                player.sendMessage(ChatColor.GOLD + plugin.lang.getString("general.allneed", "You need all of this Skills"));
            }
            else if (need_type.equalsIgnoreCase("or"))
            {
                player.sendMessage(ChatColor.GOLD + plugin.lang.getString("general.oneneeded", "You need only one of this Skills"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean buySkill(String name, Player player)
    {
        World map = player.getWorld();
        String world = map.getName();
        String key = getSkillKey(name);

        if (key != null)
        {
            List<String> earn = ExpSkills.config.getList("skills." + key + ".permissions_earn", null);
            List<String> earngrp = ExpSkills.config.getList("skills." + key + ".groups_earn", null);
            List<String> needgrp = ExpSkills.config.getList("skills." + key + ".groups_need", null);

            // TO-DO Skilllevels!

            if (buyable(name, player, true))
            {
                int skill = ExpSkills.config.getInt("skills." + key + ".skillpoints", 0);
                int costs = ExpSkills.config.getInt("skills." + key + ".money", 0);

                boolean money = true;
                if (ExpSkills.method != null)
                {
                    MethodAccount account = ExpSkills.method.getAccount(player.getName());
                    if (!account.hasEnough(costs))
                        money = false;
                }
                else if (vault != null)
                {
                    if (!vault.has(player.getName(), costs))
                        money = false;
                }

                if (getSkillPoints(player) >= skill && money)
                {
                    if (ExpSkills.method != null)
                    {
                        MethodAccount account = ExpSkills.method.getAccount(player.getName());
                        account.subtract(costs);
                    }
                    else if (vault != null)
                    {
                        vault.withdrawPlayer(player.getName(), costs);
                    }

                    if (earn != null)
                    {
                        for (String node : earn)
                        {
                            PermissionsSystem.addPermission(world, player.getName(), node);
                        }
                    }
                    if (earngrp != null)
                    {
                        for (String group : earngrp)
                        {
                            PermissionsSystem.addGroup(world, player.getName(), group);
                        }
                    }
                    if (needgrp != null && ExpSkills.config.getBoolean("skills." + key + ".revoke_need_groups", false))
                    {
                        for (String group : needgrp)
                        {
                            PermissionsSystem.removeGroup(world, player.getName(), group);
                        }
                    }

                    return true;
                }

                if (getSkillPoints(player) <= skill)
                    player.sendMessage(plugin.lang.getString("error.notenoughtskillpoints", "You dont have enought skillpoints"));
                if (money == false)
                    player.sendMessage(plugin.lang.getString("error.notenoughtmoney", "You dont have enought money"));

                return false;
            }
            else
                return false;
        }
        player.sendMessage(plugin.lang.getString("error.skillnotfound", "Skill not found"));
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean buyable(String name, Player player, boolean msg)
    {
        String skill = getSkillKey(name);

        ExpSkills.config.getList("skills." + skill + ".permissions_earn", null);
        List<String> need = ExpSkills.config.getList("skills." + skill + ".permissions_need", null);

        ExpSkills.config.getList("skills." + skill + ".groups_earn", null);
        List<String> needgrp = ExpSkills.config.getList("skills." + skill + ".groups_need", null);

        int neededtime = ExpSkills.config.getInt("skills." + skill + ".time", 0);

        YamlConfiguration skilltree = FileManager.loadSkilltree();
        YamlConfiguration pconfig = FileManager.loadPF(player);
        List<String> current = pconfig.getList("skills", null);
        boolean skills = true;

        if (ExpSkills.config.getBoolean("general.use_skilltree", false))
        {
            if (skilltree.getConfigurationSection("skilltree").getKeys(false).contains(skill))
            {
                int w = 0;
                List<String> needs = skilltree.getList("skilltree." + skill + ".skill_need", null);
                List<String> illegal = skilltree.getList("skilltree." + skill + ".skill_illegal", null);
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
                        for (String a : needs)
                            if (current.contains(a) && current != null)
                                skills = true;
                    }
                    else if (type.equalsIgnoreCase("all") && current != null)
                        if (current.containsAll(needs))
                            skills = true;
                }

                else if (w == 0)
                    skills = true;
            }
            else if (!skilltree.getConfigurationSection("skilltree").getKeys(false).contains(skill))
                skills = true;
        }

        // perm check
        if (need != null)
        {
            for (String node : need)
            {
                if (!PermissionsSystem.hasPermission(player.getWorld().getName(), player.getName(), node))
                {
                    if (msg)
                        player.sendMessage(plugin.lang.getString("error.permissions", "You don't have the needed permissions"));
                    return false;
                }
            }
        }
        if (needgrp != null)
        {
            for (String group : needgrp)
            {
                if (!PermissionsSystem.hasGroup(player.getName(), group, player.getWorld().getName()))
                {
                    if (msg)
                        player.sendMessage(plugin.lang.getString("error.group", "You don't have the needed group"));
                    return false;
                }
            }
        }
        if (skills == false)
        {
            if (msg)
                player.sendMessage(plugin.lang.getString("error.skilltree", "You dont follow the skilltree!"));
            return false;
        }
        if (neededtime > pconfig.getInt("onlineTime", 0))
        {
            if (msg)
                player.sendMessage(plugin.lang.getString("error.playtime", "You have not enought playtime"));
            return false;
        }
        if (ExpSkills.config.getInt("skills." + skill + ".level_need") > getLevel(player))
        {
            if (msg)
                player.sendMessage(plugin.lang.getString("error.level", "You need a higher level"));
            return false;
        }
        if (ExpSkills.config.getInt("general.skill_cap", 0) != 0 && current != null)
        {
            if (ExpSkills.config.getInt("general.skill_cap", 0) <= (current.size() - pconfig.getInt("extra_skills", 0)))
            {
                if (msg)
                    player.sendMessage(plugin.lang.getString("error.skillcap", "You have reached the skillcap"));
                return false;
            }
        }
        if (current != null && current.contains(skill))
        {
            if (msg)
                player.sendMessage(plugin.lang.getString("error.alreadyown", "You already own this skill!"));
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void getList(int page, String filter, Player player)
    {
        int num = getNumSkills();
        int a = 0;
        int b = 0;

        String costs = plugin.lang.getString("general.costs", "Costs");
        String skillpoints = plugin.lang.getString("general.skillpoints", "Skillpoints");
        String Name = plugin.lang.getString("general.name", "Name");
        String desc = plugin.lang.getString("general.description", "Description");

        player.sendMessage(ChatColor.AQUA + "====================================");

        for (int i = 0; i <= num; i++)
        {
            List<String> list = ExpSkills.config.getList("skills.skill" + i + ".categories", null);

            if ((list != null && list.contains(filter)) || filter == null)
            {
                if (buyable(getSkillName(i), player, false))
                {
                    if (b >= (page - 1) * 5 && a < 5)
                    {
                        String costtype = ExpSkills.config.getString("skills.skill" + i + ".cost_type", "both");
                        if (costtype.equalsIgnoreCase("skillpoints"))
                        {
                            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + skillpoints);
                        }
                        else if (costtype.equalsIgnoreCase("money"))
                        {
                            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$"));
                        }
                        else if (costtype.equalsIgnoreCase("both"))
                        {
                            player.sendMessage(ChatColor.GOLD + Name + ": " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || " + costs + ": " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$") + " " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + skillpoints);
                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + plugin.lang.getString("error.error", "Error! Please contact Admin!"));
                            player.sendMessage(ChatColor.AQUA + "====================================");
                        }
                        player.sendMessage(ChatColor.GOLD + desc + ": " + ExpSkills.config.getString("skills.skill" + i + ".description", null));
                        player.sendMessage(ChatColor.AQUA + "====================================");
                        a++;
                    }
                    b++;
                }
            }
        }
    }

    public static void getCurrent(Player player)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);

        @SuppressWarnings("unchecked")
        List<String> skills = pconfig.getList("skills", null);

        player.sendMessage(plugin.lang.getString("general.ownedskills", "Owned skills") + ":");
        if (skills != null)
        {
            int a = skills.size();
            if (a >= 0)
            {
                for (int i = 0; i < a;)
                {
                    if (a - i >= 3)
                    {
                        player.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 1) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 2) + ".name"));
                        i = i + 3;
                    }
                    else if (a - i == 2)
                    {
                        player.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 1) + ".name"));
                        i = i + 2;
                    }
                    else if (a - i == 1)
                    {
                        player.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name"));
                        return;
                    }
                }
            }
        }
        else
        {
            player.sendMessage(plugin.lang.getString("error.notanyskillyou", "You dont own any skill"));
        }
    }

    public static void getCurrent(Player player, CommandSender sender)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);

        @SuppressWarnings("unchecked")
        List<String> skills = pconfig.getList("skills", null);

        sender.sendMessage(player.getName() + "'s " + plugin.lang.getString("general.skills", "skills") + ":");
        if (skills != null)
        {
            int a = skills.size();
            if (a >= 0)
            {
                for (int i = 0; i < a;)
                {
                    if (a - i >= 3)
                    {
                        sender.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 1) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 2) + ".name"));
                        i = i + 3;
                    }
                    else if (a - i == 2)
                    {
                        sender.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name") + " " + ExpSkills.config.getString("skills." + skills.get(i + 1) + ".name"));
                        i = i + 2;
                    }
                    else if (a - i == 1)
                    {
                        sender.sendMessage(ExpSkills.config.getString("skills." + skills.get(i) + ".name"));
                        return;
                    }
                }

            }
        }
        else
        {
            sender.sendMessage(plugin.lang.getString("error.notanyskillhe", "This player dont own any skill"));
        }
    }

    public static List<String> getCats()
    {
        List<String> list = new ArrayList<String>(ExpSkills.config.getConfigurationSection("skills").getKeys(false));
        int b = list.size();

        List<String> cats = new ArrayList<String>();

        for (int i = 0; i < b; i++)
        {
            @SuppressWarnings("unchecked")
            List<String> lists = ExpSkills.config.getList("skills." + list.get(i) + ".categories");
            if (lists != null)
            {
                for (int a = 0; a < lists.size(); a++)
                {
                    if (!cats.contains(lists.get(a)))
                    {
                        cats.add(lists.get(a));
                    }
                }
            }
        }
        return cats;
    }

    @SuppressWarnings("unchecked")
    public static boolean grantSkill(Player player, boolean charge, String name)
    {
        int id = getSkillID(name);
        if (id == -1)
        {
            ExpSkills.log.info(plugin.lang.getString("error.skillnotfound", "Skill not found"));
            return false;
        }

        if (charge == true)
        {
            int costs = ExpSkills.config.getInt("skills.skill" + id + ".money", 0);
            MethodAccount account = ExpSkills.method.getAccount(player.getName());
            account.subtract(costs);
        }

        List<String> earn = ExpSkills.config.getList("skills.skill" + id + ".permissions_earn", null);
        List<String> earngrp = ExpSkills.config.getList("skills.skill" + id + ".groups_earn", null);

        if (earn != null)
        {
            for (String node : earn)
            {
                PermissionsSystem.addPermission(player.getWorld().getName(), player.getName(), node);
            }
        }
        if (earngrp != null)
        {
            for (String group : earngrp)
            {
                PermissionsSystem.addGroup(player.getWorld().getName(), player.getName(), group);
            }
        }

        addSkill(player, "skill" + id);
        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean revokeSkill(Player player, String skill)
    {
        int id = getSkillID(skill);
        if (id == -1)
        {
            ExpSkills.log.info(plugin.lang.getString("error.skillnotfound", "Skill not found"));
            return false;
        }

        List<String> earn = ExpSkills.config.getList("skills.skill" + id + ".permissions_earn");
        List<String> earngrp = ExpSkills.config.getList("skills.skill" + id + ".groups_earn");
        if (earn != null)
        {
            for (String node : earn)
            {
                PermissionsSystem.removePermission(player.getWorld().getName(), player.getName(), node);
            }
        }
        if (earngrp != null)
        {
            for (String group : earngrp)
            {
                PermissionsSystem.removeGroup(player.getWorld().getName(), player.getName(), group);
            }
        }

        removeSkill(player, skill);
        return true;
    }

    @SuppressWarnings("unchecked")
    public static void reset(Player p)
    {
        YamlConfiguration pconfig = FileManager.loadPF(p);

        List<String> skills = pconfig.getList("skills", null);
        List<String> perms = new ArrayList<String>();
        List<String> groups = new ArrayList<String>();

        if (skills != null)
        {
            for (int i = 0; i < skills.size(); i++)
            {
                List<String> perm = ExpSkills.config.getList("skills." + skills.get(i) + ".permissions_earn", null);
                if (perm != null)
                {
                    for (String node : perm)
                    {
                        perms.add(node);
                    }
                }

                List<String> group = ExpSkills.config.getList("skills." + skills.get(i) + ".groups_earn", null);
                if (group != null)
                {
                    for (String node : group)
                    {
                        groups.add(node);
                    }
                }
            }

            for (String node : perms)
            {
                PermissionsSystem.removePermission(p.getWorld().getName(), p.getName(), node);
            }

            for (String node : groups)
            {
                PermissionsSystem.removeGroup(p.getWorld().getName(), p.getName(), node);
            }

            p.sendMessage(plugin.lang.getString("success.skillreset", "Your skills were reset"));

            pconfig.set("skills", null);

            FileManager.savePF(p, pconfig);
        }
    }

    public static void addSkill(Player player, String skill)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);

        @SuppressWarnings("unchecked")
        List<String> skills = pconfig.getList("skills", null);
        if (skills != null)
        {
            skills.add(skill);
        }
        else
        {
            skills = new ArrayList<String>();
            skills.add(skill);
        }

        pconfig.set("skills", skills);
        FileManager.savePF(player, pconfig);
    }

    @SuppressWarnings("unchecked")
    public static void removeSkill(Player player, String skill)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        int id = getSkillID(skill);

        List<String> skills = pconfig.getList("skills", null);
        skills.remove("skill" + id);

        pconfig.set("skills", skills);

    }

    // add/remove Nodes/Groups funcs
}
