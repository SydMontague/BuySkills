package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;

/**
 * Handles the /skill reload command
 */
public class SkillReloadCommand extends SkillSubCommand
{
    public SkillReloadCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        
        getPlugin().loadConfigurations();
        return SkillLanguage.RELOAD_SUCCESS.getString();
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_RELOAD.getString());
    }
}
