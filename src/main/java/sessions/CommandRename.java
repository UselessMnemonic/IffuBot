package sessions;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

import unit.IffuUnit;
import unit.IffuUnitRegistry;

public class CommandRename extends Command
{
    public CommandRename()
    {
        this.name = "rename";
        this.help = "Rename the unit (Max 25 Characters allowed).";
        this.guildOnly = false;
    }

    @Override
    protected void execute(CommandEvent event)
    {
    	if(!IffuUnitRegistry.isRegistered(event.getAuthor().getId()))
    		event.reply("You must register with \"iffu?register\" first.");
    	else
    	{
    		IffuUnit u = IffuUnitRegistry.getMember(event.getAuthor().getId());
    		
    		if(event.getArgs().isEmpty())
    			event.reply("Your name must be at least one character long.");
    		else if(event.getArgs().length() > 25)
    			event.reply("Your name is too long! 25 characters max.");
    		else
    		{
    			IffuUnitRegistry.renameMember(event.getAuthor().getId(), event.getArgs());
        		event.reply("You have been renamed to \"" + u.getUserName() + '\"');
    		}
    	}
    	
    }
}
