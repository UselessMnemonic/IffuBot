package misc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

public class HashTable<K, V>
{
	HashMap<K, HashMap<K, V>> table;
	
	public HashTable()
	{
		table = new HashMap<K, HashMap<K, V>>();
	}
	
	public V get(K key1, K key2)
	{
		HashMap<K,V> set1 = table.get(key1);
		HashMap<K,V> set2 = table.get(key2);
		
		if(set1 != null)
		{
			//if key1 has a map, check for a value from key2
			if(set1.get(key2) != null)
				return set1.get(key2);
			else
			{
				//if key1 has no map, check if key2 has one.
				if(set2 != null)
					return set2.get(key1);
				else
					return null;
			}
		}
		else if(set2 != null)
		{
			//if key1 has no hashmap, then the result should be whatever is in key2's hashmap with key1 as the lag
			return set2.get(key1);
		}
		else
			return null;
	}
	
	public V put(K key1, K key2, V item)
	{
		//assume key1 is the leading key
		HashMap<K, V> enteries = table.get(key1);
		if(enteries != null)
		{
			//if there is a hashmap storing key1's items, put item in that, with key2 as lagging key
			return enteries.put(key2, item);
		}
		else//if key1 is not the leading key, assume key2 is the leading key
		{
			enteries = table.get(key2);
			//if there is a hashmap storing key2's items, put item in that, with key1 as lagging key
			if(enteries != null)
				return enteries.put(key1, item);
			else
			{//if neither key1 nor key2 are the leading keys, then give key1 the lead and key2 the lag
				table.put(key1, new HashMap<K, V>());
				return table.get(key1).put(key2, item);
			}
		}
	}

	public Set<Entry<K, HashMap<K, V>>> entrySet()
	{
		return table.entrySet();
	}

    public Set<Entry<K, V>> getIntersection(K key)
    {
        Set<Entry<K, V>> returnSet = new HashSet<>();
        Set<Entry<K, HashMap<K, V>>> tableEntries = this.entrySet();
        Set<Entry<K, V>> currentEntrySet;

        //for all sub-maps in the table
        for(Entry<K, HashMap<K, V>> tableEntry : tableEntries)
        {
            currentEntrySet = tableEntry.getValue().entrySet();

            //for all entries in all sub-maps of the table
            for(Entry<K, V> entry : currentEntrySet)
            {
                assert(entry != null);

                if (tableEntry.getKey().equals(key))
                    returnSet.add(new SimpleEntry<>(tableEntry.getKey(), entry.getValue()));
                else if(entry.getKey().equals(key))
                    returnSet.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
            }
        }

        return returnSet;
    }
	
	public String toString()
	{
		String ret = "";
		
		Set<Entry<K, V>> entrySet;
		Set<Entry<K, HashMap<K, V>>> mapSet = entrySet();
		
		//for all the key-hashmap pairs in the table...
		for(Entry<K, HashMap<K, V>> map : mapSet)
		{
			entrySet = map.getValue().entrySet();
			ret += map.getKey() + "\n----------\n";
			//for all the key-value pairs in the hashmaps of the key-hashmap pairs in the table
			for(Entry<K, V> entry : entrySet)
			{
				ret += entry.getKey() + " -> " + entry.getValue() + "\n";
			}
			ret+="\n";
		}
		
		
		return ret;
	}

	public Set<Entry<K, V>> removeAll(K key)
	{
		Set<Entry<K, V>> removed = new HashSet<Entry<K, V>>();
		V trashedObject;
		
		//first remove any hashmap belonging to the key
		HashMap<K, V> result = table.remove(key);
		
		//then add its entries to the trash
		if(result != null)
			removed.addAll(result.entrySet());
		
		//now grab the keys for the rest of the hashmaps
		Set<K> keysToCheck = table.keySet();
		
		//check the other hashmaps for entries with the specified key
		for(K keyToCheck : keysToCheck)
		{
			trashedObject = table.get(keyToCheck).get(key);
			
			if(trashedObject != null)
				removed.add(new SimpleEntry<K, V>(keyToCheck, trashedObject));
		}
		
		return removed;
	}

	public V remove(K key1, K key2)
	{
		if(table.get(key1) != null)
		{
			//if key1 has a map, check it for a value with key2
			if(table.get(key1).get(key2) != null)
				return table.get(key1).remove(key2);
			else
			{
				//if key1 has no map, check if key2 has one.
				if(table.get(key2) != null)
					return table.get(key2).remove(key1);
				else
					return null;
			}
		}
		else if(table.get(key2) != null)
		{
			//if key1 has no hashmap, then the result should be whatever is in key2's hashmap with key1 as the lag
			return table.get(key2).remove(key1);
		}
		else
			return null;
	}
}
