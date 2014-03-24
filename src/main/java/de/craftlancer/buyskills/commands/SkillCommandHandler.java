package de.craftlancer.buyskills.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillUtils;

/**
 * Handles all /skill commands
 */
public class SkillCommandHandler implements TabExecutor
{
    private HashMap<String, SkillSubCommand> commands = new HashMap<String, SkillSubCommand>();
    
    private static String[] parseArgumentStrings(String[] args)
    {
        List<String> tmp = new ArrayList<String>();
        
        StringBuilder b = null;
        boolean open = false;
        
        for (String s : args)
        {
            if (b == null)
            {
                if (s.startsWith("\"") && !s.endsWith("\""))
                {
                    b = new StringBuilder();
                    b.append(s.substring(1));
                    b.append(" ");
                }
                else
                    tmp.add(s);
            }
            else
            {
                if ((s.endsWith("\"") && !open) || s.endsWith("\"\""))
                {
                    b.append(s.substring(0, s.length() - 1));
                    tmp.add(b.toString());
                    b = null;
                }
                else if (s.startsWith("\""))
                {
                    if (open)
                        return null;
                    
                    open = true;
                    b.append(s);
                    b.append(" ");
                }
                else
                {
                    if (s.endsWith("\""))
                        if (open)
                            open = false;
                        else
                            return null;
                    
                    b.append(s);
                    b.append(" ");
                }
            }
            
        }
        if (b != null)
            return null;
        
        return tmp.toArray(new String[tmp.size()]);
    }
    
    public SkillCommandHandler(BuySkills plugin)
    {
        registerSubCommand("help", new SkillHelpCommand("buyskills.command.help", plugin, commands));
        registerSubCommand("list", new SkillListCommand("buyskills.command.list", plugin));
        registerSubCommand("info", new SkillInfoCommand("buyskills.command.info", plugin));
        registerSubCommand("buy", new SkillBuyCommand("buyskills.command.buy", plugin));
        registerSubCommand("rent", new SkillRentCommand("buyskills.command.rent", plugin));
        registerSubCommand("current", new SkillCurrentCommand("buyskills.command.current", plugin));
        registerSubCommand("rented", new SkillRentedCommand("buyskills.command.rented", plugin));
        registerSubCommand("grant", new SkillGrantCommand("buyskills.command.grant", plugin));
        registerSubCommand("revoke", new SkillRevokeCommand("buyskills.command.revoke", plugin));
        registerSubCommand("reset", new SkillResetCommand("buyskills.command.reset", plugin));
        registerSubCommand("reload", new SkillReloadCommand("buyskills.command.reload", plugin));
        registerSubCommand("recalculate", new SkillRecalculateCommand("buyskills.command.recalculate", plugin));
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        args = parseArgumentStrings(args);
        
        String message;
        
        if (args.length == 0 || !commands.containsKey(args[0]))
            if (commands.containsKey("help"))
                message = commands.get("help").execute(sender, cmd, label, args);
            else
                return false;
        else
            message = commands.get(args[0]).execute(sender, cmd, label, args);
        
        if (message != null)
            sender.sendMessage(message);
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        switch (args.length)
        {
            case 0:
                return null;
            case 1:
                List<String> l = SkillUtils.getMatches(args[0], commands.keySet());
                for (String str : l)
                    if (!sender.hasPermission(commands.get(str).getPermission()))
                        l.remove(str);
                return l;
            default:
                if (!commands.containsKey(args[0]))
                    return null;
                else
                    return commands.get(args[0]).onTabComplete(args);
        }
    }
    
    public void registerSubCommand(String name, SkillSubCommand command, String... alias)
    {
        Validate.notNull(command, "Command can't be null!");
        Validate.notEmpty(name, "Commandname can't be empty!");
        commands.put(name, command);
        for (String s : alias)
            commands.put(s, command);
    }
    
    protected Map<String, SkillSubCommand> getCommands()
    {
        return commands;
    }
}
