package com.syd.expskills;

import java.io.IOException;
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
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setXP(Player player, int exp)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        player.setTotalExperience(exp);

        int newxp = player.getTotalExperience() - getXpatLevel(funcs.getLevel(player) - 1);

        pconfig.set("experience", newxp);
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setLevel(Player player, int level)
    {
        player.setTotalExperience(funcs.getXpatLevel(level));

        YamlConfiguration pconfig = FileManager.loadPF(player);

        int newxp = player.getTotalExperience();

        pconfig.set("experience", newxp);
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void addLevel(Player player, int level)
    {
        player.setTotalExperience(funcs.getXpatLevel(funcs.getLevel(player) + level));

        YamlConfiguration pconfig = FileManager.loadPF(player);

        int newxp = player.getTotalExperience();

        pconfig.set("experience", newxp);
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
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
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setSkillPoints(Player player, int amount)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        pconfig.set("skillpoints", amount);
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
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

    public static String getSkillName(int id)
    {
        return ExpSkills.config.getString("skills.skill" + id + ".name");
    }

    public static String getSkillName(String skill)
    {
        String skills = ExpSkills.config.getString("skills." + skill + ".name");
        return skills;
    }

    @SuppressWarnings("unchecked")
    public static void getInfo(String name, Player player)
    {

        int i = getSkillID(name);
        if (i == -1)
        {
            player.sendMessage("Skill not found!");
            return;
        }

        String costtype = ExpSkills.config.getString("skills.skill" + i + ".cost_type", "both");
        if (costtype.equalsIgnoreCase("skillpoints"))
        {
            player.sendMessage(ChatColor.AQUA + "====================================");
            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
            player.sendMessage(ChatColor.GOLD + ExpSkills.config.getString("skills.skill" + i + ".info"));
            player.sendMessage(ChatColor.GOLD + "Needed Level: " + ExpSkills.config.getInt("skills.skill" + i + ".level_need", 0) + "|| Skilllevel: " + ExpSkills.config.getInt("skills.skill" + i + ".skill_level", 0));
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

                player.sendMessage(ChatColor.GOLD + "Blocked Skills: " + illegal);
                player.sendMessage(ChatColor.GOLD + "Possible Skills: " + possible);
                player.sendMessage(ChatColor.GOLD + "Needed Skills: " + need);
                if (need_type.equalsIgnoreCase("all"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need all needed Skill to buy this");
                }
                else if (need_type.equalsIgnoreCase("or"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need one needes Skill to buy this");
                }
            }
        }
        else if (costtype.equalsIgnoreCase("money"))
        {
            player.sendMessage(ChatColor.AQUA + "====================================");
            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$"));
            player.sendMessage(ChatColor.GOLD + ExpSkills.config.getString("skills.skill" + i + ".info"));
            player.sendMessage(ChatColor.GOLD + "Needed Level: " + ExpSkills.config.getInt("skills.skill" + i + ".level_need", 0) + "|| Skilllevel: " + ExpSkills.config.getInt("skills.skill" + i + ".skill_level", 0));
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

                player.sendMessage(ChatColor.GOLD + "Blocked Skills: " + illegal);
                player.sendMessage(ChatColor.GOLD + "Possible Skills: " + possible);
                player.sendMessage(ChatColor.GOLD + "Needed Skills: " + need);
                if (need_type.equalsIgnoreCase("all"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need all needed Skill to buy this");
                }
                else if (need_type.equalsIgnoreCase("or"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need one needes Skill to buy this");
                }
            }
        }
        else if (costtype.equalsIgnoreCase("both"))
        {
            player.sendMessage(ChatColor.AQUA + "====================================");
            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$") + " " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
            player.sendMessage(ChatColor.GOLD + ExpSkills.config.getString("skills.skill" + i + ".info"));
            player.sendMessage(ChatColor.GOLD + "Needed Level: " + ExpSkills.config.getInt("skills.skill" + i + ".level_need", 0) + "|| Skilllevel: " + ExpSkills.config.getInt("skills.skill" + i + ".skill_level", 0));
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

                player.sendMessage(ChatColor.GOLD + "Blocked Skills: " + illegal);
                player.sendMessage(ChatColor.GOLD + "Possible Skills: " + possible);
                player.sendMessage(ChatColor.GOLD + "Needed Skills: " + need);
                if (need_type.equalsIgnoreCase("all"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need all needed Skill to buy this");
                }
                else if (need_type.equalsIgnoreCase("or"))
                {
                    player.sendMessage(ChatColor.GOLD + "You need one needes Skill to buy this");
                }
            }
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Error in config! Please contact admin!");
            player.sendMessage(ChatColor.AQUA + "====================================");
        }
    }

    // add level_need check
    // add skill_level check
    @SuppressWarnings("unchecked")
    public static String buySkill(String name, Player player)
    {
        World map = player.getWorld();
        String world = map.getName();
        int id = getSkillID(name);
        if (id != -1)
        {
            List<String> earn = ExpSkills.config.getList("skills.skill" + id + ".permissions_earn", null);
            List<String> need = ExpSkills.config.getList("skills.skill" + id + ".permissions_need", null);

            List<String> earngrp = ExpSkills.config.getList("skills.skill" + id + ".groups_earn", null);
            List<String> needgrp = ExpSkills.config.getList("skills.skill" + id + ".groups_need", null);

            String costtype = ExpSkills.config.getString("skills.skill" + id + ".cost_type", "both");
            YamlConfiguration skilltree = FileManager.loadSkilltree();
            YamlConfiguration pconfig = FileManager.loadPF(player);
            List<String> current = pconfig.getList("skills", null);
            boolean skills = false;
            int b = 0;
            int c = 0;

            if (ExpSkills.config.getBoolean("general.use_skilltree", false))
            {
                if (skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + id))
                {
                    int w = 0;
                    List<String> needs = skilltree.getList("skilltree.skill" + id + ".skill_need", null);
                    List<String> illegal = skilltree.getList("skilltree.skill" + id + ".skill_illegal", null);
                    String type = skilltree.getString("skilltree.skill" + id + ".skill_need_type", "all");

                    skills = false;

                    if (illegal != null && current != null)
                    {
                        for (int v = 0; v < illegal.size(); v++)
                        {
                            if (current.contains(illegal.get(v)))
                            {
                                w++;
                            }
                        }
                    }
                    if (w == 0 && needs != null)
                    {
                        if (type.equalsIgnoreCase("or"))
                        {
                            for (int d = 0; d < needs.size(); d++)
                            {
                                if (current.contains(needs.get(d)) && current != null)
                                {
                                    skills = true;
                                }
                            }
                        }
                        else if (type.equalsIgnoreCase("all") && current != null)
                        {
                            if (current.containsAll(needs))
                            {
                                skills = true;
                            }
                        }
                    }
                    else if (w == 0)
                    {
                        skills = true;
                    }
                }
                else if (!skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + id))
                {
                    skills = true;
                }
            }
            else
                skills = true;

            if (skills == false)
            {
                player.sendMessage("You don't follow the skilltree!");
                return null;
            }

            if (ExpSkills.config.getInt("skills.skill" + id + ".level_need") > getLevel(player))
            {
                player.sendMessage("You need a higher level!");
                return null;
            }

            if (ExpSkills.config.getInt("general.skill_cap", 0) != 0 && current != null)
            {
                if (ExpSkills.config.getInt("general.skill_cap", 0) <= (current.size() - pconfig.getInt("extra_skills", 0)))
                {
                    player.sendMessage("You have reached your skill cap");
                    return null;
                }
            }
            // TO-DO Skilllevels!

            if (earn != null)
            {
                for (int i = 0; i <= earn.size() - 1; i++)
                {
                    if (PermissionsSystem.hasPermission(world, player.getName(), earn.get(i)))
                    {
                        b++;
                    }
                }
            }
            if (earngrp != null)
            {
                for (int i = 0; i <= earngrp.size() - 1; i++)
                {
                    if (PermissionsSystem.hasGroup(world, player.getName(), earngrp.get(i)))
                    {
                        c++;
                    }
                }
            }

            if ((earn == null || b != earn.size()) && (earngrp == null || c != earngrp.size()))
            {
                if (need != null)
                {
                    int size = need.size();
                    for (int i = 0; i <= size - 1; i++)
                    {

                        if (PermissionsSystem.hasPermission(world, player.getName(), need.get(i)))
                        {
                        }
                        else
                        {
                            player.sendMessage("Not enough permissions!");
                            return null;
                        }
                    }
                }
                if (needgrp != null)
                {
                    int sizegrp = needgrp.size();
                    for (int i = 0; i <= sizegrp - 1; i++)
                    {
                        if (PermissionsSystem.hasGroup(player.getName(), needgrp.get(i), world))
                        {
                        }
                        else
                        {
                            player.sendMessage("Not enough permissions!");
                            return null;
                        }
                    }
                }

                if (costtype.equalsIgnoreCase("skillpoints"))
                {
                    int skill = ExpSkills.config.getInt("skills.skill" + id + ".skillpoints", 0);
                    
                    if (getSkillPoints(player) >= skill)
                    {
                        addSkill(player, "skill" + id);
                        player.sendMessage("Skill successfully bought!");
                    }
                    else
                    {
                        player.sendMessage("Not enough skillpoints!");
                        return null;
                    }
                }
                else if (costtype.equalsIgnoreCase("money"))
                {
                    int costs = ExpSkills.config.getInt("skills.skill" + id + ".money", 0);

                    if (ExpSkills.method != null)
                    {
                        MethodAccount account = ExpSkills.method.getAccount(player.getName());
                        if (account.hasEnough(costs))
                        {
                            account.subtract(costs);
                            addSkill(player, "skill" + id);
                            player.sendMessage("Skill successfully bought!");
                        }
                        else
                        {
                            player.sendMessage("Not enough money!");
                            return null;
                        }
                    }                    
                    else if (vault != null)
                    {
                        if (vault.has(player.getName(), costs))
                        {
                            vault.withdrawPlayer(player.getName(), costs);
                            addSkill(player, "skill" + id);
                            player.sendMessage("Skill successfully bought!");
                        }
                        else
                        {
                            player.sendMessage("Not enough money!");
                            return null;
                        }
                    }
                    else
                    {
                        ExpSkills.log.warning("You need Register and a supported Economy plugin in order to use economy features!");
                    }

                }
                else if (costtype.equalsIgnoreCase("both"))
                {
                    int money = ExpSkills.config.getInt("skills.skill" + id + ".money", 0);
                    int skill = ExpSkills.config.getInt("skills.skill" + id + ".skillpoints", 0);

                    if (ExpSkills.method != null)
                    {
                        MethodAccount account = null;
                        account = ExpSkills.method.getAccount(player.getName());
                        if (account.hasEnough(money) && getSkillPoints(player) >= skill)
                        {
                            account.subtract(money);
                            addSkill(player, "skill" + id);
                            player.sendMessage("Skill successfully bought!");
                        }
                        else
                        {
                            if (!account.hasEnough(money))
                            {
                                player.sendMessage("Not enough money!");
                                return null;
                            }
                            if (getSkillPoints(player) < skill)
                            {
                                player.sendMessage("Not enough skillpoints!");
                                return null;
                            }
                        }
                    }
                    if (vault != null)
                    {
                        if (vault.has(player.getName(), money) && getSkillPoints(player) >= skill)
                        {
                            vault.withdrawPlayer(player.getName(), money);
                            addSkill(player, "skill" + id);
                            player.sendMessage("Skill successfully bought!");
                        }
                        else
                        {
                            if (!vault.has(player.getName(), money))
                            {
                                player.sendMessage("Not enough money!");
                                return null;
                            }
                            if (getSkillPoints(player) < skill)
                            {
                                player.sendMessage("Not enough skillpoints!");
                                return null;
                            }
                        }
                    }
                    else
                    {
                        ExpSkills.log.warning("You need Register and a supported Economy plugin in order to use economy features!");
                    }
                }

                if (earn != null)
                {
                    int size2 = earn.size();
                    for (int i = 0; i - 1 < size2 - 1; i++)
                    {
                        PermissionsSystem.addPermission(world, player.getName(), earn.get(i));
                    }
                }
                if (earngrp != null)
                {
                    int size3 = earngrp.size();
                    for (int i = 0; i - 1 < size3 - 1; i++)
                    {
                        PermissionsSystem.addGroup(world, player.getName(), earngrp.get(i));
                    }
                }
                if (needgrp != null && ExpSkills.config.getBoolean("skills.skill" + id + ".revoke_need_groups", false))
                {
                    int size3 = needgrp.size();
                    for (int i = 0; i - 1 < size3 - 1; i++)
                    {
                        PermissionsSystem.removeGroup(world, player.getName(), needgrp.get(i));
                    }
                }

                return null;
            }
            else
                player.sendMessage("You already own this right!");
            return null;
        }
        player.sendMessage("Skill is not existing!");
        return null;
    }

    @SuppressWarnings("unchecked")
    public static void getList(int page, String filter, Player player)
    {
        int num = getNumSkills();
        boolean perm = true;
        boolean skill = false;
        int a = 0;
        int b = 0;
        YamlConfiguration pconfig = FileManager.loadPF(player);
        List<String> current = pconfig.getList("skills", null);
        YamlConfiguration skilltree = FileManager.loadSkilltree();
        player.sendMessage(ChatColor.AQUA + "====================================");

        for (int i = 0; i <= num; i++)
        {
            List<String> list = ExpSkills.config.getList("skills.skill" + i + ".categories", null);
            if (list != null)
            {
                if (list.contains(filter))
                {
                    if (ExpSkills.config.getBoolean("general.use_skilltree", false))
                    {
                        if (skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + i))
                        {
                            int w = 0;
                            List<String> need = skilltree.getList("skilltree.skill" + i + ".skill_need");
                            List<String> illegal = skilltree.getList("skilltree.skill" + i + ".skill_illegal");
                            String type = skilltree.getString("skilltree.skill" + i + ".skill_need_type", "all");
                            skill = false;

                            if (illegal != null && current != null)
                            {
                                for (int v = 0; v < illegal.size(); v++)
                                {
                                    if (current.contains(illegal.get(v)))
                                    {
                                        w++;
                                    }
                                }
                            }
                            if (w == 0 && need != null && current != null)
                            {
                                if (type.equalsIgnoreCase("or"))
                                {
                                    for (int d = 0; d < need.size(); d++)
                                    {
                                        if (current.contains(need.get(d)))
                                        {
                                            skill = true;
                                        }
                                    }
                                }
                                else if (type.equalsIgnoreCase("all"))
                                {
                                    if (current.containsAll(need))
                                    {
                                        skill = true;
                                    }
                                }
                            }
                            else if (need == null)
                            {
                                skill = true;
                            }
                        }
                        else if (!skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + i))
                        {
                            skill = true;
                        }
                        else
                            skill = true;
                    }
                    else
                        skill = true;

                    if (current == null || !current.contains("skill" + i))
                    {
                        List<String> need = ExpSkills.config.getList("skills.skill" + i + ".permissions_need", null);
                        if (need != null)
                        {
                            int size = need.size();

                            for (int o = 0; o < size - 1; o++)
                            {
                                if (PermissionsSystem.hasPermission(player.getWorld().getName(), player.getName(), need.get(o)))
                                {
                                }
                                else
                                {
                                    perm = false;
                                }
                            }
                        }

                        if (perm == true && skill == true)
                        {
                            if (b >= (page - 1) * 5 && a < 5)
                            {
                                String costtype = ExpSkills.config.getString("skills.skill" + i + ".cost_type", "both");
                                if (costtype.equalsIgnoreCase("skillpoints"))
                                {
                                    player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
                                    player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                                    // player.sendMessage(ChatColor.GOLD +
                                    // "Needed Level: " +
                                    // ExpSkills.config.getInt("skills.skill" +
                                    // i + ".level_need", 0) + "|| Skilllevel: "
                                    // + ExpSkills.config.getInt("skills.skill"
                                    // + i + ".skill_level", 0));
                                    player.sendMessage(ChatColor.AQUA + "====================================");
                                }
                                else if (costtype.equalsIgnoreCase("money"))
                                {
                                    player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$"));
                                    player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                                    // player.sendMessage(ChatColor.GOLD +
                                    // "Needed Level: " +
                                    // ExpSkills.config.getInt("skills.skill" +
                                    // i + ".level_need", 0) + "|| Skilllevel: "
                                    // + ExpSkills.config.getInt("skills.skill"
                                    // + i + ".skill_level", 0));
                                    player.sendMessage(ChatColor.AQUA + "====================================");
                                }
                                else if (costtype.equalsIgnoreCase("both"))
                                {
                                    player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$") + " " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
                                    player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                                    // player.sendMessage(ChatColor.GOLD +
                                    // "Needed Level: " +
                                    // ExpSkills.config.getInt("skills.skill" +
                                    // i + ".level_need", 0) + "|| Skilllevel: "
                                    // + ExpSkills.config.getInt("skills.skill"
                                    // + i + ".skill_level", 0));
                                    player.sendMessage(ChatColor.AQUA + "====================================");
                                }
                                else
                                {
                                    player.sendMessage(ChatColor.RED + "Error in config. Please contact admin!");
                                    player.sendMessage(ChatColor.AQUA + "====================================");
                                }
                                a++;
                            }
                            b++;
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void getList(int page, Player player)
    {
        int num = getNumSkills();
        boolean perm = true;
        boolean skill = false;
        int b = 0;
        int a = 0;
        YamlConfiguration pconfig = FileManager.loadPF(player);
        YamlConfiguration skilltree = FileManager.loadSkilltree();
        List<String> current = pconfig.getList("skills", null);

        player.sendMessage(ChatColor.AQUA + "====================================");

        for (int i = 0; i <= num; i++)
        {
            if (ExpSkills.config.getBoolean("general.use_skilltree", false))
            {
                if (skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + i))
                {
                    int w = 0;
                    List<String> need = skilltree.getList("skilltree.skill" + i + ".skill_need");
                    List<String> illegal = skilltree.getList("skilltree.skill" + i + ".skill_illegal");
                    String type = skilltree.getString("skilltree.skill" + i + ".skill_need_type", "all");
                    skill = false;

                    if (illegal != null && current != null)
                    {
                        for (int v = 0; v < illegal.size(); v++)
                        {
                            if (current.contains(illegal.get(v)))
                            {
                                w++;
                            }
                        }
                    }
                    if (w == 0 && need != null && current != null)
                    {
                        if (type.equalsIgnoreCase("or"))
                        {
                            for (int d = 0; d < need.size(); d++)
                            {
                                if (current.contains(need.get(d)))
                                {
                                    skill = true;
                                }
                            }
                        }
                        else if (type.equalsIgnoreCase("all"))
                        {
                            if (current.containsAll(need))
                            {
                                skill = true;
                            }
                        }
                    }
                    else if (need == null)
                    {
                        skill = true;
                    }
                }
                else if (!skilltree.getConfigurationSection("skilltree").getKeys(false).contains("skill" + i))
                {
                    skill = true;
                }
                else
                    skill = true;
            }
            else
                skill = true;

            if (current == null || !current.contains("skill" + i))
            {
                List<String> need = ExpSkills.config.getList("skills.skill" + i + ".permissions_need", null);
                if (need != null)
                {
                    int size = need.size();

                    for (int o = 0; o < size - 1; o++)
                    {
                        if (PermissionsSystem.hasPermission(player.getWorld().getName(), player.getName(), need.get(o)))
                        {
                        }
                        else
                        {
                            perm = false;
                        }
                    }
                }

                if (perm == true && skill == true)
                {
                    if (b >= (page - 1) * 5 && a < 5)
                    {
                        String costtype = ExpSkills.config.getString("skills.skill" + i + ".cost_type", "both");
                        if (costtype.equalsIgnoreCase("skillpoints"))
                        {
                            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
                            player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                            // player.sendMessage(ChatColor.GOLD +
                            // "Needed Level: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".level_need", 0) + "|| Skilllevel: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".skill_level", 0));
                            player.sendMessage(ChatColor.AQUA + "====================================");
                        }
                        else if (costtype.equalsIgnoreCase("money"))
                        {
                            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$"));
                            player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                            // player.sendMessage(ChatColor.GOLD +
                            // "Needed Level: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".level_need", 0) + "|| Skilllevel: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".skill_level", 0));
                            player.sendMessage(ChatColor.AQUA + "====================================");
                        }
                        else if (costtype.equalsIgnoreCase("both"))
                        {
                            player.sendMessage(ChatColor.GOLD + "Name: " + ExpSkills.config.getString("skills.skill" + i + ".name", null) + " || Costs: " + ExpSkills.config.getInt("skills.skill" + i + ".money", 0) + " " + ExpSkills.config.getString("general.currency", "$") + " " + ExpSkills.config.getInt("skills.skill" + i + ".skillpoints", 0) + " Skillpoints");
                            player.sendMessage(ChatColor.GOLD + "Description: " + ExpSkills.config.getString("skills.skill" + i + ".description", null)); // description
                            // player.sendMessage(ChatColor.GOLD +
                            // "Needed Level: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".level_need", 0) + "|| Skilllevel: " +
                            // ExpSkills.config.getInt("skills.skill" + i +
                            // ".skill_level", 0));
                            player.sendMessage(ChatColor.AQUA + "====================================");
                        }
                        else
                        {
                            player.sendMessage(ChatColor.RED + "Error in config. Please contact admin!");
                            player.sendMessage(ChatColor.AQUA + "====================================");
                        }
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

        player.sendMessage("Owned skills:");
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
            player.sendMessage("You dont own any skill!");
        }
    }

    public static void getCurrent(Player player, CommandSender sender)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);

        @SuppressWarnings("unchecked")
        List<String> skills = pconfig.getList("skills", null);

        sender.sendMessage(player.getName() + "'s skills:");
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
            sender.sendMessage("This player dont own any skill!");
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
                    else
                    {
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
            ExpSkills.log.info("Skill does not exist!");
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
            int size2 = earn.size();
            for (int i = 0; i - 1 < size2 - 1; i++)
            {
                PermissionsSystem.addPermission(player.getWorld().getName(), player.getName(), earn.get(i));
            }
        }
        if (earngrp != null)
        {
            int size3 = earngrp.size();
            for (int i = 0; i - 1 < size3 - 1; i++)
            {
                PermissionsSystem.addGroup(player.getWorld().getName(), player.getName(), earngrp.get(i));
            }
        }

        addSkill(player, "skill" + id);
        return true;
    }

    @SuppressWarnings("unchecked")
    public static boolean revokeSkill(Player player, boolean payout, String skill)
    {
        int id = getSkillID(skill);
        if (id == -1)
        {
            ExpSkills.log.info("Skill does not exist!");
            return false;
        }
        if (payout == true)
        {
            int costs = ExpSkills.config.getInt("skills.skill" + id + ".money", 0);
            MethodAccount account = ExpSkills.method.getAccount(player.getName());
            account.add(costs);
        }

        List<String> earn = ExpSkills.config.getList("skills.skill" + id + ".permissions_earn");
        List<String> earngrp = ExpSkills.config.getList("skills.skill" + id + ".groups_earn");
        if (earn != null)
        {
            for (int i = 0; i - 1 < earn.size() - 1; i++)
            {
                PermissionsSystem.removePermission(player.getWorld().getName(), player.getName(), earn.get(i));
            }
        }
        if (earngrp != null)
        {
            for (int i = 0; i - 1 < earngrp.size() - 1; i++)
            {
                PermissionsSystem.removeGroup(player.getWorld().getName(), player.getName(), earngrp.get(i));
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
                    for (int a = 0; a < perm.size(); a++)
                    {
                        perms.add(perm.get(a));
                    }

                    for (int s = 0; s < perms.size(); s++)
                    {
                        PermissionsSystem.removePermission(p.getWorld().getName(), p.getName(), perms.get(s));
                    }
                }

                List<String> group = ExpSkills.config.getList("skills." + skills.get(i) + ".groups_earn", null);
                if (group != null)
                {
                    for (int b = 0; b < group.size(); b++)
                    {
                        groups.add(group.get(b));
                    }

                    for (int t = 0; t < groups.size(); t++)
                    {
                        PermissionsSystem.removeGroup(p.getWorld().getName(), p.getName(), groups.get(t));
                    }
                }
            }

            p.sendMessage("Your skills were resetted!");

            pconfig.set("skills", null);

            try
            {
                pconfig.save("plugins/ExpSkills/player/" + p.getName() + ".yml");
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

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
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void removeSkill(Player player, String skill)
    {
        YamlConfiguration pconfig = FileManager.loadPF(player);
        int id = getSkillID(skill);

        List<String> skills = pconfig.getList("skills", null);
        skills.remove("skill" + id);

        pconfig.set("skills", skills);
        try
        {
            pconfig.save("plugins/ExpSkills/player/" + player.getName() + ".yml");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    // add/remove Nodes/Groups funcs
}
