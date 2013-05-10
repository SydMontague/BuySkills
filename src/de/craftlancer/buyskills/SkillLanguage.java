package de.craftlancer.buyskills;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Handles and stores all Strings used in this plugin
 */
public class SkillLanguage
{
    public static String COMMAND_PERMISSION;
    public static String COMMAND_PLAYERONLY;
    public static String COMMAND_SKILL_NOT_EXIST;
    public static String COMMAND_PLAYER_NOT_EXIST;
    public static String COMMAND_ARGUMENTS;
    
    public static String HELP_COMMAND_HELP;
    public static String HELP_COMMAND_HELP_ADMIN;
    public static String HELP_COMMAND_BUY;
    public static String HELP_COMMAND_CURRENT;
    public static String HELP_COMMAND_GRANT;
    public static String HELP_COMMAND_INFO;
    public static String HELP_COMMAND_LIST;
    public static String HELP_COMMAND_RECALCULATE;
    public static String HELP_COMMAND_RELOAD;
    public static String HELP_COMMAND_RENT;
    public static String HELP_COMMAND_RENTED;
    public static String HELP_COMMAND_RESET;
    public static String HELP_COMMAND_REVOKE;
    
    public static String BUYRENT_ALREADY_OWN;
    public static String BUYRENT_NOT_PERMISSION;
    public static String BUYRENT_NOT_GROUP;
    public static String BUYRENT_NOT_SKILLTREE;
    public static String BUYRENT_NOT_AFFORD;
    public static String BUYRENT_SKILLCAP_REACHED;
    public static String BUYRENT_WRONG_WORLD;
    public static String BUYRENT_NOT_CURRENCYS;    
    public static String BUYRENT_CANCELLED;
    
    public static String BUY_SUCCESS;
    public static String BUY_NOT_BUYABLE;
    
    public static String RENT_SUCCESS;
    public static String RENT_NOT_RENTABLE;
    public static String RENT_EXPIRED;
    
    public static String LIST_DEFAULT_STRING;
    public static String INFO_DEFAULT_STRING;
    public static String RENTED_DEFAULT_STRING;
    public static String CURRENT_DEFAULT_STRING;
    
    public static String RESET_SUCCESS;
    public static String RESET_NOTIFY;
    
    public static String RECALC_SUCCESS;
    public static String RECALC_NOTIFY;
    
    public static String RELOAD_SUCCESS;
    
    public static String REVOKE_SUCCESS;
    public static String REVOKE_NOTIFY;
    public static String REVOKE_NOT_OWN;
    
    public static String GRANT_ALREADY_OWN;
    public static String GRANT_SUCCESS;
    public static String GRANT_NOTIFY;
    
    protected static void loadStrings(FileConfiguration config)
    {
        COMMAND_PERMISSION = config.getString("string.COMMAND_PERMISSION");
        COMMAND_PLAYERONLY = config.getString("string.COMMAND_PLAYERONLY");
        COMMAND_SKILL_NOT_EXIST = config.getString("string.COMMAND_SKILL_NOT_EXIST");
        COMMAND_PLAYER_NOT_EXIST = config.getString("string.COMMAND_PLAYER_NOT_EXIST");
        COMMAND_ARGUMENTS = config.getString("string.COMMAND_ARGUMENTS");
        HELP_COMMAND_HELP = config.getString("string.HELP_COMMAND_HELP");
        HELP_COMMAND_HELP_ADMIN = config.getString("string.HELP_COMMAND_HELP_ADMIN");
        HELP_COMMAND_BUY = config.getString("string.HELP_COMMAND_BUY");
        HELP_COMMAND_CURRENT = config.getString("string.HELP_COMMAND_CURRENT");
        HELP_COMMAND_GRANT = config.getString("string.HELP_COMMAND_GRANT");
        HELP_COMMAND_INFO = config.getString("string.HELP_COMMAND_INFO");
        HELP_COMMAND_LIST = config.getString("string.HELP_COMMAND_LIST");
        HELP_COMMAND_RECALCULATE = config.getString("string.HELP_COMMAND_RECALCULATE");
        HELP_COMMAND_RELOAD = config.getString("string.HELP_COMMAND_RELOAD");
        HELP_COMMAND_RENT = config.getString("string.HELP_COMMAND_RENT");
        HELP_COMMAND_RENTED = config.getString("string.HELP_COMMAND_RENTED");
        HELP_COMMAND_RESET = config.getString("string.HELP_COMMAND_RESET");
        HELP_COMMAND_REVOKE = config.getString("string.HELP_COMMAND_REVOKE");
        LIST_DEFAULT_STRING = config.getString("string.LIST_DEFAULT_STRING");
        INFO_DEFAULT_STRING = config.getString("string.INFO_DEFAULT_STRING");
        RENTED_DEFAULT_STRING = config.getString("string.RENTED_DEFAULT_STRING");
        CURRENT_DEFAULT_STRING = config.getString("string.CURRENT_DEFAULT_STRING");
        RENT_EXPIRED = config.getString("string.RENT_EXPIRED");
        REVOKE_NOT_OWN = config.getString("string.REVOKE_NOT_OWN");
        REVOKE_SUCCESS = config.getString("string.REVOKE_SUCCESS");
        BUY_NOT_BUYABLE = config.getString("string.BUY_NOT_BUYABLE");
        BUYRENT_SKILLCAP_REACHED = config.getString("string.BUYRENT_SKILLCAP_REACHED");
        BUYRENT_WRONG_WORLD = config.getString("string.BUYRENT_WRONG_WORLD");
        BUYRENT_ALREADY_OWN = config.getString("string.BUYRENT_ALREADY_OWN");
        BUYRENT_NOT_PERMISSION = config.getString("string.BUYRENT_NOT_PERMISSION");
        BUYRENT_NOT_GROUP = config.getString("string.BUYRENT_NOT_GROUP");
        BUYRENT_NOT_SKILLTREE = config.getString("string.BUYRENT_NOT_SKILLTREE");
        BUYRENT_NOT_CURRENCYS = config.getString("string.BUYRENT_NOT_CURRENCYS");
        BUYRENT_NOT_AFFORD = config.getString("string.BUYRENT_NOT_AFFORD");
        BUYRENT_CANCELLED = config.getString("string.BUYRENT_CANCELLED");
        BUY_SUCCESS = config.getString("string.BUY_SUCCESS");
        RESET_SUCCESS = config.getString("string.RESET_SUCCESS");
        RESET_NOTIFY = config.getString("string.RESET_NOTIFY");
        RECALC_SUCCESS = config.getString("string.RECALC_SUCCESS");
        RECALC_NOTIFY = config.getString("string.RECALC_NOTIFY");
        RELOAD_SUCCESS = config.getString("string.RELOAD_SUCCESS");
        REVOKE_NOTIFY = config.getString("string.REVOKE_NOTIFY");
        RENT_NOT_RENTABLE = config.getString("string.RENT_NOT_RENTABLE");
        RENT_SUCCESS = config.getString("string.RENT_SUCCESS");
        GRANT_ALREADY_OWN = config.getString("string.GRANT_ALREADY_OWN");
        GRANT_SUCCESS = config.getString("string.GRANT_SUCCESS");
        GRANT_NOTIFY = config.getString("string.GRANT_NOTIFY");
    }
    
}
