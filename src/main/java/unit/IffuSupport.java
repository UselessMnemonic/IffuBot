package unit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

public class IffuSupport
{
	private String 	supportA;
	private String 	supportB;
	private int		supportFromA,
					supportFromB;
	private boolean	supportAPartnerPending,
					supportBPartnerPending;
	
	public boolean makePartners(String userID)
	{
		if(userID.equals(supportA))
			supportAPartnerPending = true;
		else if(userID.equals(supportB))
			supportBPartnerPending = true;
		
		return arePartners();
	}
	
	public boolean arePartners()
	{
		return supportBPartnerPending && supportAPartnerPending;
	}
	
	public void dropPartner(String userID)
	{
		if(userID.equals(supportA))
			supportAPartnerPending = false;
		else if(userID.equals(supportB))
			supportBPartnerPending = true;
	}
	
	public IffuSupport(String userA, String userB)
	{
		supportA = userA;
		supportB = userB;
		supportAPartnerPending = false;
		supportBPartnerPending = true;
		supportFromA = 0;
		supportFromB = 0;
	}

	private IffuSupport()
	{

	}

	public void increment(String userID)
	{
		if(!arePartners())
		{
			if(supportA.equals(userID))
				supportFromA++;
			else if(supportB.equals(userID))
				supportFromB++;
			
			if(supportFromA > 200)
				supportFromA = 200;
			if(supportFromB > 200)
				supportFromB = 200;
		}
	}
	
	public String getUserA()
	{
		return supportA;
	}
	
	public String getUserB()
	{
		return supportB;
	}
	
	public int getSupportLevel()
	{
		return supportFromA < supportFromB ? supportFromA : supportFromB;
	}
	
	public String getSupportRank()
	{
	    if(arePartners())
	        return "S";

		int supportLevel = getSupportLevel();

		if(supportLevel < 0)
			return "?";
		else if(supportLevel < 10)
			return "F";
		else if (supportLevel < 20)
			return "D";
		else if (supportLevel < 50)
			return "C";
		else if(supportLevel < 100)
			return "B";
		else if(supportLevel < 200)
			return "A";
		else
			return "A+";
	}

	public static IffuSupport fromJSON(JsonObject jso)
	{
		IffuSupport nextSupport = new IffuSupport();
		
		//get name, id, exp, and level
		nextSupport.supportA = jso.getString("supportA");
		nextSupport.supportB = jso.getString("supportB");
		nextSupport.supportFromA = jso.getInt("supportFromA");
		nextSupport.supportFromB = jso.getInt("supportFromB");
		nextSupport.supportAPartnerPending = jso.getBoolean("supportAPartnerPending");
		nextSupport.supportBPartnerPending = jso.getBoolean("supportBPartnerPending");
		
		return nextSupport;
	}
	
	public JsonObject toJSONObj()
	{
		JsonObject ret = Json.createObjectBuilder()
				.add("supportA", supportA)
				.add("supportB", supportB)
				.add("supportFromA", supportFromA)
				.add("supportFromB", supportFromB)
				.add("supportAPartnerPending", supportAPartnerPending)
				.add("supportBPartnerPending", supportBPartnerPending)
				.build();
		return ret;
	}

	public static IffuSupport fromJSON(File JSONFile)
	{
		IffuSupport nextSupport = null;
		
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
				nextSupport = IffuSupport.fromJSON(e.getValue().asJsonObject());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return nextSupport;
	}
	
	public String getSupport(String userID)
	{
		if(userID.equals(supportA))
			return supportB;
		else if(userID.equals(supportB))
			return supportA;
		else
			return null;
	}
}
