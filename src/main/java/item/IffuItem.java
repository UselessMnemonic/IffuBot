package item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import unit.IffuStat;

public class IffuItem
{
	private String itemName;
	private String customName;
	private IffuItemType itemType;
	
	private IffuStat bonuses;
	private int healBonus;
	private boolean consumable;
	private int uses;
	
	private String owner;
	private boolean forged;
	
	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	public IffuItemType getItemType()
	{
		return itemType;
	}

	public void setItemType(IffuItemType itemType)
	{
		this.itemType = itemType;
	}

	public IffuStat getBonuses()
	{
		return bonuses;
	}

	public void setBonuses(IffuStat bonuses)
	{
		this.bonuses = bonuses;
	}

	public void setHealBonus(int healBonus)
	{
		this.healBonus = healBonus;
	}

	public int getHealBonus()
	{
		return healBonus;
	}

	public boolean isConsumable()
	{
		return consumable;
	}

	public void setConsumable(boolean consumable)
	{
		this.consumable = consumable;
	}

	public int getUses()
	{
		return uses;
	}

	public void setUses(int uses)
	{
		if(uses < 0)
			this.uses = 0;
		else
			this.uses = uses;
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public boolean isForged()
	{
		return forged;
	}

	public void setForged(boolean forged)
	{
		this.forged = forged;
	}

	private IffuItem()
	{
		
	}
	
	public IffuItem(IffuItem itemToClone)
	{
		setItemName(itemToClone.getItemName());
		setItemType(itemToClone.getItemType());
		setBonuses(itemToClone.getBonuses());
		setHealBonus(itemToClone.getHealBonus());
		setConsumable(itemToClone.isConsumable());
		setUses(itemToClone.getUses());
		setCustomName(itemToClone.getCustomName());
		setOwner(itemToClone.getOwner());
		setForged(itemToClone.isForged());
	}
	
	public static IffuItem fromJSON(JsonObject jso)
	{
		Object[] nextArray;
		byte[] nextStats;
		IffuItem nextItem = new IffuItem();
		
		//get name, uses, custom, and owner
		nextItem.setItemName(jso.getString("itemName"));
		nextItem.setConsumable(jso.getBoolean("isConsumable"));
		nextItem.setCustomName(jso.getString("customName"));
		nextItem.setUses(jso.getInt("uses"));
		nextItem.setForged(jso.getBoolean("isForged"));
		nextItem.setOwner(jso.getString("owner"));
		
		//get bonuses
		nextArray = jso.getJsonArray("itemStatBonuses").toArray();
		nextStats = new byte[nextArray.length];
		for(int i = 0; i <= nextArray.length - 1; i++)
			nextStats[i] = (byte) ((JsonNumber)nextArray[i]).intValue();
		nextItem.setBonuses(new IffuStat(nextStats));

		//get heal bonus
		nextItem.setHealBonus(jso.getInt("healBonus"));
		
		//get type
		try
		{
			IffuItemType type = IffuItemType.valueOf(jso.getString("itemType"));
			nextItem.setItemType(type);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			nextItem.setItemType(IffuItemType.Unknown);
		}
		
		return nextItem;
	}

	public void forge(String newName, IffuStat bonuses)
	{
		setCustomName(newName);
		setForged(true);
		setBonuses(bonuses);
	}
	
	public JsonObject toJSONObj()
	{
		JsonArrayBuilder itemBonuses = Json.createArrayBuilder();
		
		for(byte b : getBonuses().getRaw())
			itemBonuses.add(b);
		
		JsonObject ret = Json.createObjectBuilder()
				.add("itemName", getItemName())
				.add("customName", getCustomName())
				.add("isConsumable", isConsumable())
				.add("uses", getUses())
				.add("isForged", isForged())
				.add("owner", getOwner())
				.add("itemStatBonuses", itemBonuses.build())
				.add("healBonus", getHealBonus())
				.add("itemType", getItemType().toString())
				.build();
		
		return ret;
	}	
	
	public static ArrayList<IffuItem> fromJSON(File JSONFile)
	{
		ArrayList<IffuItem> items = new ArrayList<IffuItem>();
		IffuItem nextItem;
		
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
				nextItem = IffuItem.fromJSON(e.getValue().asJsonObject());
				items.add(nextItem);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return items;
	}

	public void use()
    {
        uses -= 1;
    }
}


