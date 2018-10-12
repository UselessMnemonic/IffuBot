package sessions;

import java.util.List;
import java.util.ArrayList;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import misc.Markup;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;
import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandListUnits extends Command
{
    public CommandListUnits()
    {
        this.name = "listUnits";
        this.help = "List all units.";
        this.guildOnly = true;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	List<Member> guildMemberList;
    	guildMemberList = event.getGuild().getMembers();
    	String response;
    	
    	guildMemberList.removeIf(m -> !IffuUnitRegistry.isRegistered(m.getUser().getId()));
    	
    	if(guildMemberList.isEmpty())
    		response = "There are not units on this server! Use `iffu?register` to start. :)";
    	else
    	{
    		response = "The units on this server are...\n";

    		for(Member m : guildMemberList)
        	{
        		IffuUnit target = IffuUnitRegistry.getMember(m.getUser().getId());

        		response += Markup.bold(target.getUserName()) + ' ' + m.getEffectiveName() + '\n';
        		response += Markup.italic(target.getUserClass().getClassName() + " ~ Lv. " + target.getLevel()) + '\n';
        	}
    	}
    	
    	event.reply(response);
    	
    }
}
