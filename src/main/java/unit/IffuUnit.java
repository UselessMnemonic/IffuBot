package unit;

import com.jagrosh.jdautilities.command.CommandEvent;
import item.IffuInventory;
import item.IffuInventoryRegistry;
import item.IffuItem;
import skill.IffuSkill;
import skill.IffuSkillManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Random;

import javax.json.*;

/*
 * Defines a common user.
 */

public class IffuUnit
{
	//Units can have a name, a class, and stats
	private String userName;
	private IffuClass userClass;
	private String userID;
	
	private IffuStat userBaseStats;
	private IffuStat userStatModifiers;
	private IffuStat userGrowthModifiers;

	private ArrayList<String> equippedSkills;
	
	private int currentHP;
	
	private int internalLevel;
	private int internalExp;
	
	private int level;
	private int exp;
	
	//constructor
	public IffuUnit(String unitName, String unitID, IffuClass unitClass)
	{
		setUserClass(unitClass);
		setUserName(unitName);
		setUserID(unitID);
		
		setUserBaseStats(unitClass.getBaseStats());
		setUserStatModifiers(new IffuStat());
		setUserGrowthModifiers(new IffuStat());

		setEquippedSkills(new ArrayList<String>());
		
		setCurrentHP(getUserBaseStats().getHP());
		
		setInternalLevel(1);
		setInternalExp(0);
		
		setLevel(1);
		setExp(0);

		learnSkills();
	}
	
	private IffuUnit()
	{
		
	}
	
	public IffuUnit(IffuUnit otherUnit)
	{
		userName = otherUnit.userName;
		userClass = otherUnit.userClass;
		userID = otherUnit.userID;
		
		userBaseStats = new IffuStat(otherUnit.userBaseStats.getRaw());
		userStatModifiers = new IffuStat(otherUnit.userStatModifiers.getRaw());
		userGrowthModifiers = new IffuStat(otherUnit.userGrowthModifiers.getRaw());

		equippedSkills = new ArrayList<String>();
		equippedSkills.addAll(otherUnit.equippedSkills);
		
		currentHP = otherUnit.currentHP;
		
		internalLevel = otherUnit.internalLevel;
		internalExp = otherUnit.internalExp;
		level = otherUnit.level;
		exp = otherUnit.exp;
	}

