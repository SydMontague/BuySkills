package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillPlayer;

/**
 * Handles the /skill current command
 */
public class SkillCurrentCommand extends SkillSubCommand
{
    public SkillCurrentCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (!(sender instanceof Player) && args.length <= 1)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) == null)
            return SkillLanguage.COMMAND_PLAYER_NOT_EXIST.getString();
        
        SkillPlayer skillPlayer = args.length >= 2 ? getPlugin().getSkillPlayer(args[1]) : getPlugin().getSkillPlayer((Player) sender);
        
        List<String> skills = skillPlayer.getSkills();
        StringBuilder msg = new StringBuilder(SkillLanguage.CURRENT_DEFAULT_STRING.getString().replace("%player%", skillPlayer.getName()) + "\n");
        
        for (int i = 0; i < skills.size(); i++)
        {
            msg.append(getPlugin().getSkillByKey(skills.get(i)).getName()).append(" ");
            if (i % 3 == 0 && i != 0)
                msg.append("\n");
        }
        
        // TOTEST is split needed?
        sender.sendMessage(msg.toString().split("\n"));
        return null;
    }
    
    @Override
    protected List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_CURRENT.getString());
    }
}
