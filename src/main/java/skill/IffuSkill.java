package skill;

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
import unit.IffuUnit;

public class IffuSkill
{
    private String skillName;
    private byte[] statModifiers;
    private byte[] growthModifiers;
    private int baseLevel;

    public String getSkillName()
    {
        return skillName;
    }

    public void setSkillName(String skillName)
    {
        this.skillName = skillName;
    }

    public IffuStat getStatModifiers()
    {
        return new IffuStat(statModifiers);
    }

    public IffuStat getGrowthModifiers()
    {
        return new IffuStat(growthModifiers);
    }

    public void setStatModifiers(IffuStat statModifiers)
    {
        this.statModifiers = statModifiers.getRaw();
    }

    public void setGrowthModifiers(IffuStat growthModifiers)
    {
        this.growthModifiers = growthModifiers.getRaw();
    }

    public int getBaseLevel(){return baseLevel;}

    public void setBaseLevel(int baseLevel){this.baseLevel = baseLevel;}

    private IffuSkill()
    {

    }

    public static IffuSkill fromJSON(JsonObject jso)
    {
        IffuSkill nextSkill = new IffuSkill();
        Object[] nextArray;

        nextSkill.setSkillName(jso.getString("skillName"));

        nextArray = jso.getJsonArray("statBonuses").toArray();
        nextSkill.statModifiers = new byte[nextArray.length];
        for(int i = 0; i <= nextArray.length - 1; i++)
            nextSkill.statModifiers[i] = (byte) ((JsonNumber)nextArray[i]).intValue();

        nextArray = jso.getJsonArray("growthBonuses").toArray();
        nextSkill.growthModifiers = new byte[nextArray.length];
        for(int i = 0; i <= nextArray.length - 1; i++)
            nextSkill.growthModifiers[i] = (byte) ((JsonNumber)nextArray[i]).intValue();

        nextSkill.baseLevel = jso.getInt("baseLevel");

        return nextSkill;
    }

    public static ArrayList<IffuSkill> fromJSON(File JSONFile)
    {
        ArrayList<IffuSkill> skills = new ArrayList<IffuSkill>();
        IffuSkill nextSkill;

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
                nextSkill = IffuSkill.fromJSON(e.getValue().asJsonObject());
                skills.add(nextSkill);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return skills;
    }

    public boolean canLearn(IffuUnit iffuUnit)
    {
        return iffuUnit.getLevel() >= getBaseLevel();
    }
}


