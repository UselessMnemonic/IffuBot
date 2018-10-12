package unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

/*
 * Defines the many Classes a unit can be.
 */
public class IffuClass
{
	private String		className;
	private IffuStat	baseStats;
	private IffuStat	maxStats;
	private IffuStat	growthRates;
	private int			baseLevel;
	private int			maxLvl;
	private String[]	basePromotions;
	private String[]    baseSkills;
	
	public IffuClass()
	{
		className = "";
		basePromotions = new String[0];
		baseSkills = new String[0];
		baseStats = new IffuStat();
		maxStats = new IffuStat();
		growthRates = new IffuStat();
	}
	
	public static ArrayList<IffuClass> fromJSON(File JSONFile)
	{
		ArrayList<IffuClass> classes = new ArrayList<IffuClass>();
		IffuClass nextClass;
		
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
				nextClass = IffuClass.fromJSON(e.getValue().asJsonObject());
				classes.add(nextClass);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return classes;
	}
	
	public static IffuClass fromJSON(JsonObject jso) {

        IffuClass nextClass = new IffuClass();

	    try {
            Object[] nextArray;
            byte[] nextStats;
            String[] nextPromotions;
            String[] nextSkills;

            //get name, base EXP, and max level
            nextClass.className = jso.getString("className");
            nextClass.baseLevel = jso.getInt("baseLevel");
            nextClass.maxLvl = (byte) jso.getInt("maxLvl");

            //get base stats
            nextArray = jso.getJsonArray("baseStats").toArray();
            nextStats = new byte[nextArray.length];
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextStats[i] = (byte) ((JsonNumber) nextArray[i]).intValue();
            nextClass.baseStats = new IffuStat(nextStats);

            //get max stats
            nextArray = jso.getJsonArray("maxStats").toArray();
            nextStats = new byte[nextArray.length];
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextStats[i] = (byte) ((JsonNumber) nextArray[i]).intValue();
            nextClass.maxStats = new IffuStat(nextStats);

            //get growth rates
            nextArray = jso.getJsonArray("growthRates").toArray();
            nextStats = new byte[nextArray.length];
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextStats[i] = (byte) ((JsonNumber) nextArray[i]).intValue();
            nextClass.growthRates = new IffuStat(nextStats);

            //get promotions
            nextArray = jso.getJsonArray("basePromotions").toArray();
            nextPromotions = new String[nextArray.length];
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextPromotions[i] = ((JsonString) nextArray[i]).getString();
            nextClass.setBasePromotions(nextPromotions);

            //get skills
            nextArray = jso.getJsonArray("baseSkills").toArray();
            nextSkills = new String[nextArray.length];
            for (int i = 0; i <= nextArray.length - 1; i++)
                nextSkills[i] = ((JsonString) nextArray[i]).getString();
            nextClass.setBaseSkills(nextSkills);
        }
        catch (Exception e)
        {
            System.err.println("Error in Class \"" + nextClass.getClassName() + "\"");
            e.printStackTrace();
        }

        return nextClass;
	}
	
	public JsonObject toJSONObj()
	{
		JsonArrayBuilder baseStats = Json.createArrayBuilder();
		JsonArrayBuilder maxStats = Json.createArrayBuilder();
		JsonArrayBuilder growthRates = Json.createArrayBuilder();
		JsonArrayBuilder basePromotions = Json.createArrayBuilder();
		
		for(byte b : this.baseStats.getRaw())
			baseStats.add(b);
		for(byte b : this.maxStats.getRaw())
			maxStats.add(b);
		for(byte b : this.growthRates.getRaw())
			growthRates.add(b);
		for(String s : this.basePromotions)
			basePromotions.add(s);
		
		JsonObject ret = Json.createObjectBuilder()
				.add("className", className)
				.add("baseStats", baseStats.build())
				.add("maxStats", maxStats.build())
				.add("growthRates", growthRates.build())
				.add("baseLevel", baseLevel)
				.add("maxLvl", maxLvl)
				.add("basePromotions", basePromotions.build())
				.build();
		
		return ret;
	}
	

	public String getClassName()
	{
		return className;
	}

	
	public void setClassName(String className)
	{
		this.className = className;
	}
	

	public IffuStat getBaseStats()
	{
		return baseStats;
	}
	

	public void setBaseStats(IffuStat baseStats)
	{
		this.baseStats = baseStats;
	}
	

	public IffuStat getMaxStats()
	{
		return maxStats;
	}
	

	public void setMaxStats(IffuStat maxStats)
	{
		this.maxStats = maxStats;
	}
	

	public IffuStat getGrowthRates()
	{
		return growthRates;
	}
	

	public void setGrowthRates(IffuStat growthRates)
	{
		this.growthRates = growthRates;
	}
	

	public int getBaseLevel()
	{
		return baseLevel;
	}
	

	public void setBaseLevel(int baseLevel)
	{
		this.baseLevel = baseLevel;
	}
	

	public int getMaxLvl()
	{
		return maxLvl;
	}
	

	public void setMaxLvl(int maxLvl)
	{
		this.maxLvl = maxLvl;
	}
	

	public String[] getBasePromotions()
	{
		return basePromotions;
	}
	

	public void setBasePromotions(String[] basePromotions)
	{
		this.basePromotions = basePromotions;
	}

	public String[] getBaseSkills(){return baseSkills;}

	public void setBaseSkills(String[] skills){baseSkills = skills;}

	public String toString()
	{
		return className + ", " + baseStats.toString() + ", " + maxStats.toString() + ", " + growthRates.toString() + ", " + baseLevel + ", " + maxLvl + ", " + basePromotions;
	}
}