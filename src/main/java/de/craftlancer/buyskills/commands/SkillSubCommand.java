package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;

public abstract class SkillSubCommand
{
    private String permission = "";
    private final BuySkills plugin;
    
    public SkillSubCommand(String permission, BuySkills plugin)
    {
        this.permission = permission;
        this.plugin = plugin;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public BuySkills getPlugin()
    {
        return plugin;
    }
    
    protected abstract String execute(CommandSender sender, Command cmd, String label, String[] args);
    
    protected abstract List<String> onTabComplete(String[] args);
    
    public abstract void help(CommandSender sender);
}
