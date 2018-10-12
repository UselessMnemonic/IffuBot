package unit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import misc.HashTable;
import net.dv8tion.jda.core.entities.User;

public class IffuSupportTable
{
	private static HashTable<String, IffuSupport> supportMap;
	private static File store;

    public static void loadSupports(File loc)
    {
        supportMap = new HashTable<String, IffuSupport>();
        store = loc;

        if(!store.isDirectory())
        {
            System.out.println("Invalid support store path, defaulting to sub-directory \"supportstore\"");
            store = new File("supportstore");
        }

        File[] list = store.listFiles();

        IffuSupport nextSupport;
        for(File nextFile : list)
        {
            nextSupport = IffuSupport.fromJSON(nextFile);
            supportMap.put(nextSupport.getUserA(), nextSupport.getUserB(), nextSupport);
        }
    }
	
	public static void saveSupports()
	{
	    for(Entry<String, HashMap<String, IffuSupport>> entry : supportMap.entrySet())
        {
            for(Entry<String, IffuSupport> support : entry.getValue().entrySet())
            {
                saveSupport(support.getValue());
            }
        }
	}

	public static void saveSupport(String userA, String userB)
    {
        IffuSupport supportToSave = getSupport(userA, userB);
        saveSupport(supportToSave);
    }

    public static void saveSupport(IffuSupport supportToSave)
    {
        try
                (
                        FileWriter fwtr = new FileWriter(new File(store.getAbsolutePath() + "/" + supportToSave.getUserA() + '.' + supportToSave.getUserB() + ".json"));
                        BufferedWriter bwtr = new BufferedWriter(fwtr);
                        JsonWriter jwtr = Json.createWriter(bwtr);
                )
        {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add(supportToSave.getUserA() + '.' + supportToSave.getUserB(), supportToSave.toJSONObj());
            jwtr.write(job.build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public static ArrayList<IffuSupport> getSupports(String userID)
	{
		ArrayList<IffuSupport> supportList = new ArrayList<IffuSupport>();

		String nextSupportID;

		Set<Entry<String, IffuSupport>> entries = supportMap.getIntersection(userID);
		
		for(Entry<String, IffuSupport> entry : entries)
		{
			nextSupportID = entry.getValue().getSupport(userID);

			if(nextSupportID != null && entry.getKey().equals(userID))
				if(entry.getValue().getSupportLevel() > 0)
					supportList.add(entry.getValue());
		}
		
		return supportList;
	}

	public static IffuSupport getPartner(String userID)
    {

        String nextSupportID;

        Set<Entry<String, IffuSupport>> entries = supportMap.getIntersection(userID);

        for(Entry<String, IffuSupport> entry : entries)
        {
            nextSupportID = entry.getValue().getSupport(userID);

            if(nextSupportID != null && entry.getKey().equals(userID))
                if(entry.getValue().arePartners())
                    return entry.getValue();
        }

        return null;
    }
	
	public static IffuSupport getSupport(String userA, String userB)
	{
		return supportMap.get(userA, userB);
	}
	
	public static IffuSupport getSupport(User userA, User userB)
	{
		return supportMap.get(userA.getId(), userB.getId());
	}

	public static void addSupport(String userA, String userB)
	{
		supportMap.put(userA, userB, new IffuSupport(userA, userB));
		saveSupport(userA, userB);
	}

	public static void loadSupports(String loc)
	{
		loadSupports(new File(loc));
	}

	public static void reloadSupports()
	{
		loadSupports(store);
	}

	public static void removeAll(String userID)
	{
        Set<Entry<String, IffuSupport>> set = supportMap.removeAll(userID);
        IffuSupport supportToRemove;
        File supportFile;

        for(Entry<String, IffuSupport> entry : set)
        {
            supportToRemove = entry.getValue();
            supportFile = new File(store + "/" + supportToRemove.getUserA() + "." + supportToRemove.getUserB() + ".json");
            supportFile.delete();
        }
	}
}
