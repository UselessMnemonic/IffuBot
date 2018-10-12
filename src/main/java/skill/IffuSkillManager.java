package skill;

import unitCards.IffuCardResourceManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class IffuSkillManager
{
    private static HashMap<String, IffuSkill> skillDefs;
    private static File store;

    public static void loadSkillDefs(File loc)
    {
        store = loc;
        store.setReadOnly();
        skillDefs = new HashMap<String, IffuSkill>();
        ArrayList<IffuSkill> skills = new ArrayList<IffuSkill>();

        if(store.exists())
        {
            if(store.isDirectory())
            {
                File[] list = store.listFiles();

                for(File nextFile : list)
                {
                    skills.addAll(IffuSkill.fromJSON(nextFile));
                }
            }
            else
                skills = IffuSkill.fromJSON(store);

            for(IffuSkill is : skills)
            {
                String nextName = is.getSkillName().replaceAll("\\s","").toLowerCase();
                skillDefs.put(is.getSkillName(), is);
                IffuCardResourceManager.registerSkillIcon(is.getSkillName(), "res/skillIcons/" + nextName + ".png");
                assert(skillDefs.get(is.getSkillName()) != null);
            }
        }
    }

    public static void reloadSkillDefs()
    {
        loadSkillDefs(store);
    }

    public static IffuSkill getInstance(String skillName)
    {
        IffuSkill ret = skillDefs.get(skillName);

        if(ret == null)
            ret = skillDefs.get(skillName.replaceAll("\\s","").toLowerCase());

        return ret;
    }

    public static String skillList()
    {
        String ret = "";

        Collection<IffuSkill> skillList = skillDefs.values();

        for(IffuSkill is : skillList)
        {
            ret += is.getSkillName() + '\n';
        }

        return ret;
    }

    public static void loadSkillDefs(String loc)
    {
        loadSkillDefs(new File(loc));
    }

    public static void printItems()
    {
        System.out.println("[IffuItemManager]: These are the item definitions currently on file.");
        Collection<IffuSkill> skillList = skillDefs.values();
        for(IffuSkill is : skillList)
            System.out.println(is.getSkillName());

    }
}
