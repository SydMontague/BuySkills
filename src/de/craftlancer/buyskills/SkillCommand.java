package de.craftlancer.buyskills;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SkillCommand implements CommandExecutor
{
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        Commands.valueOf(args[0]);
        
        /*
         * Commands:
         * help - page | command            //help pages
         * list - page category avaible     //list skills
         * info - skill                     //info about skills
         * buy - skill                      //buy skill as player
         * rent - skill time                //rent skill as player for time
         * current - player                 //show player's current skills
         * rented - player                  //show player's current rented skills
         * grant - skill player             //grant skill to player as admin
         * revoke - skill player            //revoke skill from player as admin
         * reset - player                   //reset player's skill as admin
         * new - skill                      //create new skill with "skill" as key
         * remove - skill                   //remove skill with key "skill"
         * edit - skill key newvalue        //set skill value "key" to newvalue
         * reload -                         //reload the config
         * reloadPerm - player              //recalculate the player's permissions
         */
        
        // TODO Auto-generated method stub
        return false;
    }
    

}


enum Commands
{    
    HELP("help"),
    LIST("list"),
    INFO("info"),
    BUY("buy"),
    RENT("rent"),
    CURRENT("current"),
    RENTED("rented"),
    GRANT("grant"),
    REVOKE("revoke"),
    RESET("reset"),
    NEW("new"),
    REMOVE("remove"),
    EDIT("edit"),
    RELOAD("reload"),
    RELOADPERM("reloadperm");
    
    String command;
    
    Commands(String cmd)
    {
        command = cmd;
    }
}