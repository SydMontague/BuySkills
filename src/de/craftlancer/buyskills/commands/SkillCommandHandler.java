package de.craftlancer.buyskills.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.craftlancer.buyskills.BuySkills;

public class SkillCommandHandler implements CommandExecutor
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
    
    /*
     * Commands:
     * help - page | command //help pages //
     * list - page category avaible //list skills //
     * info - skill //info about skills //
     * buy - skill //buy skill as player //
     * rent - skill time //rent skill as player for time //
     * current - player //show player's current skills //
     * rented - player //show player's current rented skills //
     * grant - skill player //grant skill to player as admin //
     * revoke - skill player //revoke skill from player as admin //
     * reset - player //reset player's skill as admin //
     * reload - //reload the config //
     * reloadPerm - player //recalculate the player's permissions //
     */
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0 || !commands.containsKey(args[0]))
            return false;
        else
            commands.get(args[0]).execute(sender, cmd, label, args);
        
        return true;
    }
}

enum Commands
{
    HELP("help"), LIST("list"), INFO("info"), BUY("buy"), RENT("rent"), CURRENT("current"), RENTED("rented"), GRANT("grant"), REVOKE("revoke"), RESET("reset"), NEW("new"), REMOVE("remove"), EDIT("edit"), RELOAD("reload"), RELOADPERM("reloadperm");
    
    String command;
    
    Commands(String cmd)
    {
        command = cmd;
    }
}
