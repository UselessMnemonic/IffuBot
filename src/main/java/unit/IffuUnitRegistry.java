package unit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import net.dv8tion.jda.core.entities.User;

public class IffuUnitRegistry
{
	private static HashMap<String, IffuUnit> unitMap;
	private static File store;
	
	public static void loadUnits(String filename)
	{
		File f = new File(filename);
		loadUnits(f);
	}
	
	public static void loadUnits(File loc)
	{
		unitMap = new HashMap<String, IffuUnit>();
		store = loc;

        if(!store.isDirectory())
        {
            System.out.println("Invalid unit store path, defaulting to sub-directory \"unitstore\"");
            store = new File("unitstore");
        }

        File[] list = store.listFiles();

        IffuUnit nextUnit;
        for(File nextFile : list)
        {
            nextUnit = IffuUnit.fromJSON(nextFile);
            unitMap.put(nextUnit.getUserID(), nextUnit);
        }
	}
	
	public static void saveUnits()
	{
		Set<String> set = unitMap.keySet();

        for(String userID : set)
        {
            saveUnit(userID);
        }
	}

	public static void saveUnit(String userID)
    {
        IffuUnit unitToSave = getMember(userID);
        try
        (
            FileWriter fwtr = new FileWriter(new File(store.getAbsolutePath() + "/" + userID + ".json"));
            BufferedWriter bwtr = new BufferedWriter(fwtr);
            JsonWriter jwtr = Json.createWriter(bwtr);
        )
        {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add(userID + '.' + unitToSave.getUserName(), unitToSave.toJSONObj());
            jwtr.write(job.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public static boolean isRegistered(String memberID)
	{
		return unitMap.get(memberID) != null;
	}
	
	public static boolean isRegistered(User user)
	{
		return isRegistered(user.getId());
	}

	public static boolean deregisterMember(String memberID)
	{
		IffuUnit oldUnit = unitMap.remove(memberID);
		
		if(oldUnit != null)
		{
		    File f = new File(store.getAbsolutePath() + "/" + oldUnit.getUserID() + ".json");
		    boolean success = f.delete();
		    if(!success)
		    	unitMap.put(memberID, oldUnit);
		    return success;
		}
		else
			return false;
	}

	public static boolean updateMember(String memberID, IffuUnit newUnit)
	{
		if(newUnit != null)
		{
		    unitMap.put(memberID, newUnit);
		    saveUnit(memberID);
			return true;
		}
		else
			return false;
	}
	
	public static boolean renameMember(String memberID, String newName)
	{
		IffuUnit oldUnit = unitMap.get(memberID);
		
		if(oldUnit != null)
		{
			oldUnit.setUserName(newName);
            saveUnit(memberID);
			return true;
		}
		else
			return false;
	}

	public static boolean registerMember(IffuUnit newUnit)
	{
		if(newUnit == null)
			return false;

		unitMap.put(newUnit.getUserID(), newUnit);
        saveUnit(newUnit.getUserID());
		return true;
	}
	
	public static IffuUnit getMember(String memberID)
	{
		return unitMap.get(memberID);
	}

	public static void reloadUnits()
	{
		loadUnits(store);
	}

	public static ArrayList<IffuUnit> getAll()
	{
		ArrayList<IffuUnit> ret = new ArrayList<IffuUnit>();
		
		Set<Entry<String, IffuUnit>> units = unitMap.entrySet();
		
		for(Entry<String, IffuUnit> unit : units)
			ret.add(unit.getValue());
		
		return ret;
	}
	
}