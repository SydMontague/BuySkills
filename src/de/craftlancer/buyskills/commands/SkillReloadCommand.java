package de.craftlancer.buyskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;

public class SkillReloadCommand extends SkillSubCommand
{
    private BuySkills plugin;
    
    public SkillReloadCommand(String perm, BuySkills plugin)
    {
        super(perm);
        this.plugin = plugin;
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else
            plugin.loadConfigurations();
    }
    
}
