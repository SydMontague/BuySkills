package de.craftlancer.buyskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SkillSubCommand
{
    String permission = "";
    
    public SkillSubCommand(String perm)
    {
        permission = perm;
    }
    
    public String getPermission()
    {
        return permission;
    }
    
    public abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
}
