package de.craftlancer.buyskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO merge SkillPlayerManager with SkillPlayer as much as possible
/**
 * Represents a Player
 */
public class SkillPlayer
{
    private String name;
    private List<String> skills = new ArrayList<String>();
    private HashMap<String, Long> rented = new HashMap<String, Long>();
    private int bonuscap = 0;
    
    public SkillPlayer(String name, List<String> skills, HashMap<String, Long> rented, int bonuscap)
    {
        this.name = name;
        this.skills = skills;
        this.rented = rented;
        setBonusCap(bonuscap);
    }
    
    /**
     * Get all skills (rented and bought) of this player
     * 
     * @return all the player's owned skills
     */
    public List<String> getSkills()
    {
        List<String> fin = new ArrayList<String>();
        fin.addAll(skills);
        fin.addAll(rented.keySet());
        
        return fin;
    }
    
    /**
     * Get all rented skills of this player
     * 
     * @return all rented skill of this player
     */
    public HashMap<String, Long> getRented()
    {
        return rented;
    }
    
    /**
     * Get all bought skills of this player
     * 
     * @return all bought skills of this player
     */
    public List<String> getBoughtSkills()
    {
        return skills;
    }
    
    /**
     * Get the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @return the bonuscap
     */
    public int getBonusCap()
    {
        return bonuscap;
    }
    
    /**
     * Set the amount of extra skills this player can have
     * Negative values allow the player to have less skills
     * 
     * @param bonuscap the amount of bonusskills
     */
    public void setBonusCap(int bonuscap)
    {
        this.bonuscap = bonuscap;
    }
    
    /**
     * Get the name of the player
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }
    
    protected void removeRentedSkill(String s)
    {
        rented.remove(s);
    }
    
    protected void removeRentedSkill(Skill s)
    {
        removeRentedSkill(s.getName());
    }
    
    protected void addSkill(Skill s)
    {
        addSkill(s.getName());
    }
    
    protected void addSkill(String s)
    {
        skills.add(s);
    }
    
    protected void removeSkill(String s)
    {
        skills.remove(s);
    }
    
    protected void removeSkill(Skill s)
    {
        removeSkill(s.getName());
    }
    
    protected void addRented(String s, long l)
    {
        rented.put(s, l);
    }
}
