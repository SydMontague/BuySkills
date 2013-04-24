package de.craftlancer.buyskills;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        this.setBonusCap(bonuscap);
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
    
    public HashMap<String, Long> getRented()
    {
        return rented;
    }
    
    public int getBonusCap()
    {
        return bonuscap;
    }
    
    public void setBonusCap(int bonuscap)
    {
        this.bonuscap = bonuscap;
    }
    
    public String getName()
    {
        return name;
    }
    
    
    
    public void removeRentedSkill(String s)
    {
        rented.remove(s);
    }
    
    public void removeRentedSkill(Skill s)
    {
        removeRentedSkill(s.getName());
    }
    
    public void addSkill(Skill s)
    {
        addSkill(s.getName());
    }
    
    public void addSkill(String s)
    {
        skills.add(s);
    }
    
    public void removeSkill(String s)
    {
        skills.remove(s);
    }
    
    public void removeSkill(Skill s)
    {
        removeSkill(s.getName());
    }

    public void addRented(String s, long l)
    {
        rented.put(s, l);        
    }

    public List<String> getBoughtSkills()
    {
        return skills;
    }
}
