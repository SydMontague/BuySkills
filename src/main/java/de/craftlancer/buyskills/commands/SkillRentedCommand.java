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
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (!(sender instanceof Player))
            return SkillLanguage.COMMAND_PLAYERONLY.getString();
        
        Map<String, Long> skills = getPlugin().getSkillPlayer(sender.getName()).getRented();
        
        sender.sendMessage(SkillLanguage.RENTED_DEFAULT_STRING.getString());
        for (Entry<String, Long> entry : skills.entrySet())
            sender.sendMessage(entry.getKey() + " : " + SkillUtils.getTimeDiffString(entry.getValue()));
        
        return null;
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null;
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_RENTED.getString());
    }
}
