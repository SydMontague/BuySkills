package com.syd.expskills;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class ServerEntityListener implements Listener
{
    ExpSkills plugin;
    HashMap<Short, ExpSave> expsave = new HashMap<Short, ExpSave>();
    short bottleNumber = 1;
    
    public ServerEntityListener(ExpSkills instance)
    {
        plugin = instance;
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event)
    {
        // modify ORB drop on player death
        if (event.getEntity() instanceof Player && ExpSkills.config.getBoolean("general.change_expdrop", false))
        {
            PlayerDeathEvent death = (PlayerDeathEvent) event;
            Player p = (Player) event.getEntity();
            
            // int xp = p.getExp();
            int totalxp = p.getTotalExperience();
            int newxp = funcs.getXpatLevel(funcs.getLevel(p));
            int xp = totalxp - newxp;
            
            YamlConfiguration pconfig = FileManager.loadPF(p);
            
            // Workaround for Bukkit Bug #1595
            if (newxp < pconfig.getInt("experience", 0))
                newxp = pconfig.getInt("experience", 0);
            
            pconfig.set("experience", newxp);
            FileManager.savePF(p, pconfig);
            
            int exp = ExpSkills.getExp(p.getLevel() + p.getExp());
            
            // level = 1.75 * (level * level) + 4.5 * level;
            
            // default usage of EXP Drop - add dynamic method with config usage
            p.getWorld().dropItem(p.getLocation(), new ItemStack(384, 1, bottleNumber));
            expsave.put(bottleNumber, new ExpSave(xp, exp));
            
            bottleNumber++;
            if (bottleNumber > 32760)
                bottleNumber = 1;
            
            // item.setMetadata("expskills.exp", new FixedMetadataValue(plugin,
            // xp));
            // item.setMetadata("expskills.level", new
            // FixedMetadataValue(plugin, exp));
            
            event.setDroppedExp(0);
            death.setNewLevel(0);
        }
        else
            return;
    }
    
    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event)
    {
        if (event.getItem().getItemStack().getType() == Material.EXP_BOTTLE)
        {
            Item item = event.getItem();
            short save = item.getItemStack().getDurability();
            if (expsave.get(save) != null)
            {
                
                Player player = event.getPlayer();
                
                int exp = ExpSkills.getExp(player.getLevel() + player.getExp());
                
                int copy2 = exp;
                exp += expsave.get(save).exp;
                int copy1 = exp;
                
                int i = (int) ExpSkills.getLevel(exp);
                
                if (i > 200)
                {
                    ExpSkills.log.info("Enchantmentlevel > 200, please check for bug by player: " + event.getPlayer().getName());
                    ExpSkills.log.info(expsave.get(save).exp + " " + copy2 + " " + copy1);
                }
                player.setTotalExperience(player.getTotalExperience() + expsave.get(save).xp);
                player.setLevel(i);
                
                expsave.remove(save);
                event.getItem().remove();
                event.setCancelled(true);
            }
        }
    }
    
}

class ExpSave
{
    int xp;
    int exp;
    
    public ExpSave(int xp, int exp)
    {
        this.xp = xp;
        this.exp = exp;
    }
    
}