	public static IffuUnit fromJSON(File JSONFile)
	{
		IffuUnit nextUnit = null;
		
		try
		(
				FileReader frdr = new FileReader(JSONFile);
				BufferedReader brdr = new BufferedReader(frdr);
				JsonReader jrdr = Json.createReader(brdr);
		)
		{
			Set<Entry<String, JsonValue>> set = jrdr.readObject().entrySet();
			
			for(Entry<String, JsonValue> e : set)
			{
				nextUnit = IffuUnit.fromJSON(e.getValue().asJsonObject());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return nextUnit;
	}
	
	public static IffuUnit fromJSON(JsonObject jso)
	{
		Object[] nextArray;
		byte[] nextStats;
		IffuUnit nextUnit = new IffuUnit();

		//get name, id, exp, and level
		nextUnit.setUserClass( IffuClassRegistry.getClass(jso.getString("userClass")) );
		nextUnit.setExp(jso.getInt("exp"));
		nextUnit.setInternalExp(jso.getInt("internalExp"));
		nextUnit.setLevel(jso.getInt("level"));
		nextUnit.setInternalLevel(jso.getInt("internalLevel"));
		nextUnit.setUserName(jso.getString("userName"));
		nextUnit.setUserID(jso.getString("userID"));
		nextUnit.setCurrentHP(jso.getInt("currentHP"));
		
		//get base user stats
		nextArray = jso.getJsonArray("userBaseStats").toArray();
		nextStats = new byte[nextArray.length];
		for(int i = 0; i <= nextArray.length - 1; i++)
			nextStats[i] = (byte) ((JsonNumber)nextArray[i]).intValue();
		nextUnit.setUserBaseStats(new IffuStat(nextStats));
		
		//get stat modifiers
		nextArray = jso.getJsonArray("userStatModifiers").toArray();
		nextStats = new byte[nextArray.length];
		for(int i = 0; i <= nextArray.length - 1; i++)
			nextStats[i] = (byte) ((JsonNumber)nextArray[i]).intValue();
		nextUnit.setUserStatModifiers(new IffuStat(nextStats));
		
		//get growth modifiers
		nextArray = jso.getJsonArray("userGrowthModifiers").toArray();
		nextStats = new byte[nextArray.length];
		for(int i = 0; i <= nextArray.length - 1; i++)
			nextStats[i] = (byte) ((JsonNumber)nextArray[i]).intValue();
		nextUnit.setUserGrowthModifiers(new IffuStat(nextStats));


        nextUnit.equippedSkills = new ArrayList<String>();

		try {
            //get equipped skills
            nextArray = jso.getJsonArray("equippedSkills").toArray();
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextUnit.equippedSkills.add(((JsonString) (nextArray[i])).getString());
        }catch(Exception e)
        {
        }
		
		return nextUnit;
	}
	
	public void setCurrentHP(int value)
	{
		currentHP = value;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public IffuClass getUserClass()
	{
		return userClass;
	}

	public void setUserClass(IffuClass userClass)
	{
		this.userClass = userClass;
	}

	public String getUserID()
	{
		return userID;
	}

	public void setUserID(String userID)
	{
		this.userID = userID;
	}

	public IffuStat getUserBaseStats()
	{
		return userBaseStats;
	}
	
	public void setUserBaseStats(IffuStat stats)
	{
		userBaseStats = stats;
	}
	
	public IffuStat getUserStatModifiers()
	{
		return userStatModifiers;
	}
	
	public void setUserStatModifiers(IffuStat modifiers)
	{
		userStatModifiers = modifiers;
	}
	
	public IffuStat getVisibleUserStats()
	{
		return IffuStat.add(getUserBaseStats(), getUserStatModifiers());
	}

    public IffuStat getUserGrowthModifiers()
	{
		return userGrowthModifiers;
	}
	
	public void setUserGrowthModifiers(IffuStat modifiers)
	{
		userGrowthModifiers = modifiers;
	}
	
	public IffuStat getVisibleUserGrowthRates()
	{
		return IffuStat.add(getUserGrowthModifiers(), getUserClass().getGrowthRates());
	}
	
	public int getInternalLevel()
	{
		return internalLevel;
	}
	
	public void setInternalLevel(int internalLevel)
	{
		this.internalLevel = internalLevel;
	}

	public int getInternalExp()
	{
		return internalExp;
	}

	public void setInternalExp(int internalExp)
	{
		this.internalExp = internalExp;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
		if(level > getUserClass().getMaxLvl())
			this.level = getUserClass().getMaxLvl();
		if(level < 1)
			this.level = 1;
	}

	public int getExp()
	{
		return exp;
	}

	public void setExp(int exp)
	{
		this.exp = exp;
		if(exp >= 100)
			this.exp -= 100;
		if(exp < 0)
			this.exp = 0;
	}

	public ArrayList<String> getEquippedSkills()
    {
        return equippedSkills;
    }

    public void setEquippedSkills(ArrayList<String> list){equippedSkills = list;}

	public JsonObject toJSONObj()
	{
		JsonArrayBuilder baseStats = Json.createArrayBuilder();
		JsonArrayBuilder statModifiers = Json.createArrayBuilder();
		JsonArrayBuilder growthModifiers = Json.createArrayBuilder();
        JsonArrayBuilder equippedSkills = Json.createArrayBuilder();
		
		for(byte b : getUserBaseStats().getRaw())
			baseStats.add(b);
		for(byte b : getUserStatModifiers().getRaw())
			statModifiers.add(b);
		for(byte b : getUserGrowthModifiers().getRaw())
			growthModifiers.add(b);
		for(String s : this.equippedSkills)
		    equippedSkills.add(s);
		
		JsonObject ret = Json.createObjectBuilder()
				.add("userName", getUserName())
				.add("userClass", getUserClass().getClassName())
				.add("userID", getUserID())
				.add("currentHP", getCurrentHP())
				.add("userBaseStats", baseStats.build())
				.add("userStatModifiers", statModifiers.build())
				.add("userGrowthModifiers", growthModifiers.build())
				.add("internalLevel", getInternalLevel())
				.add("internalExp", getInternalExp())
				.add("level", getLevel())
				.add("exp", getExp())
                .add("equippedSkills", equippedSkills.build())
				.build();
		
		return ret;
	}

	public int getCurrentHP()
	{
		return currentHP;
	}

	public boolean isMaxLvl()
	{
		return getUserClass().getMaxLvl() <= getLevel();
	}

	public ArrayList<IffuClass> getPromotions()
	{
		String[] promotions = getUserClass().getBasePromotions();
		ArrayList<IffuClass> ret = new ArrayList<IffuClass>();
		IffuClass nextClass;
		
		for(String s : promotions)
		{
			nextClass = IffuClassRegistry.getClass(s);
			if(canPromote(nextClass))
				ret.add(nextClass);
		}
		
		return ret;
	}

	public boolean canPromote(IffuClass ic)
	{
		return (ic != null) && (getLevel() >= ic.getBaseLevel());
	}

	public void promoteUnit(IffuClass ic, boolean willSave)
	{
		setUserClass(ic);
		for(int i = 0; i <= 8; i++)
		{
			if(getUserBaseStats().getRaw()[i] < ic.getBaseStats().getRaw()[i])
				getUserBaseStats().getRaw()[i] = ic.getBaseStats().getRaw()[i];
			else if(getUserBaseStats().getRaw()[i] > ic.getMaxStats().getRaw()[i])
				getUserBaseStats().getRaw()[i] = ic.getMaxStats().getRaw()[i];
		}

		if(willSave)
			learnSkills();
		
		setLevel( 1 );
		setExp( 0 );
		validateHP();
	}

	public boolean addExpAndLvlUp(int expToAdd)
	{
		setInternalExp( getInternalExp() + expToAdd);
		int nextExp = getExp()+expToAdd;
		setExp( nextExp );
		
		if(nextExp >= 100)
		{
			setLevel( getLevel() + 1) ;
			setInternalLevel( getInternalLevel() + 1 );
			
			for(int i = 0; i <= 8; i++)
				if( (getUserBaseStats().getRaw()[i] < getUserClass().getMaxStats().getRaw()[i]) &&
                        (new Random(System.nanoTime()).nextInt(100) + 1) <= (int)(getVisibleUserGrowthRates().getRaw()[i]) )
					getUserBaseStats().getRaw()[i] += 1;

            learnSkills();
			
			return true;
		}
		
		return false;
	}

    public void raiseHP(int amt)
    {
        currentHP += amt;
        validateHP();
    }

    public void validateHP()
    {
        int maxHP = getVisibleUserStats().getHP();
        if(currentHP > maxHP)
            currentHP = maxHP;
    }

    public void addStatModifiers(IffuStat modifiers)
    {
        IffuStat newStatModifiers = IffuStat.add(getUserStatModifiers(), modifiers);
        setUserStatModifiers(newStatModifiers);
        validateHP();
    }

    public void removeStatModifiers(IffuStat modifiers)
    {
        IffuStat newStatModifiers = IffuStat.sub(getUserStatModifiers(), modifiers);
        setUserStatModifiers(newStatModifiers);
        validateHP();
    }

    public void addGrowthModifiers(IffuStat modifiers)
    {
        IffuStat newGrowthModifiers = IffuStat.add(getUserGrowthModifiers(), modifiers);
        setUserGrowthModifiers(newGrowthModifiers);
        validateHP();
    }

    public void removeGrowthModifiers(IffuStat modifiers)
    {
        IffuStat newGrowthModifiers = IffuStat.sub(getUserGrowthModifiers(), modifiers);
        setUserGrowthModifiers(newGrowthModifiers);
        validateHP();
    }

    public void raiseBaseStats(IffuStat modifiers)
    {
        IffuStat newBaseStats = IffuStat.add(getUserBaseStats(), modifiers);
        setUserBaseStats(newBaseStats);
        validateHP();
    }

    public void useItem(int place)
    {
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(userID);
        IffuItem myItem = myInventory.grabItem(place);

        if(myItem == null)
            return;

        if(myItem.isConsumable())
        {
            raiseBaseStats(myItem.getBonuses());
            raiseHP(myItem.getHealBonus());

            myItem.use();

            if(myItem.getUses() == 0)
                removeItem(place);
        }
    }

    public void equipItem(int place)
    {
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(userID);
        IffuItem myItem = myInventory.grabItem(place);

        if(myInventory.isEquipped())
            unequipItem();

        if(myItem != null)
        {
            addStatModifiers(myItem.getBonuses());
            myInventory.bringToFront(place);
            myInventory.setEquipped(true);
        }
    }

    public void removeItem(int place)
    {
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(userID);

        if(place == 1 && myInventory.isEquipped())
            unequipItem();

        myInventory.removeItem(place);
    }

    public void unequipItem()
    {
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(userID);
        IffuItem myItem = myInventory.getEquippedItem();

        if(myItem != null)
        {
            myInventory.setEquipped(false);
            removeStatModifiers(myItem.getBonuses());
        }
    }

    public boolean hasEquippedSkill(String skillName)
    {
        return equippedSkills.contains(skillName);
    }

    public boolean canEquipSkill()
    {
        return equippedSkills.size() < 4;
    }

    public void equipSkill(String skillName)
	{
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(userID);
        if(!canEquipSkill())
            return;
        if(myInventory.hasSkill(skillName))
        {
            IffuSkill mySkill = IffuSkillManager.getInstance(skillName);

            if(mySkill != null && !equippedSkills.contains(skillName))
            {
                equippedSkills.add(skillName);
                addStatModifiers(mySkill.getStatModifiers());
                addGrowthModifiers(mySkill.getGrowthModifiers());
            }
        }
	}

	public void unequipSkill(String skillName)
	{
	    if(hasEquippedSkill(skillName))
        {
            equippedSkills.remove(skillName);
            IffuSkill mySkill = IffuSkillManager.getInstance(skillName);

            if(mySkill != null)
            {
                removeStatModifiers(mySkill.getStatModifiers());
                removeGrowthModifiers(mySkill.getGrowthModifiers());
            }
        }
	}

	public void learnSkills()
    {
        String[] possibleSkillsLearned = getUserClass().getBaseSkills();
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(getUserID());
        IffuSkill nextSkill;

        for(String s : possibleSkillsLearned)
        {
           nextSkill = IffuSkillManager.getInstance(s);

           if(canLearn(nextSkill))
           {
               myInventory.addSkill(s);

               if(canEquipSkill())
                   equipSkill(s);
           }
        }
    }

    public boolean canLearn(IffuSkill skill)
    {
        IffuInventory myInventory = IffuInventoryRegistry.getInventory(getUserID());
        return (getLevel() >= skill.getBaseLevel()) && (!myInventory.hasSkill(skill.getSkillName()));
    }

    public boolean canLearn(String skillName)
    {
        return canLearn(IffuSkillManager.getInstance(skillName));
    }
}