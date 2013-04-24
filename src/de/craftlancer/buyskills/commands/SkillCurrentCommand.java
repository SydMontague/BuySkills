package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;

public class SkillCurrentCommand extends SkillSubCommand
{
    public SkillCurrentCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()))
            sender.sendMessage(SkillLanguage.COMMAND_PERMISSION);
        else if (!(sender instanceof Player) && args.length <= 1)
            sender.sendMessage(SkillLanguage.COMMAND_ARGUMENTS);
        else if (args.length >= 2 && Bukkit.getPlayerExact(args[1]) == null)
            sender.sendMessage(SkillLanguage.COMMAND_PLAYER_NOT_EXIST);
        else
        {
            Player p;
            if (args.length >= 2)
                p = Bukkit.getPlayerExact(args[1]);
            else
                p = (Player) sender;
            
            List<String> skills = plugin.getPlayerManager().getSkills(p);
            String msg = SkillLanguage.CURRENT_DEFAULT_STRING.replace("%player%", p.getName());
            
            for (int i = 0; i < skills.size(); i++)
            {
                msg = msg + skills.get(i) + " ";
                if (i % 3 == 0 && i != 0)
                    msg = msg + "\n";
            }
            
            sender.sendMessage(msg.split("\n"));
        }
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        return null; // TOTEST player Tabcomplete
    }
}
