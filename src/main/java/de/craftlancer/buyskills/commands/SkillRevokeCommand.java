package de.craftlancer.buyskills.commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.craftlancer.buyskills.BuySkills;
import de.craftlancer.buyskills.SkillLanguage;
import de.craftlancer.buyskills.SkillPlayer;
import de.craftlancer.buyskills.SkillUtils;
import de.craftlancer.buyskills.event.BuySkillsRevokeEvent;

/**
 * Handles the /skill revoke command
 */
public class SkillRevokeCommand extends SkillSubCommand
{
    public SkillRevokeCommand(String perm, BuySkills plugin)
    {
        super(perm, plugin);
    }
    
    @SuppressWarnings("deprecation")
    @Override
    public String execute(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!sender.hasPermission(getPermission()) && sender instanceof Player)
            return SkillLanguage.COMMAND_PERMISSION.getString();
        if (args.length < 3)
            return SkillLanguage.COMMAND_ARGUMENTS.getString();
        
        SkillPlayer skillPlayer = getPlugin().getSkillPlayer(args[1]);
        
        if (skillPlayer == null)
            return SkillLanguage.COMMAND_PLAYER_NOT_EXIST.getString();
        if (!getPlugin().hasSkill(args[2]))
            return SkillLanguage.COMMAND_SKILL_NOT_EXIST.getString();
        if (!skillPlayer.hasSkill(args[2]))
            return SkillLanguage.REVOKE_NOT_OWN.getString();
        
        if (skillPlayer.getRented().containsKey(args[2]))
            skillPlayer.revokeRented(getPlugin().getSkill(args[2]));
        else
            skillPlayer.revokeSkill(getPlugin().getSkill(args[2]));
        
        getPlugin().getServer().getPluginManager().callEvent(new BuySkillsRevokeEvent(getPlugin().getSkill(args[2]), skillPlayer));
        
        if (getPlugin().getServer().getPlayerExact(args[1]) != null)
            getPlugin().getServer().getPlayerExact(args[1]).sendMessage(SkillLanguage.REVOKE_NOTIFY.getString().replace("%skill%", args[2]));
        
        return SkillLanguage.REVOKE_SUCCESS.getString();
    }
    
    @Override
    public List<String> onTabComplete(String[] args)
    {
        switch (args.length)
        {
            case 3:
                return SkillUtils.getMatches(args[2], getPlugin().getSkillMap().keySet());
            default:
                return null;
        }
    }
    
    @Override
    public void help(CommandSender sender)
    {
        sender.sendMessage(SkillLanguage.HELP_COMMAND_REVOKE.getString());
    }
}
