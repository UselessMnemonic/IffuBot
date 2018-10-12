package unit;

import unitCards.IffuCardResourceManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

public class IffuClassRegistry
{
	private static HashMap<String, IffuClass> classMap;
	private static File store;
	
	public static void loadClasses(String filename)
	{
		File f = new File(filename);
		loadClasses(f);
	}
	
	public static void loadClasses(File loc)
	{
		classMap = new HashMap<String, IffuClass>();
		store = loc;
		store.setReadOnly();
		ArrayList<IffuClass> classes;
		
		if(store.exists())
		{
			if(store.isDirectory())
			{
				File[] list = store.listFiles();
				classes = new ArrayList<IffuClass>();
				
				for(File nextFile : list)
				{					
					classes.addAll(IffuClass.fromJSON(nextFile));
				}
			}
			else
				classes = IffuClass.fromJSON(store);
			
			for(IffuClass ic : classes)
				addClass(ic);
		}
	}

	public static IffuClass getClass(String className)
	{
		IffuClass ret = classMap.get(className);
		if(ret == null)
			ret = classMap.get("Outrealm Class");
		return ret;
	}
	
	public static void addClass(IffuClass ic)
	{
	    String reducednamed = ic.getClassName().toLowerCase().replaceAll("\\s","");
		classMap.put(ic.getClassName(), ic);
		IffuCardResourceManager.registerClassIcon(ic.getClassName(), "res/classSprites/" + reducednamed + ".png");
	}

	public static void reloadClasses()
	{
		loadClasses(store);
	}

	public static void printClasses()
	{
		System.out.println("[IffuClassRegistry]: These are the class definitions currently on file.");
		Collection<IffuClass> classList = classMap.values();
		for(IffuClass ic : classList)
			System.out.println(ic.getClassName());
			
	}

    public static IffuClass getNullableClass(String className)
    {
        return classMap.get(className);
    }
}
