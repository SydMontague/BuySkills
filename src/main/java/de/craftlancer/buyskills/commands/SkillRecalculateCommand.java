package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillPlayer;

/**
 * Handles the /skill recalculate command
 */
public class SkillRecalculateCommand extends SkillSubCommand
{
    public SkillRecalculateCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (args.length < 2)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        
        SkillPlayer skillPlayer = plugin.getSkillPlayer(args[1]);
        
        if (skillPlayer == null)
            return SkillLanguage.COMMAND_PLAYER_NOT_EXIST.getString();
        
        for (String s : skillPlayer.getSkills())
            skillPlayer.handleSkillGrant(plugin.getSkillByKey(s));
        
        if (plugin.getServer().getPlayerExact(args[1]) != null)
            plugin.getServer().getPlayerExact(args[1]).sendMessage(SkillLanguage.RECALC_NOTIFY.getString());
        
        return SkillLanguage.RECALC_SUCCESS.getString();
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_RECALCULATE.getString());
    }
}
