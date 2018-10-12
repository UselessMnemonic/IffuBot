package item;

import skill.IffuSkill;
import unit.IffuUnitRegistry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

public class IffuInventoryRegistry
{
	private static HashMap<String, IffuInventory> inventories;
	private static File store;
	
	public static void loadInventories(String filename)
	{
		File f = new File(filename);
		loadInventories(f);
	}

    public static void loadInventories(File loc)
    {
        inventories = new HashMap<String, IffuInventory>();
        store = loc;

        if(!store.isDirectory())
        {
            System.out.println("Invalid inventory store path, defaulting to sub-directory \"inventories\"");
            store = new File("inventories");
        }

        File[] list = store.listFiles();

        IffuInventory nextInventory;

        for(File nextFile : list)
        {
            nextInventory = IffuInventory.fromJSON(nextFile);
            inventories.put(nextInventory.getUser(), nextInventory);
        }
    }

	public static void saveInventories()
	{
        Set<String> set = inventories.keySet();

        for(String userID : set)
        {
            saveInventory(userID);
        }
    }

    public static void saveInventory(String userID)
    {
        IffuInventory inventoryToSave = getInventory(userID);
        try
        (
            FileWriter fwtr = new FileWriter(new File(store.getAbsolutePath() + "/" + userID + ".json"));
            BufferedWriter bwtr = new BufferedWriter(fwtr);
            JsonWriter jwtr = Json.createWriter(bwtr);
        )
        {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add(userID, inventoryToSave.toJSONObj());
            jwtr.write(job.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public static void clearInventory(String userID)
	{
		inventories.get(userID).clear();
	}

	public static void reloadInventories()
	{
		loadInventories(store);
	}
	
	public static boolean addItem(String userID, IffuItem nextItem)
	{
		if(inventories.get(userID) == null)
			inventories.put(userID, new IffuInventory(userID));
		
		if(inventories.get(userID).addItem(nextItem)) {
            saveInventory(userID);
            return true;
        }

        return false;
	}

	public static boolean addSkill(String userID, IffuSkill nextSkill)
	{
		if(inventories.get(userID) == null)
			inventories.put(userID, new IffuInventory(userID));

		if(inventories.get(userID).addSkill(nextSkill.getSkillName()))
        {
            saveInventory(userID);
            return true;
        }

        return false;
	}

    public static IffuItem removeItem(String userID, int place)
    {
        if(inventories.get(userID) == null)
            inventories.put(userID, new IffuInventory(userID));

        IffuItem removedItem = inventories.get(userID).removeItem(place);
        saveInventory(userID);
        return removedItem;
    }

    public static IffuItem grabItem(String userID, int place)
    {
        if(inventories.get(userID) == null)
            inventories.put(userID, new IffuInventory(userID));

        return inventories.get(userID).grabItem(place);
    }

	public static IffuInventory getInventory(String userID)
	{
		IffuInventory nextInventory = inventories.get(userID);
		if(nextInventory == null)
			inventories.put(userID, new IffuInventory(userID));
		return inventories.get(userID);
	}

	public static void remove(String userID)
    {
        IffuInventory oldInventory = inventories.remove(userID);

        if(oldInventory != null)
        {
            File f = new File(store.getAbsolutePath() + "/" + userID + ".json");
            f.delete();
        }
    }
}
