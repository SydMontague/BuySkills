package de.craftlancer.buyskills.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;

public class SkillRecalculateCommand extends SkillSubCommand
{
    
    public SkillRecalculateCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (args.length < 2)
            sender.sendMessage(SkillLanguage.COMMAND_ARGUMENTS);
        else if (plugin.getServer().getPlayerExact(args[1]) == null)
            sender.sendMessage(SkillLanguage.COMMAND_PLAYER_NOT_EXIST);
        else
        {
            for (String s : plugin.getPlayerManager().getSkills(args[1]))
                plugin.getPlayerManager().handleSkillGrant(args[1], s);
            
            sender.sendMessage(SkillLanguage.RECALC_SUCCESS);
            plugin.getServer().getPlayerExact(args[1]).sendMessage(SkillLanguage.RECALC_NOTIFY);
        }
        
        // TOTEST
    }
    
}
