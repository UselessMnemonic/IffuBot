package item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.*;

public class IffuInventory
{
	ArrayList<IffuItem> inventory;
	ArrayList<String> useableSkills;
	private int balance;
	private String userID;
	private boolean itemEquipped;

	public IffuInventory(String userID)
	{
		this.userID = userID;
		balance = 0;
		itemEquipped = false;
		inventory = new ArrayList<IffuItem>();
		useableSkills = new ArrayList<String>();
	}
	
	private IffuInventory()
	{
		inventory = new ArrayList<IffuItem>();
		useableSkills = new ArrayList<String>();
	}

	public static IffuInventory fromJSON(File JSONFile)
	{
		IffuInventory nextInventory = null;
		
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
				nextInventory = IffuInventory.fromJSON(e.getValue().asJsonObject());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return nextInventory;
	}

	public static IffuInventory fromJSON(JsonObject obj)
	{
		IffuInventory nextInventory = new IffuInventory();
		nextInventory.setUser(obj.getString("userID"));
		
		Object[] nextArray = obj.getJsonArray("items").toArray();
		for(Object o : nextArray)
		{
			nextInventory.addItem(IffuItem.fromJSON((JsonObject)o));
		}

        //get bonuses
        nextArray = obj.getJsonArray("useableSkills").toArray();
        for(int i = 0; i <= nextArray.length - 1; i++)
            nextInventory.useableSkills.add(((JsonString)nextArray[i]).getString());

		nextInventory.balance = obj.getInt("balance");

		nextInventory.itemEquipped = obj.getBoolean("equipped");

		return nextInventory;
	}

	public JsonObject toJSONObj()
	{
		JsonArrayBuilder itemArray = Json.createArrayBuilder();
		JsonArrayBuilder skillArray = Json.createArrayBuilder();

		for(int i = 0; i <= inventory.size()-1 && i <= 4 ; i++)
		{
			itemArray.add(inventory.get(i).toJSONObj());
		}

        for(int i = 0; i <= useableSkills.size()-1; i++)
        {
            skillArray.add(useableSkills.get(i));
        }
		
		return Json.createObjectBuilder()
				.add("userID", getUser())
				.add("items", itemArray)
				.add("balance", balance)
                .add("equipped", itemEquipped)
                .add("useableSkills", skillArray)
				.build();
	}

	public void clear()
	{
		inventory.clear();
		setEquipped(false);
	}

	public boolean addItem(IffuItem nextItem)
	{
		if(inventory.size() == 5)
			return false;
		
		inventory.add(nextItem);

		return true;
	}

	public boolean addSkill(String nextSkill)
    {
        if(hasSkill(nextSkill))
            return false;

        useableSkills.add(nextSkill);
        return true;
    }

    public ArrayList<String> getUseableSkills()
    {
        return useableSkills;
    }

	public String getUser()
	{
		return userID;
	}
	
	public void setUser(String userID)
	{
		this.userID = userID;
	}

	public ArrayList<IffuItem> getItemList()
	{
		return inventory;
	}

    public IffuItem removeItem(int place)
    {
        IffuItem item;

        if(place <= inventory.size())
        {
            return inventory.remove(place - 1);
        }

        return null;
    }

    public IffuItem grabItem(int place)
    {
        if(place <= inventory.size())
        {
            return inventory.get(place - 1);
        }

        return null;
    }

    public boolean hasSkill(String skillName)
    {
        return useableSkills.contains(skillName);
    }

	public int getBalance()
	{
		return balance;
	}

	public boolean spend(int amt)
	{
		if(balance < amt)
			return false;

		balance -= amt;
		return true;
	}

    public int numItems()
    {
       return getItemList().size();
    }

    public int numSkills()
    {
        return getUseableSkills().size();
    }

    public void setEquipped(boolean equip)
    {
        if(equip)
			itemEquipped = (numItems() > 0);
        else
			itemEquipped = false;
    }

    public IffuItem getEquippedItem()
    {
        if(itemEquipped)
            return grabItem(1);
        else
            return null;
    }

    public boolean isEquipped()
    {
        return itemEquipped;
    }

    public void bringToFront(int place)
    {
        if(place <= inventory.size())
        {
            IffuItem nextItem = inventory.remove(place-1);
            inventory.add(0, nextItem);
        }
    }
}
