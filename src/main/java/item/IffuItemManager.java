package item;

import unitCards.IffuCardResourceManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class IffuItemManager
{
	private static HashMap<String, IffuItem> itemDefs;
	private static File store;

	public static void loadItemDefs(File loc)
	{
		store = loc;
		store.setReadOnly();
		itemDefs = new HashMap<String, IffuItem>();
		ArrayList<IffuItem> items = new ArrayList<IffuItem>();
		
		if(store.exists())
		{
			if(store.isDirectory())
			{
				File[] list = store.listFiles();
				
				for(File nextFile : list)
				{					
					items.addAll(IffuItem.fromJSON(nextFile));
				}
			}
			else
				items = IffuItem.fromJSON(store);
			
			for(IffuItem ii : items)
			{
				String nextName = ii.getItemName().replaceAll("\\s","").toLowerCase();
				itemDefs.put(nextName, ii);
				IffuCardResourceManager.registerItemIcon(nextName, "res/itemIcons/" + nextName + ".png");
			}
		}
	}
	
	public static void reloadItemDefs()
	{
		loadItemDefs(store);
	}
	
	public static IffuItem getInstance(String itemName)
	{
		IffuItem ret = itemDefs.get(itemName);

		if(ret == null)
			ret = itemDefs.get(itemName.replaceAll("\\s","").toLowerCase());

		if(ret == null)
		    return null;

		return new IffuItem(ret);
	}
	
	public static String itemList()
	{
		String ret = "";
		
		Collection<IffuItem> itemList = itemDefs.values();
		
		for(IffuItem ii : itemList)
		{
			ret += ii.getItemName() + '\n';
		}
		
		return ret;
	}

	public static void loadItemDefs(String loc)
	{
		loadItemDefs(new File(loc));
	}

	public static void printItems()
	{
		System.out.println("[IffuItemManager]: These are the item definitions currently on file.");
		Collection<IffuItem> itemList = itemDefs.values();
		for(IffuItem ii : itemList)
			System.out.println(ii.getItemName());
			
	}
}
