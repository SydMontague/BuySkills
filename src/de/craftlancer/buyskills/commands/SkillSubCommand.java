package de.craftlancer.buyskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;

public abstract class SkillSubCommand
{
    String permission = "";
    BuySkills plugin;
    
    public SkillSubCommand(String permission, BuySkills plugin)
    {
        this.permission = permission;
        this.plugin = plugin;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
}
