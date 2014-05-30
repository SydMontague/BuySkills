package de.craftlancer.buyskills.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillPlayer;
import de.craftlancer.buyskills.event.BuySkillsResetEvent;

/**
 * Handles the /skill reset command
 */
public class SkillResetCommand extends SkillSubCommand
{
    public SkillResetCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (args.length < 2)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        
        SkillPlayer skillPlayer = getPlugin().getSkillPlayer(args[1]);
        
        if (skillPlayer == null)
            return SkillLanguage.COMMAND_PLAYER_NOT_EXIST.getString();
        
        getPlugin().getServer().getPluginManager().callEvent(new BuySkillsResetEvent(skillPlayer));
        
        for (String s : skillPlayer.getRented().keySet())
            skillPlayer.revokeRented(getPlugin().getSkillByKey(s));
        
        for (String s : new ArrayList<String>(skillPlayer.getBoughtSkills()))
            skillPlayer.revokeSkill(getPlugin().getSkillByKey(s));
        
        if (getPlugin().getServer().getPlayerExact(args[1]) != null)
            getPlugin().getServer().getPlayerExact(args[1]).sendMessage(SkillLanguage.RESET_NOTIFY.getString());
        
        return SkillLanguage.RESET_SUCCESS.getString();
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_RESET.getString());
    }
}
