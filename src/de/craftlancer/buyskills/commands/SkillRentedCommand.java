package de.craftlancer.buyskills.commands;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillUtils;

/**
 * Handles the /skill rented command
 */
public class SkillRentedCommand extends SkillSubCommand
{
    public SkillRentedCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) || !(sender instanceof Player))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (!(sender instanceof Player))
            sender.sendMessage(SkillLanguage.COMMAND_PLAYERONLY);
        else
        {
            Player p = (Player) sender;
            
            Map<String, Long> skills = plugin.getPlayerManager().getRentedSkills(p);
            
            sender.sendMessage(SkillLanguage.RENTED_DEFAULT_STRING);
            for (Entry<String, Long> entry : skills.entrySet())
                sender.sendMessage(entry.getKey() + " : " + SkillUtils.getTimeDiffString(entry.getValue()));
        }
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
}
