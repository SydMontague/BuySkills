package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;

public class SkillReloadCommand extends SkillSubCommand
{
    public SkillReloadCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) || !(sender instanceof Player))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else
        {
            plugin.loadConfigurations();
            sender.sendMessage(SkillLanguage.RELOAD_SUCCESS);
        }
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
}
