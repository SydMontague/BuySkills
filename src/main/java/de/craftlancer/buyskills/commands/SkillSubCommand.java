package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;

public abstract class SkillSubCommand
{
    private String permission = "";
    protected BuySkills plugin;
    
    public SkillSubCommand(String permission, BuySkills plugin)
    {
        this.permission = permission;
        this.plugin = plugin;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    protected abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
    
    protected abstract List<String> onTabComplete(String[] args);
}
