package de.craftlancer.buyskills.commands;

import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillUtils;

public class SkillCommandHandler implements TabExecutor
{
    public HashMap<String, SkillSubCommand> commands = new HashMap<String, SkillSubCommand>();
    
    public SkillCommandHandler(BuySkills plugin)
    {
        commands.put("help", new SkillHelpCommand("buyskills.command.help", plugin));
        commands.put("list", new SkillListCommand("buyskills.command.list", plugin));
        commands.put("info", new SkillInfoCommand("buyskills.command.info", plugin));
        commands.put("buy", new SkillBuyCommand("buyskills.command.buy", plugin));
        commands.put("rent", new SkillRentCommand("buyskills.command.rent", plugin));
        commands.put("current", new SkillCurrentCommand("buyskills.command.current", plugin));
        commands.put("rented", new SkillRentedCommand("buyskills.command.rented", plugin));
        commands.put("grant", new SkillGrantCommand("buyskills.command.grant", plugin));
        commands.put("revoke", new SkillRevokeCommand("buyskills.command.revoke", plugin));
        commands.put("reset", new SkillResetCommand("buyskills.command.reset", plugin));
        commands.put("reload", new SkillReloadCommand("buyskills.command.reload", plugin));
        commands.put("recalculate", new SkillRecalculateCommand("buyskills.command.recalculate", plugin));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0 || !commands.containsKey(args[0]))
            return false;
        else
            commands.get(args[0]).execute(sender, cmd, label, args);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        switch (args.length)
        {
            case 1:
                return SkillUtils.getMatches(args[0], commands.keySet());
            case 2:
                if (!commands.containsKey(args[0]))
                    return null;
                else
                    return commands.get(args[0]).onTabComplete(args);
                
            default:
                return null;
        }
    }
}
