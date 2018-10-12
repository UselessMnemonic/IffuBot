package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import item.IffuInventoryRegistry;
import item.IffuItem;
import item.IffuItemManager;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import skill.IffuSkill;
import skill.IffuSkillManager;
import unit.IffuUnitRegistry;

import java.util.List;

public class CommandGiveSkill extends Command
{
    public CommandGiveSkill()
    {
        this.name = "giveskill";
        this.help = "Gives a skill to a unit.  (Owner only)";
        this.guildOnly = false;
        this.ownerCommand = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	List<User> toWhom = event.getMessage().getMentionedUsers();
		String toParse = event.getMessage().getContentDisplay();
    	toParse = toParse.substring(toParse.lastIndexOf('$')+1);
    	
    	if(toParse.isEmpty())
    	{
    		event.reply("Error in parsing command.");
    		return;
    	}
    	
    	IffuSkill skillToGive = IffuSkillManager.getInstance(toParse);

    	if(skillToGive == null)
    	{
    		event.reply("The specified skill does not exist.");
    		return;
    	}
    	
    	for(User u : toWhom)
    	{
			Member nextMember = event.getGuild().getMemberById(u.getId());
			if(nextMember == null)
				event.reply(u.getName() + " is not on this server.");
    		else if(!IffuUnitRegistry.isRegistered(u.getId()))
    			event.reply(nextMember.getEffectiveName() + " must register.");
    		else
    		{
    			if(IffuInventoryRegistry.addSkill(u.getId(), skillToGive))
    			    event.reply("Gave \"" + skillToGive.getSkillName() + "\" to " + u.getName());
    			else
                    event.reply("Could not give \"" + skillToGive.getSkillName() + "\" to " + u.getName());
    		}
    	}
    }
}